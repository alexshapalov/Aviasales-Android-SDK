package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.aviasales.core.search.object.AirlineData;
import ru.aviasales.core.search.object.AirportData;
import ru.aviasales.core.search.object.FlightData;
import ru.aviasales.template.R;

public class TicketFlightView extends LinearLayout {

	private List<FlightData> flights;
	private Map<String, AirlineData> airlines;
	private Map<String, AirportData> airports;

	public TicketFlightView(Context context) {
		super(context);
		setOrientation(VERTICAL);
		forEditMode();
	}

	public TicketFlightView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
		forEditMode();
	}

	private void forEditMode() {
		if (isInEditMode()) {
			flights = new ArrayList<FlightData>();
			flights.add(new FlightData());
			flights.add(new FlightData());
			generateViews(false);
		}
	}

	public void setData(Map<String, AirportData> airports, Map<String, AirlineData> airlines,
	                    List<FlightData> flights, boolean returnFlight) {
		this.airports = airports;
		this.airlines = airlines;
		this.flights = flights;
		generateViews(returnFlight);
	}

	private void generateViews(boolean returnFlight) {
		FlightData prevFlight = null;
		for (FlightData flight : flights) {
			if (prevFlight != null) {
				TicketTransferView transferView = (TicketTransferView) LayoutInflater.from(getContext())
						.inflate(R.layout.ticket_transfer_item, this, false);
				addView(transferView);
				if (!isInEditMode()) {
					transferView.setData(prevFlight, flight, airports);
				}
			}

			TicketFlightSegmentView flightSegmentView = (TicketFlightSegmentView) LayoutInflater.from(getContext())
					.inflate(R.layout.ticket_flight_segment, this, false);
			addView(flightSegmentView);
			if (!isInEditMode()) {
				flightSegmentView.setData(airlines, flight, returnFlight);
			}
			prevFlight = flight;
		}
	}
}
