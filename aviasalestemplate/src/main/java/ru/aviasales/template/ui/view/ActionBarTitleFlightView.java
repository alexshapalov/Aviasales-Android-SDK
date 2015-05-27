package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.aviasales.template.R;


public class ActionBarTitleFlightView extends LinearLayout {

	private TextView tvOrigin;
	private TextView tvDestination;
	private ImageView ivPlanesIcon;

	public ActionBarTitleFlightView(Context context) {
		super(context);
		setUpViews(context);
	}

	public ActionBarTitleFlightView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews(context);
	}

	public void setUpViews(Context context) {
		LayoutInflater.from(context).inflate(R.layout.results_title_view, this, true);
		tvOrigin = (TextView) findViewById(R.id.tv_origin);
		tvDestination = (TextView) findViewById(R.id.tv_destination);
		ivPlanesIcon = (ImageView) findViewById(R.id.iv_planes_icon);
	}

	public void setData(String origin, String destination) {
		tvOrigin.setText(origin.toUpperCase());
		tvDestination.setText(destination.toUpperCase());
		ivPlanesIcon.setImageResource(R.drawable.ic_title_plane);
	}
}
