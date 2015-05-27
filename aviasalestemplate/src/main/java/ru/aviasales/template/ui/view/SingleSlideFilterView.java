package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Map;

import ru.aviasales.template.R;
import ru.aviasales.template.filters.BaseNumericFilter;
import ru.aviasales.template.utils.CurrencyUtils;
import ru.aviasales.template.utils.StringUtils;

public class SingleSlideFilterView extends LinearLayout {


	public interface OnRangeChangeListener {
		void onChange(int max);
	}

	private Context context;
	private TextView currentMaxPrice;
	private TextView title;
	private FormatterInterface formatter;
	private SeekBar seekBar;
	private int min;
	private int max;
	private OnRangeChangeListener listener;

	public SingleSlideFilterView setListener(OnRangeChangeListener listener) {
		this.listener = listener;
		return this;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setFormatter(FormatterInterface formatter) {
		this.formatter = formatter;
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	public static SingleSlideFilterView newPriceFilterView(Context context, String titleText, BaseNumericFilter filter) {
		FormatterInterface formatter = new PriceFilterFormatter();
		SingleSlideFilterView filterView = new SingleSlideFilterView(context, null);
		filterView.init(titleText, filter, formatter);
		return filterView;
	}

	public static SingleSlideFilterView newHoursMinutesFilterView(Context context, String titleText, BaseNumericFilter filter) {
		FormatterInterface formatter = new HoursMinutesFormatter();
		SingleSlideFilterView filterView = new SingleSlideFilterView(context, null);
		filterView.init(titleText, filter, formatter);
		return filterView;
	}

	private void init(String titleText, BaseNumericFilter filter, FormatterInterface formatter) {
		setTitle(titleText);
		setMax(filter.getMaxValue());
		setMin(filter.getMinValue());
		setFormatter(formatter);
		setCurrentMaxPrice(filter.getCurrentMaxValue());
	}

	private void setCurrentMaxPrice(int currentMaxValue) {
		currentMaxPrice.setText(String.valueOf(currentMaxValue));

		setPrigressBarMaxProgress(max - min);
		initProgressBar(currentMaxValue);

		updateValues(currentMaxValue - min);
	}


	public SingleSlideFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context)
				.inflate(R.layout.price_filter_view, this, true);
		this.context = context;
		currentMaxPrice = (TextView) findViewById(R.id.txtv_price_filter_view_max);

		seekBar = (SeekBar) findViewById(R.id.sbar_price_filter_view);
		seekBar.setSaveEnabled(false); // switch off autorotate
		title = (TextView) findViewById(R.id.txtv_price_filter_view_min);

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				updateValues(i);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				onChange(seekBar.getProgress());
			}
		});
	}

	private CharSequence formatMinMax() {
		return formatter.format(max, min, context);
	}

	public void clear() {
		initProgressBar(max);
	}

	public void initProgressBar(int current) {
		seekBar.setProgress(current - min);
		updateValues(current - min);
	}

	public void setPrigressBarMaxProgress(int maxProgress) {
		seekBar.setMax(maxProgress);
	}

	private void onChange(int result) {
		listener.onChange(result + min);
	}

	public void refresh() {
		updateValues(seekBar.getProgress());
	}

	private void updateValues(int max) {
		CharSequence maxValue = null;
		maxValue = formatter.format(max, min, context);

		currentMaxPrice.setText(maxValue);
	}

	public void setTitle(TextView title) {
		this.title = title;
	}

	public interface FormatterInterface {
		CharSequence format(int max, int min, Context context);
	}

	public static class PriceFilterFormatter implements FormatterInterface {
		@Override
		public CharSequence format(int max, int min, Context context) {
			CharSequence format = StringUtils.getSpannablePriceString(context.getResources().getString(R.string.filters_price)+ " " + getPrice(max + min, context),
					CurrencyUtils.getAppCurrency(context));
			return format;
		}

		private String getPrice(int price, Context context) {
			String appCurCode = CurrencyUtils.getAppCurrency(context);
			Map<String, Double> currencies = getCurrencyRates();
			return StringUtils.formatPriceInAppCurrency(price, appCurCode, currencies);
		}

		private Map<String, Double> getCurrencyRates() {
			return CurrencyUtils.getCurrencyRates();
		}
	}

	public static class HoursMinutesFormatter implements FormatterInterface {
		@Override
		public CharSequence format(int max, int min, Context context) {
			return String.format(context.getResources().getString(R.string.filters_hours_and_minutes), (max + min) / 60, (max + min) % 60);
		}
	}

}