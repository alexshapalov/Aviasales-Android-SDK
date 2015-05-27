package ru.aviasales.template.ui.view;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import ru.aviasales.template.R;
import ru.aviasales.template.filters.BaseNumericFilter;
import ru.aviasales.template.utils.DateUtils;
import ru.aviasales.template.utils.Defined;

public class TwoSideSeekBarFilterView extends LinearLayout {

	public interface OnRangeSeekBarChangeListener {
		void onChange(int min, int max);
	}

	private Context context;
	private TextView currentMaxPrice;
	private TextView titleTextView;
	private LinearLayout filter;
	private OnRangeSeekBarChangeListener listener;
	private RangeSeekBar priceSeekBar;
	private FormatterInterface formatter;

	private int tCurrentMin;
	private int tCurrentMax;
	private int tMin;
	private int tMax;

	public static TwoSideSeekBarFilterView newStopOverTimeFilter(Context context, String title, BaseNumericFilter filter) {
		TwoSideSeekBarFilterView twoSideSeekBarFilterView = new TwoSideSeekBarFilterView(context, null);

		twoSideSeekBarFilterView.init(title, filter, new StopOverTimeFormatter());
		return twoSideSeekBarFilterView;
	}

	public static TwoSideSeekBarFilterView newTakeOfTimeFilter(Context context, String title, BaseNumericFilter filter) {
		TwoSideSeekBarFilterView twoSideSeekBarFilterView = new TwoSideSeekBarFilterView(context, null);

		twoSideSeekBarFilterView.init(title, filter, new TakeOffTimeFormatter());
		return twoSideSeekBarFilterView;
	}

	private void init(String title, BaseNumericFilter filter, FormatterInterface formatter) {

		setFormatter(formatter);
		setTitle(title);
		setMin(filter.getMinValue());
		setMax(filter.getMaxValue());
		setCurrentMin(filter.getCurrentMinValue());
		setCurrentMax(filter.getCurrentMaxValue());

		priceSeekBar = new RangeSeekBar(tMin, tMax, context);

		priceSeekBar.setSelectedMaxValue(tCurrentMax);
		priceSeekBar.setSelectedMinValue(tCurrentMin);
		updateValues(tCurrentMin, tCurrentMax);

		priceSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				if (listener != null) {
					listener.onChange(minValue, maxValue);
				}
			}

			@Override
			public void onRangeSeekBarTracking(RangeSeekBar<?> tRangeSeekBar, Integer selectedMinValue, Integer selectedMaxValue) {
				updateValues(selectedMinValue, selectedMaxValue);
			}
		});

		this.filter.addView(priceSeekBar);
	}

	public TwoSideSeekBarFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context)
				.inflate(R.layout.base_range_seekbar_filter_view, this, true);

		filter = (LinearLayout) findViewById(R.id.llay_base_range_seekbar_filter_view_filter);

		currentMaxPrice = (TextView) findViewById(R.id.txtv_base_range_seekbar_filter_view_max);
		titleTextView = (TextView) findViewById(R.id.txtv_base_range_seekbar_fitler_vew_title);

	}

	public void clear() {
		priceSeekBar.setSelectedMaxValue(tMax);
		priceSeekBar.setSelectedMinValue(tMin);
		updateValues(tMin, tMax);
	}

	public void setValuesManually(int min, int max) {
		if (max > tCurrentMax) {
			tCurrentMax = max;
			tCurrentMin = min;
			priceSeekBar.setSelectedMaxValue(tCurrentMax);
			priceSeekBar.setSelectedMinValue(tCurrentMin);
		} else {
			tCurrentMax = max;
			tCurrentMin = min;
			priceSeekBar.setSelectedMinValue(tCurrentMin);
			priceSeekBar.setSelectedMaxValue(tCurrentMax);
		}
		updateValues(min, max);
	}

	private void updateValues(int min, int max) {
		currentMaxPrice.setText(formatter.format(max, min, context));
	}


	public void setTitle(String title) {
		this.titleTextView.setText(title);
	}

	public void setMin(int min) {
		this.tMin = min;
	}

	public void setMax(int max) {
		this.tMax = max;
	}

	public void setCurrentMin(int currentMin) {
		this.tCurrentMin = currentMin;
	}

	public void setCurrentMax(int currentMax) {
		this.tCurrentMax = currentMax;
	}

	private static SimpleDateFormat getTimeFormat(Context context) {
		SimpleDateFormat timeFormat;
		if (DateFormat.is24HourFormat(context)) {
			timeFormat = new SimpleDateFormat(Defined.FILTERS_TIME_FORMAT);
		} else {
			timeFormat = new SimpleDateFormat(Defined.AM_PM_FILTERS_TIME_FORMAT);
			timeFormat.setDateFormatSymbols(DateUtils.getDateFormatSymbols());
		}
		TimeZone utc = TimeZone.getTimeZone(Defined.UTC_TIMEZONE);
		timeFormat.setTimeZone(utc);
		return timeFormat;
	}

	public void setFormatter(FormatterInterface formatter) {
		this.formatter = formatter;
	}

	public TwoSideSeekBarFilterView setListener(OnRangeSeekBarChangeListener listener) {
		this.listener = listener;
		return this;
	}

	public interface FormatterInterface {
		String format(int max, int min, Context context);
	}

	public static class TakeOffTimeFormatter implements FormatterInterface {

		@Override
		public String format(int max, int min, Context context) {
			SimpleDateFormat timeFormat = getTimeFormat(context);
			String minValue = timeFormat.format(DateUtils.getAmPmTime(min / 60, min % 60));
			String maxValue = timeFormat.format(DateUtils.getAmPmTime(max / 60, max % 60));

			return context.getString(R.string.range_with) + " " + minValue + " "
					+ context.getString(R.string.range_to) + " " + maxValue;
		}
	}

	public static class StopOverTimeFormatter implements FormatterInterface {

		@Override
		public String format(int max, int min, Context context) {
			String minValue = String.format("%02d", min / 60) + ":" + String.format("%02d", min % 60);
			String maxValue = String.format("%02d", max / 60) + ":" + String.format("%02d", max % 60);

			return context.getString(R.string.range_from) + " " + minValue + " "
					+ context.getString(R.string.range_to) + " " + maxValue;
		}
	}



}