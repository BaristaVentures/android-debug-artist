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
class DebugDrawer(application: Application, activity: AppCompatActivity) : OnCheckedChangeListener, Drawer.OnDrawerItemClickListener, SpinnerItemListener {
  internal var mActivityWeakReference: WeakReference<AppCompatActivity>
  internal var mApplicationWeakReference: WeakReference<Application>
  private var mWeakScalpelLayout: WeakReference<ScalpelFrameLayout>? = null
  private val mMenuDrawer: Drawer
  private var mRestartListener: RestartListener? = null
  private var mInputItemListener: InputItemListener? = null

  init {
    mActivityWeakReference = WeakReference(activity)
    mApplicationWeakReference = WeakReference(application)

    mMenuDrawer = DrawerBuilder(activity).withTranslucentStatusBar(true)
        .withDrawerGravity(Gravity.END)
        .build()
        .apply { onDrawerItemClickListener = this@DebugDrawer }
  }

  fun openDrawer() = mMenuDrawer.openDrawer()

  fun withAllFeatures(): DebugDrawer {
    return this.withLeakCanarySwitch(true)
        .withLynksSwitch()
        .withPicassoLogsSwitch()
        .withStethoSwitch()
  }

  fun withLeakCanarySwitch(checked: Boolean = true): DebugDrawer {
    addSwitchDrawerItem("Leak Canary", R.id.drawer_dev_item_leak).withChecked(checked)
    return this
  }

  fun withStethoSwitch(): DebugDrawer {
    addSwitchDrawerItem("Stetho (Chrome debug bridge)", R.id.drawer_dev_item_stetho)
    return this
  }

  fun withLynksSwitch(): DebugDrawer {
    addSwitchDrawerItem("Lynks (logs)", R.id.drawer_dev_item_lynks)
    return this
  }

  fun withPicassoLogsSwitch(): DebugDrawer {
    addSwitchDrawerItem("Picasso Logs", R.id.drawer_dev_item_picasso)
    return this
  }

  fun withScalpelSwitch(layout: ScalpelFrameLayout?): DebugDrawer {
    if (layout != null) {
      mWeakScalpelLayout = WeakReference(layout)
      addSwitchDrawerItem("Scalpel", R.id.drawer_dev_item_scalpel)
    }
    return this
  }

