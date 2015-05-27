package ru.aviasales.expandedlistview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.aviasales.expandedlistview.R;
import ru.aviasales.expandedlistview.interfaces.OnSomethingChange;

public class BaseFiltersListViewItem extends LinearLayout {

	protected RelativeLayout layout;
	protected TextView textView;
	protected CheckBox checkBox;
	protected BaseCheckedText checkedText;
	protected ViewStub airlineViewStub;

	public BaseFiltersListViewItem(Context context) {
		this(context, null);
	}

	public BaseFiltersListViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		setUpView(context);
	}

	protected void setUpView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.base_expanded_list_item,
				this, true);

		layout = (RelativeLayout) findViewById(R.id.rlay_base_filter_list_item);
		textView = (TextView) findViewById(R.id.txtv_base_filter_list_item);
		checkBox = (CheckBox) findViewById(R.id.cbox_base_filter_list_item);
		airlineViewStub = (ViewStub) findViewById(R.id.stub_import_airline);
		checkBox.setSaveEnabled(false);
	}

	public void setCheckedText(BaseCheckedText checkedText) {
		this.checkedText = checkedText;
		textView.setText(checkedText.getName());
		checkBox.setChecked(checkedText.isChecked());
	}

	public void setChecked(boolean checked) {
		checkedText.setChecked(checked);
		checkBox.setChecked(checked);
	}

	public void setOnClickListener(final OnSomethingChange listener) {
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				checkBox.setChecked(!checkBox.isChecked());
				checkedText.setChecked(!checkedText.isChecked());
				listener.onChange();
			}
		});
	}

	public boolean isChecked() {
		return checkedText.isChecked();
	}
}