package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import ru.aviasales.core.search.object.TicketData;
import ru.aviasales.template.R;
import ru.aviasales.template.ticket.TicketManager;

public class TicketView extends LinearLayout {

	public TicketView(Context context) {
		super(context);
	}

	public TicketView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setUpViews(Context context, TicketData ticketData) {
		setOrientation(VERTICAL);

		TicketData ticket = ticketData;

		TicketFlightHeaderView thereToFlightHeader = (TicketFlightHeaderView) LayoutInflater.from(context)
				.inflate(R.layout.ticket_flight_header, this, false);
		thereToFlightHeader.setData(ticket, true);

		TicketFlightView thereToFlight = (TicketFlightView) LayoutInflater.from(context)
				.inflate(R.layout.ticket_flight, this, false);
		thereToFlight.setData(TicketManager.getInstance().getAirports(), TicketManager.getInstance().getAirlines(), ticket.getDirectFlights(), false);

		TicketRelativeLayout departCardView = (TicketRelativeLayout) LayoutInflater.from(context).inflate(R.layout.ticket_details_card_view, this, false);
		addView(departCardView);
		departCardView.addView(thereToFlightHeader);
		departCardView.addView(thereToFlight);

		if (ticket.getReturnFlights() != null) {

			TicketFlightHeaderView thereFromHeader = (TicketFlightHeaderView) LayoutInflater.from(context)
					.inflate(R.layout.ticket_flight_header, this, false);
			thereFromHeader.setData(ticket, false);

			TicketFlightView thereFromFlight = (TicketFlightView) LayoutInflater.from(context)
					.inflate(R.layout.ticket_flight, this, false);
			thereFromFlight.setData(TicketManager.getInstance().getAirports(), TicketManager.getInstance().getAirlines(), ticket.getReturnFlights(), true);

			TicketRelativeLayout returnCardView = (TicketRelativeLayout) LayoutInflater.from(context).inflate(R.layout.ticket_details_card_view, this, false);
			addView(returnCardView);
			returnCardView.addView(thereFromHeader);
			returnCardView.addView(thereFromFlight);
		}
	}
}
