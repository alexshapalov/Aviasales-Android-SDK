package ru.aviasales.template.filters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.aviasales.core.search.object.AirlineData;
import ru.aviasales.template.R;
import ru.aviasales.expandedlistview.view.BaseCheckedText;

public class AllianceFilter implements Parcelable {

	private Context context;
	private List<BaseCheckedText> allianceList;

	public AllianceFilter(Context context) {
		this.context = context;
		allianceList = new ArrayList<BaseCheckedText>();
	}

	public AllianceFilter(Context context, AllianceFilter allianceFilter) {
		if (allianceFilter.getAllianceList() == null) return;
		this.context = context;

		allianceList = new ArrayList<BaseCheckedText>();
		for (int i = 0; i < allianceFilter.getAllianceList().size(); i++) {
			allianceList.add(new BaseCheckedText(allianceFilter.getAllianceList().get(i)));
		}
	}

	public void addAlliance(String alliance) {
		allianceList.add(new BaseCheckedText(alliance));
	}

	public List<BaseCheckedText> getAllianceList() {
		return allianceList;
	}

	public void setAllianceList(List<BaseCheckedText> allianceList) {
		this.allianceList = allianceList;
	}

	public boolean isActual(String alliance) {
		for (BaseCheckedText checkedAlliance : allianceList) {
			if (checkedAlliance.getName().equals(alliance) && !checkedAlliance.isChecked()) {
				return false;
			}
			if (context.getString(R.string.filters_another_alliances).equals(checkedAlliance.getName()) &&
					alliance == null &&
					!checkedAlliance.isChecked()) {
				return false;
			}
		}
		return true;
	}

	public void setAlliancesFromGsonClass(Map<String, AirlineData> airlineMap) {
		for (String airline : airlineMap.keySet()) {
			if (airlineMap.get(airline) != null && airlineMap.get(airline).getAllianceName() != null) {
				BaseCheckedText cAlliance = new BaseCheckedText(airlineMap.get(airline).getAllianceName());

				if (!allianceList.contains(cAlliance) && cAlliance.getName() != null) {
					allianceList.add(cAlliance);
				}
			}
		}
		Collections.sort(allianceList, BaseCheckedText.sortByName);
		BaseCheckedText anotherAlliances = new BaseCheckedText();
		anotherAlliances.setChecked(true);
		anotherAlliances.setName(context.getString(R.string.filters_another_alliances));
		allianceList.add(anotherAlliances);
	}

	public boolean isActive() {
		for (BaseCheckedText alliance : allianceList) {
			if (!alliance.isChecked()) {
				return true;
			}
		}
		return false;
	}

	public void clearFilter() {
		for (BaseCheckedText alliance : allianceList) {
			alliance.setChecked(true);
		}
	}

	public AllianceFilter(Parcel in) {
		if (allianceList == null) {
			allianceList = new ArrayList<BaseCheckedText>();
		}
		in.readTypedList(allianceList, BaseCheckedText.CREATOR);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(allianceList);
	}

	public static final Creator<AllianceFilter> CREATOR = new Creator<AllianceFilter>() {
		public AllianceFilter createFromParcel(Parcel in) {
			return new AllianceFilter(in);
		}

		public AllianceFilter[] newArray(int size) {
			return new AllianceFilter[size];
		}
	};
}