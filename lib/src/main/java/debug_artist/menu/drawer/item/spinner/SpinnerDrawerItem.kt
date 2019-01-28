package debug_artist.menu.drawer.item.spinner

import android.support.v7.widget.PopupMenu
import android.view.MenuItem
import android.view.View
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import debug_artist.menu.R

open class SpinnerDrawerItem(id: Int,
                             internal val items: Array<String>,
                             spinnerItemListener: SpinnerItemListener,
                             selectedItemIndex: Int) :
    PrimaryDrawerItem(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

  internal val listeners = mutableListOf(spinnerItemListener)

  init {
    withTag(id)

    if (selectedItemIndex != -1) {
      withDescription(items[selectedItemIndex])
    }

    withIcon(R.drawable.ic_arrow_drop_down_grey_700_24dp)
  }

  override fun bindView(viewHolder: ViewHolder?, payloads: MutableList<Any?>) {
    super.bindView(viewHolder, payloads)
    viewHolder?.itemView?.setOnClickListener(this)
  }

  //<editor-fold desc="OnClickListener">
  override fun onClick(v: View) {
    PopupMenu(v.context, v).apply {
      items.forEach { menu.add(it) }
      setOnMenuItemClickListener(this@SpinnerDrawerItem)
      show()
    }
  }
  //</editor-fold>

  //<editor-fold desc="OnMenuItemClickListener">
  override fun onMenuItemClick(item: MenuItem): Boolean {
    listeners.forEach { it.onSpinnerItemClick(this, tag as Int, item.title) }
    return true
  }
  //</editor-fold>

  fun withMoreListeners(listener: SpinnerItemListener): SpinnerDrawerItem {
    listeners.add(listener)
    return this
  }
}
