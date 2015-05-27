package ru.aviasales.expandedlistview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.aviasales.expandedlistview.R;
import ru.aviasales.expandedlistview.listener.OnChangeState;

public class SelectAllView extends BaseFiltersListViewItem {

	private int id;
	private RelativeLayout layout;
	private TextView textView;
	private CheckBox checkBox;

	public SelectAllView(Context context) {
		super(context);
	}

	@Override
	protected void setUpView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.select_all_view,
				this, true);

		layout = (RelativeLayout) findViewById(R.id.rlSelectAllItemView);
		textView = (TextView) findViewById(R.id.tvSelectAllItem);
		checkBox = (CheckBox) findViewById(R.id.cbSelectAllItem);

		checkBox.setSaveEnabled(false);
	}

	public void setText(String text) {
//		this.text = text;
//		this.selectAll = selectAll;
		textView.setText(text);
	}

	public void setChecked(boolean checked) {
		checkBox.setChecked(checked);
		checkedText.setChecked(checked);
	}

	public void setCheckedText(BaseCheckedText baseCheckedText) {
		checkedText = baseCheckedText;
		checkBox.setChecked(baseCheckedText.isChecked());
		checkedText.setChecked(baseCheckedText.isChecked());
	}

	public void setOnClickListener(final OnChangeState listener) {
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				checkBox.setChecked(!checkBox.isChecked());
				listener.onChange(checkBox.isChecked());
			}
		});
	}
}