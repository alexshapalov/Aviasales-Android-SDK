package ru.aviasales.template.filters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

import ru.aviasales.expandedlistview.view.BaseCheckedText;

public class CheckedAirport extends BaseCheckedText implements Parcelable {

	private String iata;
	private String city;
	private String country;
	private float rating;

	public static Comparator<CheckedAirport> sortByName = new Comparator<CheckedAirport>() {
		public int compare(CheckedAirport checkedAirport, CheckedAirport checkedAirport1) {
			return checkedAirport.getCity().toLowerCase().compareTo(checkedAirport1.getCity().toLowerCase());
		}
	};

	public CheckedAirport(String iata) {
		this.iata = iata;
	}

	public CheckedAirport(CheckedAirport checkedAirport) {
		iata = checkedAirport.getIata();
		city = checkedAirport.getCity();
		country = checkedAirport.getCountry();
		rating = checkedAirport.getRating();
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = (float) rating;
	}

	@Override
	public boolean equals(Object o) {
		CheckedAirport secondAirport = (CheckedAirport) o;
		if (iata.equals(secondAirport.getIata()) && name.equals(secondAirport.getName()) &&
				city.equals(secondAirport.getCity()) && country.equals(secondAirport.getCountry())) {
			return true;
		} else {
			return false;
		}
	}


	public CheckedAirport(Parcel in) {
		super(in);
		iata = in.readString();
		city = in.readString();
		country = in.readString();
		rating = in.readFloat();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(iata);
		dest.writeString(city);
		dest.writeString(country);
		dest.writeFloat(rating);
	}

	public static final Creator<CheckedAirport> CREATOR = new Creator<CheckedAirport>() {
		public CheckedAirport createFromParcel(Parcel in) {
			return new CheckedAirport(in);
		}

		public CheckedAirport[] newArray(int size) {
			return new CheckedAirport[size];
		}
	};
}
