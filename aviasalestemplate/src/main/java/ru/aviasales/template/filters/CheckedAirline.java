package ru.aviasales.template.filters;

import android.os.Parcel;
import android.os.Parcelable;

import ru.aviasales.expandedlistview.view.BaseCheckedText;

public class CheckedAirline extends BaseCheckedText implements Parcelable {

	private String iata;
	private float rating;
	private int minimalPrice = Integer.MAX_VALUE;

	public CheckedAirline(CheckedAirline checkedAirline) {
		super(checkedAirline);
		iata = checkedAirline.getIata();
		rating = checkedAirline.getRating();
		minimalPrice = checkedAirline.getMinimalPrice();
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public CheckedAirline(String iata) {
		this.iata = iata;
	}

	public String getAirline() {
		return iata;
	}

	public void setAirline(String iata) {
		this.iata = iata;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = (float) rating;
	}

	public int getMinimalPrice() {
		return minimalPrice;
	}

	public void setMinimalPrice(int minimalPrice) {
		this.minimalPrice = minimalPrice;
	}

	public CheckedAirline(Parcel in) {
		super(in);
		iata = in.readString();
		rating = in.readFloat();
		minimalPrice = in.readInt();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(iata);
		dest.writeFloat(rating);
		dest.writeInt(minimalPrice);
	}

	public static final Creator<CheckedAirline> CREATOR = new Creator<CheckedAirline>() {
		public CheckedAirline createFromParcel(Parcel in) {
			return new CheckedAirline(in);
		}

		public CheckedAirline[] newArray(int size) {
			return new CheckedAirline[size];
		}
	};
}
