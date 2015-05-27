package ru.aviasales.template.ui.view;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.aviasales.core.search.object.FlightData;
import ru.aviasales.template.R;
import ru.aviasales.template.utils.DateUtils;
import ru.aviasales.template.utils.Defined;
import ru.aviasales.template.utils.StringUtils;
import ru.aviasales.template.utils.Utils;


public class ResultsItemRouteView extends RelativeLayout {

	private TextView tvIatas;
	private TextView tvDepartureAndArrivalTime;
	private TextView tvTransfers;
	private TextView tvDuration;

	public ResultsItemRouteView(Context context) {
		super(context);
		setUpViews(context);
	}

	public ResultsItemRouteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews(context);
	}

	public ResultsItemRouteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setUpViews(context);
	}

	protected void setUpViews(Context context) {
		View.inflate(context, R.layout.search_result_item_route, this);

		tvIatas = (TextView) findViewById(R.id.tv_cities);
		tvDepartureAndArrivalTime = (TextView) findViewById(R.id.tv_arrival_and_departure_time);
		tvTransfers = (TextView) findViewById(R.id.tv_flight_transfers);
		tvDuration = (TextView) findViewById(R.id.tv_flight_duration);
	}

	public void setRouteData(List<FlightData> flights) {
		SimpleDateFormat dfTime;
		if (!DateFormat.is24HourFormat(getContext())) {
			dfTime = new SimpleDateFormat(Defined.AM_PM_RESULTS_TIME_FORMAT);
			dfTime.setDateFormatSymbols(DateUtils.getDateFormatSymbols());
		} else {
			dfTime = new SimpleDateFormat(Defined.RESULTS_TIME_FORMAT);
		}
		SimpleDateFormat dfDate = new SimpleDateFormat(
				Defined.RESULTS_SHORT_DATE_FORMAT, DateUtils.getFormatSymbolsShort(getContext()));

		TimeZone utc = TimeZone.getTimeZone(Defined.UTC_TIMEZONE);
		dfTime.setTimeZone(utc);
		dfDate.setTimeZone(utc);

		tvIatas.setText(flights.get(0).getOrigin() + " " + getResources().getText(R.string.dot) +
				" " + flights.get(flights.size() - 1).getDestination());

		Date departureDate = new Date(flights.get(0).getDeparture() * 1000);
		Date arrivalDate = new Date(flights.get(flights.size() - 1).getArrival() * 1000);
		tvDepartureAndArrivalTime.setText(dfTime.format(departureDate) + " " + getResources().getString(R.string.dash) +
				" " + dfTime.format(arrivalDate));

		tvTransfers.setText(StringUtils.getTransferText(getContext(), flights));

		tvDuration.setText(StringUtils.getDurationString(
				getContext(), Utils.getRouteDurationInMin(flights)));
	}


}
