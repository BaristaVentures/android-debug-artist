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
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.barista_v.debug_artist.DebugActor
import com.barista_v.debug_artist.R
import com.barista_v.debug_artist.item.*
import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.issue_reporter.AndroidShakeDetector
import com.barista_v.debug_artist.item.phoenix.RestartListener
import com.barista_v.debug_artist.item.spinner.SpinnerDrawerItem
import com.barista_v.debug_artist.item.spinner.SpinnerItemListener
import com.barista_v.debug_artist.repositories.BugReportRepository
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
class DebugDrawer @JvmOverloads constructor(application: Application,
                                            activity: AppCompatActivity,
                                            showDrawerOnFirstLaunch: Boolean = false)
  : OnCheckedChangeListener, Drawer.OnDrawerItemClickListener, SpinnerItemListener, DebugDrawerView {

  private var activityWeakReference = WeakReference(activity)
  private var presenter = DebugDrawerPresenter()
  private val debugActor = DebugActor(application, activity)
  private val menuDrawer = DrawerBuilder(activity)
      .withTranslucentStatusBar(true)
      .withDrawerGravity(Gravity.END)
      .withShowDrawerOnFirstLaunch(showDrawerOnFirstLaunch)
      .build()
      .apply {
        onDrawerItemClickListener = this@DebugDrawer

        addItems(PrimaryDrawerItem().withName("Debug Artist - Q&A Module")
            .withDescription("Drag from right to left to open")
            .withSelectable(false)
            .withEnabled(false),
            DividerDrawerItem())
      }

  init {
    presenter.onAttach(this@DebugDrawer, debugActor, AndroidShakeDetector(activity))
  }

  fun release() = presenter.deAttach()

  fun openDrawer(): DebugDrawer {
    menuDrawer.openDrawer()
    return this
  }

  fun withMenuItem(item: MenuItem): DebugDrawer {
    presenter.onItemAdded(item)
    return this
  }

  //<editor-fold desc="Builder Helpers">
  fun withDivider() = withMenuItem(DividerMenuItem())

  @JvmOverloads
  fun withStethoSwitch(checked: Boolean = false) = withMenuItem(StethoSwitchMenuItem(checked))

  @JvmOverloads
  fun withLeakCanarySwitch(checked: Boolean = false) = withMenuItem(LeakCanarySwitchMenuItem(checked))

  @JvmOverloads
  fun withScalpelSwitch(layout: ScalpelFrameLayout, checked: Boolean = false) =
      withMenuItem(ScalpelSwitchMenuItem(layout, checked))

  @JvmOverloads
  fun withPicassoLogsSwitch(checked: Boolean = false) = withMenuItem(PicassoLogsSwitchMenuItem(checked))

  @JvmOverloads
  fun withReportBugReportSwitch(checked: Boolean = false, bugReportRepository: BugReportRepository) =
      withMenuItem(ReportBugSwitchMenuItem(checked, bugReportRepository))

  fun withLynksButton() = withMenuItem(LynksButtonMenuItem())

  fun withPhoenixRestartButton(listener: RestartListener) = withMenuItem(PhoenixButtonMenuItem(listener))

  fun withInputItem(id: Int, name: String, inputItemListener: InputItemListener) =
      withMenuItem(InputMenuItem(id, name, inputItemListener))

  fun withSpinnerItem(id: Int,
                      name: String,
                      options: Array<String>,
                      selectedItem: Int,
                      listener: SpinnerItemListener) =
      withMenuItem(SpinnerMenuItem(id, name, options, selectedItem, listener))

  fun withInfoProperties(properties: Map<String, String>) = withMenuItem(LabelMenuItem(properties))

  //</editor-fold>

  //<editor-fold desc="DebugDrawerView">
  override fun addStethoSwitch(checked: Boolean) {
    addSwitchDrawerItem(R.string.stetho, R.id.drawer_dev_item_stetho).withChecked(checked)
  }

  override fun addDividerItem() {
    menuDrawer.addItem(DividerDrawerItem())
  }

  override fun addLeakCanarySwitch(checked: Boolean) {
    addSwitchDrawerItem(R.string.leak_canary, R.id.drawer_dev_item_leak).withChecked(checked)
  }

  override fun addPicassoLogsSwitch(checked: Boolean) {
    addSwitchDrawerItem(R.string.picasso, R.id.drawer_dev_item_picasso).withChecked(checked)
  }

  override fun addScalpelSwitch(checked: Boolean) {
    addSwitchDrawerItem(R.string.scalpel, R.id.drawer_dev_item_scalpel).withChecked(checked)
  }

  override fun addBugReportSwitch(checked: Boolean) {
    addSwitchDrawerItem(R.string.report_bug, R.id.drawer_dev_item_bug_report).withChecked(checked)
  }

  override fun addLynksButton() {
    menuDrawer.addItem(PrimaryDrawerItem().withName(R.string.lynks)
        .withIdentifier(R.id.drawer_dev_item_lynks.toLong())
        .withIcon(R.drawable.ic_android_grey_700_18dp))
  }

  override fun addPhoenixButton() {
    menuDrawer.addItems(PrimaryDrawerItem().withName(R.string.restart_app)
        .withIdentifier(R.id.drawer_dev_item_phoenix_app.toLong())
        .withIcon(R.drawable.ic_gavel_grey_700_18dp))
  }

  override fun addSpinnerItem(it: SpinnerMenuItem) {
    menuDrawer.addItem(SpinnerDrawerItem(it.id, it.options, it.listener, it.selectedItem)
        .withMoreListeners(this)
        .withName(it.name))
  }

  override fun addLabelItem(id: String, text: String) {
    menuDrawer.addItem(SecondaryDrawerItem()
        .withName(id)
        .withIcon(R.drawable.ic_info_grey_700_24dp)
        .withDescription(text)
        .withIdentifier(R.id.drawer_dev_item_info.toLong()))
  }

  override fun addInputItem(item: InputMenuItem) {
    val text = activityWeakReference.get()?.getString(R.string.enter_value_for, item.name) ?: ""

    menuDrawer.addItem(PrimaryDrawerItem()
        .withName(text)
        .withTag(item.id)
        .withIcon(R.drawable.ic_textsms_grey_700_18dp)
        .withIdentifier(R.id.drawer_dev_item_input.toLong()))
  }

  override fun showProgressDialog() {

  }

  override fun dismissProgressDialog() {
  }

  override fun showErrorDialog(message: String) {
    activityWeakReference.get()?.let {
      Toast.makeText(it, message, LENGTH_LONG).show()
    }
  }

  override fun showSuccessToast() {
    activityWeakReference.get()?.let {
      Toast.makeText(it, "Success", LENGTH_SHORT).show()
    }
  }

  //</editor-fold>

  private fun showInputDialog(drawerItem: PrimaryDrawerItem) = activityWeakReference.get()?.let {
    val factory = LayoutInflater.from(it)
    val entryView = factory.inflate(R.layout.input_view, null) as EditText

    AlertDialog.Builder(it)
        .setTitle(drawerItem.name.toString())
        .setView(entryView)
        .setPositiveButton(android.R.string.yes) { dialog, which ->
          val inputText = entryView.text.toString()
          presenter.onTextInputEntered(drawerItem.tag as Int, inputText)

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
      R.id.drawer_dev_item_stetho.toLong() -> presenter.onStethoItemSelected()
      R.id.drawer_dev_item_leak.toLong() -> presenter.onLeakCanaryItemSelected()
      R.id.drawer_dev_item_picasso.toLong() -> presenter.onPicassoItemSelected()
      R.id.drawer_dev_item_scalpel.toLong() -> presenter.onScalpelItemSelected(isChecked)
      R.id.drawer_dev_item_bug_report.toLong() -> presenter.onBugReporterItemSelected(isChecked)
    }
  }

  override fun onItemClick(view: View, position: Int,
                           drawerItem: IDrawerItem<Any, RecyclerView.ViewHolder>): Boolean {
    when (drawerItem.identifier) {
      R.id.drawer_dev_item_phoenix_app.toLong() -> presenter.onPhoenixItemSelected()
      R.id.drawer_dev_item_input.toLong() -> showInputDialog(drawerItem as PrimaryDrawerItem)
      R.id.drawer_dev_item_lynks.toLong() -> presenter.onLynksItemSelected()
    }

    return true
  }

  override fun onSpinnerItemClick(item: SpinnerDrawerItem, itemId: Int, title: CharSequence) {
    item.withDescription(title.toString())
    menuDrawer.updateItem(item)
  }

  //</editor-fold>

}

