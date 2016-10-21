package com.barista_v.debug_artist

import android.app.Application
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.phoenix.RestartListener
import com.barista_v.debug_artist.item.spinner.SpinnerDrawerItem
import com.barista_v.debug_artist.item.spinner.SpinnerItemListener
import com.facebook.stetho.Stetho
import com.github.pedrovgs.lynx.LynxActivity
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.scalpel.ScalpelFrameLayout
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SwitchDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference

/**
 * Debug drawer showing some debugging tools and info.

 * To add dynamic actions check "debugDrawer.with*" methods.
 */
class DebugDrawer(application: Application, activity: AppCompatActivity) : OnCheckedChangeListener,
    Drawer.OnDrawerItemClickListener, SpinnerItemListener {

  private var activityWeakReference: WeakReference<AppCompatActivity>
  private var applicationWeakReference: WeakReference<Application>
  private var weakScalpelLayout: WeakReference<ScalpelFrameLayout>? = null
  private val menuDrawer: Drawer
  private var restartListener: RestartListener? = null
  private var inputItemListener: InputItemListener? = null

  init {
    activityWeakReference = WeakReference(activity)
    applicationWeakReference = WeakReference(application)

    menuDrawer = DrawerBuilder(activity).withTranslucentStatusBar(true)
        .withDrawerGravity(Gravity.END)
        .withShowDrawerOnFirstLaunch(true)
        .build()
        .apply { onDrawerItemClickListener = this@DebugDrawer }

    menuDrawer.addItems(
        PrimaryDrawerItem()
            .withName("Q&A Module")
            .withDescription("Drag from right to left to open")
            .withSelectable(false)
            .withEnabled(false),
        DividerDrawerItem())

  }

  fun openDrawer(): DebugDrawer {
    menuDrawer.openDrawer()
    return this
  }

  fun withLeakCanarySwitch(checked: Boolean = true): DebugDrawer {
    addSwitchDrawerItem("Leak Canary - Memory Leaks", R.id.drawer_dev_item_leak)
        .withChecked(checked)
    enableLeakCanary()
    return this
  }

  fun withStethoSwitch(): DebugDrawer {
    addSwitchDrawerItem("Stetho - Debug from Chrome", R.id.drawer_dev_item_stetho)
    return this
  }

  fun withPicassoLogsSwitch(): DebugDrawer {
    addSwitchDrawerItem("Picasso Image Cache Logs", R.id.drawer_dev_item_picasso)
    return this
  }

  fun withScalpelSwitch(layout: ScalpelFrameLayout?): DebugDrawer {
    layout?.let {
      weakScalpelLayout = WeakReference(it)
      addSwitchDrawerItem("Scalpel - 3D layouts", R.id.drawer_dev_item_scalpel)
    }

    return this
  }

  fun withLynksButton(): DebugDrawer {
    menuDrawer.addItem(PrimaryDrawerItem().withName("Lynks - Live Device Log")
        .withIdentifier(R.id.drawer_dev_item_lynks.toLong())
        .withIcon(R.drawable.ic_android_grey_700_18dp))

    return this
  }

  fun withPhoenixRestartButtons(restartListener: RestartListener): DebugDrawer {
    this.restartListener = restartListener

    menuDrawer.addItems(
        PrimaryDrawerItem().withName("Restart App")
            .withIdentifier(R.id.drawer_dev_item_phoenix_app.toLong())
            .withIcon(R.drawable.ic_gavel_grey_700_18dp),

        PrimaryDrawerItem().withName("Restart Activity")
            .withIdentifier(R.id.drawer_dev_item_phoenix_activity.toLong())
            .withIcon(R.drawable.ic_rowing_grey_700_18dp))

    return this
  }

  fun withSpinnerItem(id: Int, name: String, options: Array<String>, selectedItem: String,
                      listener: SpinnerItemListener): DebugDrawer {
    menuDrawer.addItem(SpinnerDrawerItem(id, options, listener, options.indexOf(selectedItem))
        .withMoreListeners(this)
        .withName(name))

    return this
  }

  fun withSpinnerItem(id: Int, name: String, options: Array<String>, selectedItem: Int,
                      listener: SpinnerItemListener): DebugDrawer {
    menuDrawer.addItem(SpinnerDrawerItem(id, options, listener, selectedItem)
        .withMoreListeners(this)
        .withName(name))

    return this
  }

  /**
   * Add an item that shows an input dialog.
   */
  fun withInputItem(id: Int, name: String, inputItemListener: InputItemListener): DebugDrawer {
    this.inputItemListener = inputItemListener

    menuDrawer.addItem(PrimaryDrawerItem()
        .withName(String.format("Enter value for '%s'", name))
        .withTag(id)
        .withIcon(R.drawable.ic_textsms_grey_700_18dp)
        .withIdentifier(R.id.drawer_dev_item_input.toLong()))

    return this
  }

  fun withDivider(): DebugDrawer {
    menuDrawer.addItem(DividerDrawerItem())
    return this
  }

  fun withInfoProperties(properties: Map<String, String>): DebugDrawer {
    properties.entries.forEach {
      menuDrawer.addItem(SecondaryDrawerItem()
          .withName(it.key)
          .withIcon(R.drawable.ic_info_grey_700_24dp)
          .withDescription(it.value)
          .withIdentifier(R.id.drawer_dev_item_info.toLong()))
    }

    return this
  }

  private fun showToast(dialog: String) = activityWeakReference.get()?.let {
    Toast.makeText(it, dialog, Toast.LENGTH_LONG).show()
  }

  fun restartApp() = applicationWeakReference.get()?.let {
    restartListener?.onAppRestart()
    ProcessPhoenix.triggerRebirth(it)
  }

  fun restartActivity() = activityWeakReference.get()?.let {
    restartListener?.onActivityRestart()
    ProcessPhoenix.triggerRebirth(it)
  }

  private fun showInputDialog(drawerItem: PrimaryDrawerItem) = activityWeakReference.get()?.let {
    val factory = LayoutInflater.from(it)
    val entryView = factory.inflate(R.layout.input_view, null) as EditText

    AlertDialog.Builder(it)
        .setTitle(drawerItem.name.toString())
        .setView(entryView)
        .setPositiveButton("OK") { dialog, which ->
          val inputText = entryView.text.toString()
          inputItemListener?.onInputOkClick(drawerItem.tag as Int, inputText)
          drawerItem.withDescription(inputText)
          menuDrawer.updateItem(drawerItem)
        }.show()
  }

  private fun addSwitchDrawerItem(text: String, id: Int): SwitchDrawerItem {
    val item = SwitchDrawerItem().withName(text).withIdentifier(id.toLong()).apply {
      withOnCheckedChangeListener(this@DebugDrawer)
    }
    menuDrawer.addItem(item)

    return item
  }

  private fun enableLeakCanary() {
    applicationWeakReference.get()?.let {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      if (LeakCanary.isInAnalyzerProcess(it)) return

      LeakCanary.install(it)
    }
  }

  //<editor-fold desc="Select events">

  override fun onCheckedChanged(drawerItem: IDrawerItem<Any, RecyclerView.ViewHolder>,
                                buttonView: CompoundButton, isChecked: Boolean) {
    val activity = activityWeakReference.get() ?: return

    when (drawerItem.identifier) {
      R.id.drawer_dev_item_scalpel.toLong() -> {
        val toggleDrawerItem = drawerItem as SwitchDrawerItem
        weakScalpelLayout?.get()?.apply {
          isLayerInteractionEnabled = toggleDrawerItem.isChecked
          setDrawViews(toggleDrawerItem.isChecked)
          chromeShadowColor = android.R.color.black
        }
      }
      R.id.drawer_dev_item_stetho.toLong() -> {
        showToast("On chrome open chrome://inspect")
        Stetho.initializeWithDefaults(activity)
      }
      R.id.drawer_dev_item_leak.toLong() -> enableLeakCanary()
      R.id.drawer_dev_item_picasso.toLong() -> {
        showToast("Picasso logs cant be disabled.")
        val picassoStats = Picasso.with(activity).apply {
          setIndicatorsEnabled(true)
          isLoggingEnabled = true
        }.snapshot

        showToast("Log and Indicators enabled: " +
            "\ngreen (memory, best performance)\n" +
            " blue (disk, good performance)\n" +
            " red (network, worst performance)." +
            "\n\nStats(on logs too): \n" + picassoStats.toString())

        Log.i("DEBUG", "Picasso stats:" + picassoStats.toString())
      }
    }
  }

  override fun onItemClick(view: View, position: Int, drawerItem: IDrawerItem<Any,
      RecyclerView.ViewHolder>): Boolean {
    when (drawerItem.identifier) {
      R.id.drawer_dev_item_info.toLong() -> (drawerItem as SecondaryDrawerItem).apply {
        withSetSelected(false)
        showToast(description.text)
      }
      R.id.drawer_dev_item_phoenix_activity.toLong() -> restartActivity()
      R.id.drawer_dev_item_phoenix_app.toLong() -> restartApp()
      R.id.drawer_dev_item_input.toLong() -> showInputDialog(drawerItem as PrimaryDrawerItem)
      R.id.drawer_dev_item_lynks.toLong() -> activityWeakReference.get()?.apply {
        startActivity(LynxActivity.getIntent(applicationContext))
      }
    }

    return true
  }

  override fun onSpinnerItemClick(item: SpinnerDrawerItem, itemId: Int, title: CharSequence) {
    item.withDescription(title.toString())
    menuDrawer.updateItem(item)
  }

  //</editor-fold>

}

