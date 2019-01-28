package debug_artist.menu.drawer

import android.app.Activity
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SwitchDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import debug_artist.menu.R
import debug_artist.menu.drawer.item.*
import debug_artist.menu.drawer.item.input.InputItemListener
import debug_artist.menu.drawer.item.spinner.SpinnerDrawerItem
import debug_artist.menu.drawer.item.spinner.SpinnerItemListener
import debug_artist.menu.utils.device.AndroidDevice
import debug_artist.menu.utils.device.AndroidDevice.Companion.getProjectProperties

/**
 * Debug drawer showing some debugging tools and info.

 * To add dynamic actions check "debugDrawer.with*" methods.
 */
class DebugDrawer @JvmOverloads constructor(val activity: Activity,
                                            showDrawerOnFirstLaunch: Boolean = false)
  : Drawer.OnDrawerItemClickListener, SpinnerItemListener, DebugDrawerView {

  private val presenter = DebugDrawerPresenter()
  private val menuDrawer = DrawerBuilder(activity)
      .withTranslucentStatusBar(true)
      .withDrawerGravity(Gravity.END)
      .withShowDrawerOnFirstLaunch(showDrawerOnFirstLaunch)
      .build()
      .apply {
        onDrawerItemClickListener = this@DebugDrawer

        addItems(
            PrimaryDrawerItem().withName("Debug Artist - Q&A Module")
                .withDescription("Drag from right to left to open")
                .withSelectable(false)
                .withEnabled(false),
            DividerDrawerItem())
      }

  init {
    presenter.attach(this@DebugDrawer, AndroidDevice(activity))
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

  fun withInputItem(id: Int, name: String, inputItemListener: InputItemListener) =
      withMenuItem(InputMenuItem(id, name, inputItemListener))

  fun withSpinnerItem(id: Int,
                      name: String,
                      options: Array<String>,
                      selectedItem: Int,
                      listener: SpinnerItemListener) =
      withMenuItem(SpinnerMenuItem(id, name, options, selectedItem, listener))

  fun withInfoProperties() = withMenuItem(LabelMenuItem(getProjectProperties(activity)))

  //</editor-fold>

  //<editor-fold desc="DebugDrawerView">

  override fun addDividerItem() {
    menuDrawer.addItem(DividerDrawerItem())
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
    val text = activity.getString(R.string.enter_value_for, item.name) ?: ""

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
    Toast.makeText(activity, message, LENGTH_LONG).show()
  }

  override fun showSuccessToast() {
    Toast.makeText(activity, "Success", LENGTH_SHORT).show()
  }

  //</editor-fold>

  private fun showInputDialog(drawerItem: PrimaryDrawerItem) {
    val factory = LayoutInflater.from(activity)
    val entryView = factory.inflate(R.layout.input_view, null) as EditText

    AlertDialog.Builder(activity)
        .setTitle(drawerItem.name.toString())
        .setView(entryView)
        .setPositiveButton(android.R.string.yes) { _, _ ->
          val inputText = entryView.text.toString()
          presenter.onTextInputEntered(drawerItem.tag as Int, inputText)

          drawerItem.withDescription(inputText)
          menuDrawer.updateItem(drawerItem)
        }.show()
  }

  //<editor-fold desc="Select events">

  override fun onItemClick(view: View, position: Int,
                           drawerItem: IDrawerItem<Any, RecyclerView.ViewHolder>): Boolean {
    when (drawerItem.identifier) {
      R.id.drawer_dev_item_input.toLong() -> showInputDialog(drawerItem as PrimaryDrawerItem)
    }

    return true
  }

  override fun onSpinnerItemClick(item: SpinnerDrawerItem, itemId: Int, title: CharSequence) {
    item.withDescription(title.toString())
    menuDrawer.updateItem(item)
  }

  //</editor-fold>

}

