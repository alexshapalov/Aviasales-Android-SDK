package ru.aviasales.template.ui.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.aviasales.core.http.utils.CoreDateUtils;
import ru.aviasales.core.search.params.SearchParams;
import ru.aviasales.core.search_airports.object.PlaceData;
import ru.aviasales.template.R;
import ru.aviasales.template.utils.DateUtils;
import ru.aviasales.template.utils.Defined;
import ru.aviasales.template.utils.Utils;

public class SearchFormData {

	public static final String HAS_SAVED_STATE = "has_saved_state";
	public static final String EXTRA_RETURN_ENABLED = "extra-return_enabled";

	private static boolean isFirstLaunch = false;

	private PlaceData origin;
	private PlaceData destination;
	private String departDate;
	private String returnDate;
	private boolean returnEnabled;
	private int tripClass = SearchParams.TRIP_CLASS_ECONOMY;

	private Passengers passengers = new Passengers();

	private Context context;


	public SearchFormData(Context context) {

		this.context = context;

		SharedPreferences prefs = Utils.getPreferences(context);

		if (prefs.getBoolean(HAS_SAVED_STATE, false)) {


			origin = PlaceData.create(prefs.getString(SearchParams.SEARCH_PARAM_ORIGIN_NAME, null));
			destination = PlaceData.create(prefs.getString(SearchParams.SEARCH_PARAM_DESTINATION_NAME, null));

			departDate = prefs.getString(SearchParams.SEARCH_PARAM_DEPART_DATE, null);
			returnDate = prefs.getString(SearchParams.SEARCH_PARAM_RETURN_DATE, null);

			if (departDate != null) {
				LocalDate departLocalDate = new LocalDate(getDepartDate());
				if (departLocalDate.isBefore(new LocalDate(DateTimeZone.forID("-11:00")))) {

					if (!isFirstLaunch) {
						departDate = getTomorrowDate();
					} else {
						departDate = getTodayDate();
					}
					returnDate = null;
					prefs.edit()
							.remove(SearchParams.SEARCH_PARAM_DEPART_DATE)
							.remove(SearchParams.SEARCH_PARAM_RETURN_DATE)
							.apply();
				}
			}

			returnEnabled = prefs.getBoolean(EXTRA_RETURN_ENABLED, false);

			tripClass = prefs.getInt(SearchParams.SEARCH_PARAM_TRIP_CLASS, SearchParams.TRIP_CLASS_ECONOMY);

			passengers = new Passengers();

			passengers.setAdults(prefs.getInt(SearchParams.SEARCH_PARAM_ADULTS, 1));
			passengers.setChildren(prefs.getInt(SearchParams.SEARCH_PARAM_CHILDREN, 0));
			passengers.setInfants(prefs.getInt(SearchParams.SEARCH_PARAM_INFANTS, 0));
		} else {
			departDate = getTomorrowDate();
		}
		isFirstLaunch = true;
	}

	public PlaceData getOrigin() {
		return origin;
	}

	public PlaceData getDestination() {
		return destination;
	}

	public void setOrigin(PlaceData origin) {
		this.origin = origin;
	}

	public void setDestination(PlaceData destination) {
		this.destination = destination;
	}

