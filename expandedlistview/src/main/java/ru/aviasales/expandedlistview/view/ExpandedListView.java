package ru.aviasales.expandedlistview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.aviasales.expandedlistview.R;
import ru.aviasales.expandedlistview.adapter.BaseExpandedListViewAdapter;

public class ExpandedListView extends LinearLayout {

	public static final int DEFAULT_ARROW_OPENED = R.drawable.ic_filters_up;
	public static final int DEFAULT_ARROW_CLOSED = R.drawable.ic_filters_down;
	public static final int DEFAULT_TITLE_TEXT_OPENED_COLOR = R.color.gray_A7A7A7;
	public static final int DEFAULT_TITLE_TEXT_CLOSED_COLOR = R.color.gray_A7A7A7;

	private View titleView;
	private View selectAllView;
	private List<View> viewList = new ArrayList<View>();

	private ViewGroup listParent;

	private TextView titleText;
	private ImageView titleIcon;

	private boolean isOpened = false;

	private ExpandedListViewListener mListener;
	private OnItemClickListener onItemClickListener;

	private BaseExpandedListViewAdapter mAdapter;

	interface ExpandedListViewListener {
		void onOpen();

		void onClose();
	}

	public interface OnItemClickListener {
		void onItemClick();
	}

	public ExpandedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
	}

	private void generateLayout() {
		addTitle();
		addListParent();
		if (viewList.size() != 0) {
			addSelectAllView();
			createItems();
		}
		setupTitle();
	}

	private void addTitle() {
		if (titleView == null) {
			titleView = mAdapter.getTitleView(titleView, this);
			titleText = (TextView) titleView.findViewById(R.id.txtv_airports_view_list_title);
			titleIcon = (ImageView) titleView.findViewById(R.id.img_airports_view_list_isopened);
			addView(titleView);

			titleView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (isOpened) {
						close();
					} else {
						open();
					}
					setupTitle();
				}
			});
		}
	}

	private void addSelectAllView() {
		if (selectAllView == null) {
			selectAllView = mAdapter.getSelectAllView(selectAllView, listParent);
			listParent.addView(selectAllView);
		} else {
			mAdapter.getSelectAllView(selectAllView, listParent);
		}
	}

	private void addListParent() {
		if (listParent == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listParent = (ViewGroup) inflater.inflate(R.layout.expanded_listview_content, this, false);
			addView(listParent);
		}
	}

	public void setAdapter(BaseExpandedListViewAdapter adapter) {
		this.mAdapter = adapter;
		generateLayout();
		mAdapter.setListener(new BaseExpandedListViewAdapter.AdapterCallback() {
			@Override
			public void onSelectAllPressed(Object state) {
				for (View view : viewList) {
					((BaseFiltersListViewItem) view).setChecked((Boolean) state);
				}
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick();
				}
			}

			@Override
			public void onViewPressed() {
				((SelectAllView) selectAllView).setChecked(areAllItemsChecked());
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick();
				}
			}
		});
	}

	public void notifyDataChanged() {
		generateLayout();
		if (selectAllView != null) {
			((SelectAllView) selectAllView).setChecked(mAdapter.areAllItemsChecked());
		}
	}

	private void createItems() {
		if (viewList.size() == 0) {
			for (int i = 0; i < mAdapter.getItemsCount(); i++) {
				View view = mAdapter.getItemView(viewList.size() <= i ? null : viewList.get(i), this, i);
				if (!viewList.contains(view)) {
					viewList.add(view); //TODO: Проверить на утечку памяти из-за множественного добавления
				}
				if (mAdapter.hasSeparators() && mAdapter.getSeparatorIndexes().contains(i)) {
					listParent.addView(mAdapter.getSeparatorView(i));
				}
				listParent.addView(view);
			}
		} else {
			for (int i = 0; i < mAdapter.getItemsCount(); i++) {
				mAdapter.getItemView(viewList.size() <= i ? null : viewList.get(i), this, i);
			}
		}
	}

	public void open() {
		if (mListener != null) {
			mListener.onOpen();
		}
		isOpened = true;
		listParent.setVisibility(VISIBLE);
	}

	public void close() {
		if (mListener != null) {
			mListener.onClose();
		}
		isOpened = false;
		listParent.setVisibility(GONE);
	}

	public void setmListener(ExpandedListViewListener mListener) {
		this.mListener = mListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public Boolean areAllItemsChecked() {
		for (int i = 0; i < mAdapter.getItemsCount(); i++) {
			if (!mAdapter.isItemChecked(i)) return false;
		}
		return true;
	}

	public void setupTitle() {
		if (isOpened) {
			if (viewList.size() == 0 && mAdapter.getItemsCount() != 0) {
				addSelectAllView();
				createItems();
			}
			titleIcon.setImageResource(DEFAULT_ARROW_OPENED);
			titleText.setTextColor(getResources().getColor(DEFAULT_TITLE_TEXT_OPENED_COLOR));
		} else {
			titleIcon.setImageResource(DEFAULT_ARROW_CLOSED);
			titleText.setTextColor(getResources().getColor(DEFAULT_TITLE_TEXT_CLOSED_COLOR));
		}
	}
}
