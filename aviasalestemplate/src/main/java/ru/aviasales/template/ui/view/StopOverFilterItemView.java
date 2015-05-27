package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import ru.aviasales.template.R;
import ru.aviasales.template.utils.CurrencyUtils;
import ru.aviasales.template.utils.StringUtils;

public class StopOverFilterItemView extends RelativeLayout {
	private TextView tvText;
	private TextView tvPrice;
	private CheckBox checkBox;

	private OnStateChangedListener listener;

	public boolean isChecked() {
		return checkBox.isChecked();
	}

	public interface OnStateChangedListener {
		void onChanged(boolean newState);
	}

	public StopOverFilterItemView(Context context) {
		super(context);
	}

	public StopOverFilterItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StopOverFilterItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tvText = (TextView) findViewById(R.id.tv_stop_over_filter_item_text);
		tvPrice = (TextView) findViewById(R.id.tv_stop_over_filter_item_price);
		checkBox = (CheckBox) findViewById(R.id.cb_stop_over_filter_view);

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isEnabled()) {

					checkBox.setChecked(!checkBox.isChecked());
					if (listener != null) {
						listener.onChanged(checkBox.isChecked());
					}
				}
			}
		});
	}

	public void setMinPrice(int minPrice) {
		String appCurCode = CurrencyUtils.getAppCurrency(getContext());
		Map<String, Double> currencies = CurrencyUtils.getCurrencyRates();
		String priceString = getResources().getString(R.string.range_from) + " " +
				StringUtils.formatPriceInAppCurrency(minPrice, appCurCode, currencies);

		if (minPrice != Integer.MAX_VALUE) {
			tvPrice.setText(StringUtils.getSpannablePriceString(priceString, appCurCode));
		} else {
			disableView();
		}
	}

	public void disableView() {
		hidePrice();
		setEnabled(false);
		checkBox.setEnabled(false);
		tvText.setEnabled(false);
	}

	public void enableView() {
		showPrice();
		setEnabled(true);
		checkBox.setEnabled(true);
		tvText.setEnabled(true);
	}

	public void setListener(OnStateChangedListener listener) {
		this.listener = listener;
	}

	public void reset() {
		if (isEnabled()) {
			checkBox.setChecked(true);
		}
	}

	private void hidePrice() {
		tvPrice.setVisibility(View.GONE);
	}

	private void showPrice() {
		tvPrice.setVisibility(View.VISIBLE);
	}

	public void setText(String text) {
		tvText.setText(text);
	}

	public void setText(int resourceId) {
		tvText.setText(resourceId);
	}

	public void setChecked(boolean isChecked) {
		checkBox.setChecked(isChecked);
	}
}
