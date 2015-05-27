package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import ru.aviasales.template.R;
import ru.aviasales.template.filters.StopOverFilter;

public class StopOverFilterView extends LinearLayout {

	public interface OnStopOverChangeState {
		void onChange(boolean oneStopOver, boolean withoutStopOver, boolean twoStopOver);
	}


	private StopOverFilterItemView withoutStopOverView;
	private StopOverFilterItemView oneStopOverView;
	private StopOverFilterItemView twoPlusStopOverView;
	private OnStopOverChangeState listener;

	public static StopOverFilterView newStopOverFilterView(Context context, StopOverFilter filter) {
		StopOverFilterView filterView = new StopOverFilterView(context, null);
		filterView.initFilter(filter);
		return filterView;
	}

	private void initFilter(StopOverFilter filter) {
		withoutStopOverView.setText(R.string.stop_over_filter_view_without);
		oneStopOverView.setText(R.string.stop_over_filter_view_one);
		twoPlusStopOverView.setText(R.string.stop_over_filter_view_twoplus);

		withoutStopOverView.setChecked(filter.isWithoutStopOver());
		oneStopOverView.setChecked(filter.isOneStopOver());
		twoPlusStopOverView.setChecked(filter.isTwoPlusStopOver());

	}


	public StopOverFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context)
				.inflate(R.layout.stop_over_filter_view, this, true);

		withoutStopOverView = (StopOverFilterItemView) findViewById(R.id.cv_stop_over_filter_view_without);
		oneStopOverView = (StopOverFilterItemView) findViewById(R.id.cv_stop_over_filter_view_one);
		twoPlusStopOverView = (StopOverFilterItemView) findViewById(R.id.cv_stop_over_filter_view_two_plus);


		withoutStopOverView.setListener(new StopOverFilterItemView.OnStateChangedListener() {
			@Override
			public void onChanged(boolean newState) {
				if (listener != null) {
					listener.onChange(oneStopOverView.isChecked(), newState, twoPlusStopOverView.isChecked());
				}
			}
		});

		oneStopOverView.setListener(new StopOverFilterItemView.OnStateChangedListener() {
			@Override
			public void onChanged(boolean newState) {
				if (listener != null) {
					listener.onChange(newState, withoutStopOverView.isChecked(), twoPlusStopOverView.isChecked());
				}
			}
		});

		twoPlusStopOverView.setListener(new StopOverFilterItemView.OnStateChangedListener() {
			@Override
			public void onChanged(boolean newState) {
				if (listener != null) {
					listener.onChange(oneStopOverView.isChecked(), withoutStopOverView.isChecked(), newState);
				}
			}
		});
	}

	public void clear() {
		withoutStopOverView.reset();
		oneStopOverView.reset();
		twoPlusStopOverView.reset();
	}

	public void setMinPrices(int minWithoutPrice, int minOnePrice, int twoPlusMinPrice) {
		withoutStopOverView.setMinPrice(minWithoutPrice);
		oneStopOverView.setMinPrice(minOnePrice);
		twoPlusStopOverView.setMinPrice(twoPlusMinPrice);
	}

	public void setViewsEnabled(boolean withoutStopOver, boolean oneStopOver, boolean twoPlusStopOver) {
		if (withoutStopOver) {
			withoutStopOverView.enableView();
		} else {
			withoutStopOverView.disableView();
		}

		if (oneStopOver) {
			oneStopOverView.enableView();
		} else {
			oneStopOverView.disableView();
		}

		if (twoPlusStopOver) {
			twoPlusStopOverView.enableView();
		} else {
			twoPlusStopOverView.disableView();
		}
	}

	public boolean isOneStopOverEnabled() {
		return oneStopOverView.isEnabled();
	}

	public boolean isTwoPlusStopOverEnabled() {
		return twoPlusStopOverView.isEnabled();
	}

	public StopOverFilterView setListener(OnStopOverChangeState listener){
		this.listener = listener;
		return this;
	}
}