package com.barista_v.debug_artist.drawer

import android.app.Application
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import com.barista_v.debug_artist.DebugActor
import com.barista_v.debug_artist.R
import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.phoenix.RestartListener
import com.barista_v.debug_artist.item.spinner.SpinnerDrawerItem
import com.barista_v.debug_artist.item.spinner.SpinnerItemListener
import com.jakewharton.scalpel.ScalpelFrameLayout
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SwitchDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import java.lang.ref.WeakReference

/**
 * Debug drawer showing some debugging tools and info.

 * To add dynamic actions check "debugDrawer.with*" methods.
 */
class DebugDrawer(application: Application, activity: AppCompatActivity) : OnCheckedChangeListener,
    Drawer.OnDrawerItemClickListener, SpinnerItemListener, DebugDrawerView {

  private var activityWeakReference = WeakReference(activity)

  private var presenter: DebugDrawerPresenter? = null
  private val menuDrawer: Drawer
  private val debugActor = DebugActor(application, activity)

  init {
    menuDrawer = DrawerBuilder(activity)
        .withTranslucentStatusBar(true)
        .withDrawerGravity(Gravity.END)
        .withShowDrawerOnFirstLaunch(true)
        .build()
        .apply {
          onDrawerItemClickListener = this@DebugDrawer

          addItems(PrimaryDrawerItem().withName("Q&A Module")
              .withDescription("Drag from right to left to open")
              .withSelectable(false)
              .withEnabled(false),
              DividerDrawerItem())
        }

    presenter = DebugDrawerPresenter().apply {
      onAttach(this@DebugDrawer, debugActor)
    }
  }

  fun release() = presenter?.onDetach()

  fun openDrawer(): DebugDrawer {
    menuDrawer.openDrawer()
    return this
  }

  fun withLeakCanarySwitch(checked: Boolean = true): DebugDrawer {
    addSwitchDrawerItem(R.string.leak_canary, R.id.drawer_dev_item_leak).withChecked(checked)
    presenter?.onLeakCanarySwitchAdded(checked)
    return this
  }

  fun withStethoSwitch(): DebugDrawer {
    addSwitchDrawerItem(R.string.stetho, R.id.drawer_dev_item_stetho)
    return this
  }

  fun withPicassoLogsSwitch(): DebugDrawer {
    addSwitchDrawerItem(R.string.picasso, R.id.drawer_dev_item_picasso)
    return this
  }

  fun withScalpelSwitch(layout: ScalpelFrameLayout): DebugDrawer {
    debugActor.scalpelFrameLayout = layout
    addSwitchDrawerItem(R.string.scalpel, R.id.drawer_dev_item_scalpel)

    return this
  }

  fun withLynksButton(): DebugDrawer {
    menuDrawer.addItem(PrimaryDrawerItem().withName(R.string.lynks)
        .withIdentifier(R.id.drawer_dev_item_lynks.toLong())
        .withIcon(R.drawable.ic_android_grey_700_18dp))

    return this
  }

  fun withPhoenixRestartButtons(restartListener: RestartListener): DebugDrawer {
    presenter?.restartListener = restartListener

    menuDrawer.addItems(PrimaryDrawerItem().withName(R.string.restart_app)
        .withIdentifier(R.id.drawer_dev_item_phoenix_app.toLong())
        .withIcon(R.drawable.ic_gavel_grey_700_18dp))

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
    presenter?.inputItemListener = inputItemListener
    val text = activityWeakReference.get()?.getString(R.string.enter_value_for, name) ?: ""

    menuDrawer.addItem(PrimaryDrawerItem()
        .withName(text)
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

  private fun showInputDialog(drawerItem: PrimaryDrawerItem) = activityWeakReference.get()?.let {
    val factory = LayoutInflater.from(it)
    val entryView = factory.inflate(R.layout.input_view, null) as EditText

    AlertDialog.Builder(it)
        .setTitle(drawerItem.name.toString())
        .setView(entryView)
        .setPositiveButton(android.R.string.yes) { dialog, which ->
          val inputText = entryView.text.toString()
          presenter?.onTextInputEntered(drawerItem.tag as Int, inputText)

          drawerItem.withDescription(inputText)
          menuDrawer.updateItem(drawerItem)
        }.show()
  }

  private fun addSwitchDrawerItem(@StringRes text: Int, id: Int): SwitchDrawerItem {
    val item = SwitchDrawerItem().withName(text).withIdentifier(id.toLong()).apply {
      withOnCheckedChangeListener(this@DebugDrawer)
    }
    menuDrawer.addItem(item)

    return item
  }

  //<editor-fold desc="Select events">

  override fun onCheckedChanged(drawerItem: IDrawerItem<Any, RecyclerView.ViewHolder>,
                                buttonView: CompoundButton, isChecked: Boolean) {
    when (drawerItem.identifier) {
      R.id.drawer_dev_item_stetho.toLong() -> presenter?.onStethoItemSelected()
      R.id.drawer_dev_item_leak.toLong() -> presenter?.onLeakCanaryItemSelected()
      R.id.drawer_dev_item_picasso.toLong() -> presenter?.onPicassoItemSelected()
      R.id.drawer_dev_item_scalpel.toLong() -> presenter?.onScalpelItemSelected(isChecked)
    }
  }

  override fun onItemClick(view: View, position: Int,
                           drawerItem: IDrawerItem<Any, RecyclerView.ViewHolder>): Boolean {
    when (drawerItem.identifier) {
      R.id.drawer_dev_item_phoenix_app.toLong() -> presenter?.onPhoenixItemSelected()
      R.id.drawer_dev_item_input.toLong() -> showInputDialog(drawerItem as PrimaryDrawerItem)
      R.id.drawer_dev_item_lynks.toLong() -> presenter?.onLynksItemSelected()
    }

    return true
  }

  override fun onSpinnerItemClick(item: SpinnerDrawerItem, itemId: Int, title: CharSequence) {
    item.withDescription(title.toString())
    menuDrawer.updateItem(item)
  }

  //</editor-fold>

}

