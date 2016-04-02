package com.barista_v.debugging.item.spinner;

import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.barista_v.debugging.R;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class SpinnerDrawerItem extends PrimaryDrawerItem implements View.OnClickListener,
    PopupMenu.OnMenuItemClickListener {

  final String[] mItems;
  final SpinnerItemListener mSpinnerItemListener;

  public SpinnerDrawerItem(int id, String[] items, SpinnerItemListener spinnerItemListener) {
    mItems = items;
    mSpinnerItemListener = spinnerItemListener;

    withTag(id);
    withDescription(mItems[0]);
    withIcon(R.drawable.ic_arrow_drop_down_grey_700_24dp);
  }

  @Override
  public void bindView(ViewHolder viewHolder) {
    super.bindView(viewHolder);
    viewHolder.itemView.setOnClickListener(this);
  }

  //<editor-fold desc="OnClickListener">
  @Override
  public void onClick(View v) {
    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
    Menu menu = popupMenu.getMenu();

    for (int i = 0, length = mItems.length; i < length; i++) {
      menu.add(mItems[i]);
    }

    popupMenu.setOnMenuItemClickListener(this);
    popupMenu.show();
  }
  //</editor-fold>

  //<editor-fold desc="OnMenuItemClickListener">
  @Override
  public boolean onMenuItemClick(MenuItem item) {
    mSpinnerItemListener.onItemClick((int) getTag(), item.getTitle());
    return true;
  }
  //</editor-fold>
}