  fun withPhoenixRestartButtons(restartListener: RestartListener): DebugDrawer {
    mRestartListener = restartListener

    mMenuDrawer.addItems(
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
    mMenuDrawer.addItem(SpinnerDrawerItem(id, options, listener, options.indexOf(selectedItem))
        .withMoreListeners(this)
        .withName(name))
    return this
  }

  fun withSpinnerItem(id: Int, name: String, options: Array<String>, selectedItem: Int,
                      listener: SpinnerItemListener): DebugDrawer {
    mMenuDrawer.addItem(SpinnerDrawerItem(id, options, listener, selectedItem)
        .withMoreListeners(this)
        .withName(name))
    return this
  }

  /**
   * Add an item that shows an input dialog.
   */
  fun withInputItem(id: Int, name: String, inputItemListener: InputItemListener): DebugDrawer {
    mInputItemListener = inputItemListener

    mMenuDrawer.addItem(PrimaryDrawerItem()
        .withName(String.format("Enter value for '%s'", name))
        .withTag(id)
        .withIcon(R.drawable.ic_textsms_grey_700_18dp)
        .withIdentifier(R.id.drawer_dev_item_input.toLong()))

    return this
  }

  fun withDivider(): DebugDrawer {
    mMenuDrawer.addItem(DividerDrawerItem())
    return this
  }

  fun withInfoProperties(properties: Map<String, String>): DebugDrawer {
    properties.entries.forEach {
      mMenuDrawer.addItem(SecondaryDrawerItem()
          .withName(it.key)
          .withIcon(R.drawable.ic_info_grey_700_24dp)
          .withDescription(it.value)
          .withIdentifier(R.id.drawer_dev_item_info.toLong()))
    }

    return this
  }

  //<editor-fold desc="OnCheckedChangeListener">
  override fun onCheckedChanged(drawerItem: IDrawerItem<Any, RecyclerView.ViewHolder>,
                                buttonView: CompoundButton, isChecked: Boolean) {
    val activity = mActivityWeakReference.get()
    val application = mApplicationWeakReference.get()
    if (activity == null || application == null) {
      return
    }

    when (drawerItem.identifier) {
      R.id.drawer_dev_item_scalpel.toLong() -> {
        val toggleDrawerItem = drawerItem as SwitchDrawerItem
        mWeakScalpelLayout?.get()?.apply {
          isLayerInteractionEnabled = toggleDrawerItem.isChecked
          setDrawViews(toggleDrawerItem.isChecked)
          chromeShadowColor = android.R.color.black
        }
      }
      R.id.drawer_dev_item_stetho.toLong() -> {
        showToast("Stetho cant be disabled. Check: chrome://inspect")
        Stetho.initializeWithDefaults(activity.applicationContext)
      }
      R.id.drawer_dev_item_leak.toLong() -> {
        showToast("Leak Canary cant be disabled.")
        LeakCanary.install(application)
      }
      R.id.drawer_dev_item_lynks.toLong() -> {
        buttonView.isChecked = false
        activity.startActivity(LynxActivity.getIntent(activity.applicationContext))
      }
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
  //</editor-fold>

  //<editor-fold desc="OnDrawerItemSelectedListener">
  override fun onItemClick(view: View, position: Int,
                           drawerItem: IDrawerItem<Any, RecyclerView.ViewHolder>): Boolean {
    when (drawerItem.identifier) {
      R.id.drawer_dev_item_info.toLong() -> (drawerItem as SecondaryDrawerItem).apply {
        withSetSelected(false)
        showToast(description.text)
      }
      R.id.drawer_dev_item_phoenix_activity.toLong() -> restartActivity()
      R.id.drawer_dev_item_phoenix_app.toLong() -> restartApp()
      R.id.drawer_dev_item_input.toLong() -> showInputDialog(drawerItem as PrimaryDrawerItem)
    }

    return true
  }
  //</editor-fold>

  private fun showToast(dialog: String) = mActivityWeakReference.get()?.let {
    Toast.makeText(it, dialog, Toast.LENGTH_LONG).show()
  }

  private fun addSwitchDrawerItem(text: String, id: Int): SwitchDrawerItem {
    val item = SwitchDrawerItem().withName(text).withIdentifier(id.toLong())
    item.withOnCheckedChangeListener(this)
    mMenuDrawer.addItem(item)
    return item
  }

  fun restartApp() = mApplicationWeakReference.get()?.let {
    mRestartListener?.onAppRestart()
    ProcessPhoenix.triggerRebirth(it)
  }

  fun restartActivity() = mActivityWeakReference.get()?.let {
    mRestartListener?.onActivityRestart()
    ProcessPhoenix.triggerRebirth(it)
  }

  private fun showInputDialog(drawerItem: PrimaryDrawerItem) = mActivityWeakReference.get()?.let {
    val factory = LayoutInflater.from(it)
    val entryView = factory.inflate(R.layout.input_view, null) as EditText

    AlertDialog.Builder(it)
        .setTitle(drawerItem.name.toString())
        .setView(entryView)
        .setPositiveButton("OK") { dialog, which ->
          val inputText = entryView.text.toString()
          mInputItemListener?.onInputOkClick(drawerItem.tag as Int, inputText)
          drawerItem.withDescription(inputText)
          mMenuDrawer.updateItem(drawerItem)
        }.show()
  }

  override fun onSpinnerItemClick(item: SpinnerDrawerItem, itemId: Int, title: CharSequence) {
    item.withDescription(title.toString())
    mMenuDrawer.updateItem(item)
  }
}

