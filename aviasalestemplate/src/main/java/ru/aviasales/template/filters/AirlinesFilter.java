package ru.aviasales.template.filters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.aviasales.core.search.object.AirlineData;

public class AirlinesFilter implements Parcelable {

	private List<CheckedAirline> airlineList;

	public AirlinesFilter() {
		airlineList = new ArrayList<CheckedAirline>();
	}

	public AirlinesFilter(AirlinesFilter airlinesFilter) {
		if (airlinesFilter.getAirlineList() == null) return;
		airlineList = new ArrayList<CheckedAirline>();
		for (int i = 0; i < airlinesFilter.getAirlineList().size(); i++) {
			airlineList.add(new CheckedAirline(airlinesFilter.getAirlineList().get(i)));
		}
	}

	public void addAirline(String iata) {
		airlineList.add(new CheckedAirline(iata));
	}

	public List<CheckedAirline> getAirlineList() {
		return airlineList;
	}

	public void setAirlineList(List<CheckedAirline> airlineList) {
		this.airlineList = airlineList;
	}

	public void sortByName() {
		Collections.sort(airlineList, CheckedAirline.sortByName);
	}

	public void setAirlinesFromGsonClass(Map<String, AirlineData> airlineMap) {
		for (String iata : airlineMap.keySet()) {
			CheckedAirline airline = new CheckedAirline(iata);
			if (airlineMap.get(iata) != null && airlineMap.get(iata).getName() != null) {
				airline.setName(airlineMap.get(iata).getName());
			} else {
				airline.setName(iata);
			}

			if (airlineMap.get(iata) != null && airlineMap.get(iata).getAverageRate() != null) {
				airline.setRating(airlineMap.get(iata).getAverageRate());
			}
			airlineList.add(airline);
		}
		sortByName();
	}

	public boolean isActual(String airline) {
		for (CheckedAirline checkedAirline : airlineList) {
			if (checkedAirline.getAirline().equals(airline) && !checkedAirline.isChecked()) {
				return false;
			}
		}
		return true;
	}

	public boolean isActive() {
		for (CheckedAirline airline : airlineList) {
			if (!airline.isChecked()) {
				return true;
			}
		}
		return false;
	}

	public void clearFilter() {
		for (CheckedAirline airline : airlineList) {
			airline.setChecked(true);
		}
	}

	public AirlinesFilter(Parcel in) {
		if (airlineList == null) {
			airlineList = new ArrayList<CheckedAirline>();
		}
		in.readTypedList(airlineList, CheckedAirline.CREATOR);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(airlineList);
	}

	public static final Creator<AirlinesFilter> CREATOR = new Creator<AirlinesFilter>() {
		public AirlinesFilter createFromParcel(Parcel in) {
			return new AirlinesFilter(in);
		}

		public AirlinesFilter[] newArray(int size) {
			return new AirlinesFilter[size];
		}
	};
}