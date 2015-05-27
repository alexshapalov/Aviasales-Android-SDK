package ru.aviasales.template.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.aviasales.core.http.utils.CoreDateUtils;
import ru.aviasales.template.R;
import ru.aviasales.template.utils.Defined;
import ru.aviasales.template.utils.Utils;

public class SearchFormDateButton extends RelativeLayout {

	public static final int TYPE_DEPART = 0;
	public static final int TYPE_RETURN = 1;

	private TextView tvDate;
	private TextView tvWeekDay;
	private int type;

	public SearchFormDateButton(Context context) {
		super(context);
	}

	public SearchFormDateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
		setUpViews(context);
	}

	public SearchFormDateButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		parseAttributes(context, attrs);
		setUpViews(context);
	}

	private void setUpViews(Context context) {
		LayoutInflater.from(context).inflate(R.layout.search_form_date_btn, this, true);
		tvDate = (TextView) findViewById(R.id.tv_departure_date);
		tvWeekDay = (TextView) findViewById(R.id.tv_week_day);

		setUpDefaultValues();
	}

	private void setUpDefaultValues() {
		switch (type) {
			case TYPE_DEPART:
				setDefaultDepartData();
				break;
			case TYPE_RETURN:
				setDefaultReturnData();
				break;
		}
	}

	private void setDefaultReturnData() {
		tvDate.setText(R.string.search_form_return_date_default);
	}

	private void setDefaultDepartData() {
		tvDate.setText(R.string.search_form_depart_date_default);
	}

	private void parseAttributes(Context context, AttributeSet attrs) {
		TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.SearchFormDateButton);
		type = values.getInt(R.styleable.SearchFormDateButton_dateType, TYPE_DEPART);
		values.recycle();
	}

	public void setData(String dateInServerFormat) {
		if (dateInServerFormat == null) {
			setUpDefaultValues();
			return;
		}

		Date date = CoreDateUtils.parseDateString(dateInServerFormat, Defined.SEARCH_SERVER_DATE_FORMAT);

		SimpleDateFormat dateFormat = new SimpleDateFormat(Defined.SEARCH_FORM_DATE_FORMAT);
		tvDate.setText(Utils.capitalizeFirstLetter(dateFormat.format(date)));

		SimpleDateFormat weekDayFormat = new SimpleDateFormat(Defined.SEARCH_FORM_WEEK_DAY_FORMAT);
		tvWeekDay.setText(Utils.capitalizeFirstLetter(weekDayFormat.format(date)));
	}

	public int getType() {
		return type;
	}

	@Override
	public void setEnabled(boolean enabled) {
		tvWeekDay.setEnabled(enabled);
		tvDate.setEnabled(enabled);
	}
}
