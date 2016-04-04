package com.barista_v.debugging.item.spinner;

import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.barista_v.debugging.R;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import java.util.ArrayList;
import java.util.List;

public class SpinnerDrawerItem extends PrimaryDrawerItem implements View.OnClickListener,
    PopupMenu.OnMenuItemClickListener {

  final String[] mItems;
  final List<SpinnerItemListener> mListeners;

  public SpinnerDrawerItem(int id, String[] items, final SpinnerItemListener spinnerItemListener,
      String selectedItem) {
    this(id, items, spinnerItemListener, java.util.Arrays.asList(items).indexOf(selectedItem));
  }

  public SpinnerDrawerItem(int id, String[] items, final SpinnerItemListener spinnerItemListener,
      int selectedItemIndex) {
    mItems = items;
    mListeners = new ArrayList<SpinnerItemListener>() {{
      add(spinnerItemListener);
    }};

    withTag(id);
    withDescription(mItems[selectedItemIndex]);
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
    for (int i = 0, length = mListeners.size(); i < length; i++) {
      mListeners.get(i).onSpinnerItemClick(this, (int) getTag(), item.getTitle());
    }
    return true;
  }
  //</editor-fold>

  public SpinnerDrawerItem withMoreListeners(SpinnerItemListener listener) {
    mListeners.add(listener);
    return this;
  }
}