	private String getTomorrowDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return formatData(calendar);
	}

	private String getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		return formatData(calendar);
	}

	public void setDepartDate(String departDate) {
		this.departDate = departDate;
	}

	public void setDepartDate(Calendar departDate) {
		this.departDate = DateUtils.convertToString(departDate);
		checkReturnDate();
	}

	private void checkReturnDate() {
		if (returnDate == null) {
			return;
		}
		if (DateUtils.convertToCalendar(departDate).compareTo(DateUtils.convertToCalendar(returnDate)) > 0) {
			returnDate = null;
		}
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public void setReturnDate(Calendar returnDate) {
		this.returnDate = DateUtils.convertToString(returnDate);
	}

	public Date getDepartDate() {
		return getDate(departDate);
	}

	public Date getReturnDate() {
		return getDate(returnDate);
	}

	public String getDepartDateString() {
		return departDate;
	}

	public String getReturnDateString() {
		return returnDate;
	}

	public Passengers getPassengers() {
		return passengers;
	}

	public void setReturnEnabled(boolean isReturnEnabled) {
		returnEnabled = isReturnEnabled;
	}

	public boolean isReturnEnabled() {
		return returnEnabled;
	}

	private Date getDate(String date) {
		if (date == null) return null;
		return CoreDateUtils.parseDateString(date, Defined.SEARCH_SERVER_DATE_FORMAT);
	}

	private String formatData(Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat(Defined.SEARCH_SERVER_DATE_FORMAT);
		return sdf.format(calendar.getTime());
	}

	public int getTripClass() {
		return tripClass;
	}

	public String getTripClassName() {
		switch (tripClass) {
			case SearchParams.TRIP_CLASS_ECONOMY:
				return Utils.capitalizeFirstLetter(context.getString(R.string.trip_class_economy));
			case SearchParams.TRIP_CLASS_BUSINESS:
				return Utils.capitalizeFirstLetter(context.getString(R.string.trip_class_business));
			case SearchParams.TRIP_CLASS_PREMIUM_ECONOMY:
				return Utils.capitalizeFirstLetter(context.getString(R.string.trip_class_premium_economy));
		}
		return null;
	}

	public SearchParams createSearchParams() {
		SearchParams params = new SearchParams();
		if (origin != null) {
			params.setOriginIata(origin.getIata());
		}

		if (destination != null) {
			params.setDestinationIata(destination.getIata());
		}

		params.setDepartDate(departDate);
		if (returnEnabled) {
			params.setReturnDate(returnDate);
		}
		params.setAdults(passengers.getAdults());
		params.setChildren(passengers.getChildren());
		params.setInfants(passengers.getInfants());
		params.setTripClass(tripClass);
		params.setDirect(SearchParams.DIRECT_STOP_OVER);
		params.setRange(SearchParams.RANGE_EXACT);
		params.setEnableApiAuth(true);
		params.setPreinitializeFilters(true);
		params.setContext(context.getApplicationContext());

		return params;
	}

	public void switchOriginDestination() {
		PlaceData tmp = origin;
		origin = destination;
		destination = tmp;
	}

	public void saveState() {
		String originSerialized = null;
		String destinationSerialized = null;

		if (origin != null) {
			originSerialized = origin.serialize();
		}

		if (destination != null) {
			destinationSerialized = destination.serialize();
		}
		SharedPreferences preferences = Utils.getPreferences(context);

		preferences.edit()
				.putString(SearchParams.SEARCH_PARAM_ORIGIN_NAME, originSerialized)
				.putString(SearchParams.SEARCH_PARAM_DESTINATION_NAME, destinationSerialized)
				.putString(SearchParams.SEARCH_PARAM_DEPART_DATE, departDate)
				.putString(SearchParams.SEARCH_PARAM_RETURN_DATE, returnDate)
				.putBoolean(EXTRA_RETURN_ENABLED, returnEnabled)
				.putInt(SearchParams.SEARCH_PARAM_ADULTS, passengers.getAdults())
				.putInt(SearchParams.SEARCH_PARAM_CHILDREN, passengers.getChildren())
				.putInt(SearchParams.SEARCH_PARAM_INFANTS, passengers.getInfants())
				.putInt(SearchParams.SEARCH_PARAM_TRIP_CLASS, tripClass)
				.putBoolean(HAS_SAVED_STATE, true)
				.apply();
	}

	public void setTripClass(int tripClass) {
		this.tripClass = tripClass;
	}

	public void setPassengers(Passengers passengers) {
		this.passengers = passengers;
	}

	public boolean areDestinationsEqual() {
		return origin.equals(destination) || areCitiesEquals();
	}

	public boolean areCitiesEquals() {
		return origin.getName().equals(destination.getName());
	}

	public boolean areDestinationsSet() {
		return origin == null || destination == null;
	}
}
