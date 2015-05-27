package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.aviasales.template.R;


public class SelectAirportInfoView extends LinearLayout {

	private TextView textView;
	private RelativeLayout layout;
	private ActivityIndicator progressBar;
	private Context context;
	private boolean clickAlowed = false;

	public SelectAirportInfoView(Context context) {
		this(context, null);
	}

	public SelectAirportInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.destination_fragment_location_view,
				this, true);

		this.context = context;

		textView = (TextView) findViewById(R.id.tv_destination_fragment_location_text);
		progressBar = (ActivityIndicator) findViewById(R.id.pb_destination_fragment_location_text);
		layout = (RelativeLayout) findViewById(R.id.llDestinationFeagmentLocation);
		layout.setEnabled(false);
	}

	public void setMessage(String text) {
		progressBar.setVisibility(View.GONE);
		textView.setVisibility(View.VISIBLE);
		textView.setText(text);
	}

	public void setMessageResource(int stringId) {
		progressBar.clearAnimation();
		progressBar.setVisibility(View.GONE);
		textView.setVisibility(View.VISIBLE);
		textView.setText(context.getResources().getString(stringId));
	}

	public void showProgressBar() {
		layout.setEnabled(false);
		progressBar.setVisibility(View.VISIBLE);
		textView.setVisibility(View.GONE);
	}

	public void kickProgressBar() {
		if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
			progressBar.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	public void setOnClickListener(final OnClickListener listener) {
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (clickAlowed) {
					listener.onClick(view);
				}
			}
		});
	}

	public void allowClick(boolean allowed) {
		clickAlowed = allowed;
	}

	public void setViewEnabled(boolean enabled) {
		layout.setEnabled(enabled);
	}
}
