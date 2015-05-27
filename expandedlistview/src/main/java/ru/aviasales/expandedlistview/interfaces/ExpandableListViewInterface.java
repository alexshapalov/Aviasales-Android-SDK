package ru.aviasales.expandedlistview.interfaces;

import android.view.View;
import android.view.ViewGroup;

public interface ExpandableListViewInterface {

	View getItemView(View view, ViewGroup parent, int position);

	View getSelectAllView(View view, ViewGroup parent);

	Object getItem(int item);

	Boolean isItemChecked(int position);

	int getItemsCount();
}
