package ru.aviasales.template.filters;

import android.os.Parcel;
import android.os.Parcelable;

import ru.aviasales.expandedlistview.view.BaseCheckedText;

public class CheckedAgency extends BaseCheckedText implements Parcelable {
	private String id;

	public CheckedAgency(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public CheckedAgency(CheckedAgency checkedAgency) {
		super(checkedAgency);
		id = checkedAgency.getId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || ((Object) this).getClass() != o.getClass()) return false;

		CheckedAgency agency = (CheckedAgency) o;

		if (id != null ? !id.equals(agency.id) : agency.id != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (checked != null ? checked.hashCode() : 0);
		return result;
	}


	public CheckedAgency(Parcel in) {
		super(in);
		id = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(id);
	}

	public static final Creator<CheckedAgency> CREATOR = new Creator<CheckedAgency>() {
		public CheckedAgency createFromParcel(Parcel in) {
			return new CheckedAgency(in);
		}

		public CheckedAgency[] newArray(int size) {
			return new CheckedAgency[size];
		}
	};
}