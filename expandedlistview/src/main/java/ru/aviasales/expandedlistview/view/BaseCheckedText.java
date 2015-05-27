package ru.aviasales.expandedlistview.view;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class BaseCheckedText implements Parcelable {
	protected String name;
	protected Boolean checked = true;

	public BaseCheckedText() {
	}

	public BaseCheckedText(String name) {
		this.name = name;
	}

	public static Comparator<BaseCheckedText> sortByName = new Comparator<BaseCheckedText>() {
		public int compare(BaseCheckedText checkedText, BaseCheckedText checkedText1) {
			return checkedText.getName().toLowerCase().compareTo(checkedText1.getName().toLowerCase());
		}
	};

	public BaseCheckedText(BaseCheckedText baseCheckedText) {
		name = baseCheckedText.getName();
		checked = baseCheckedText.isChecked();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Override
	public boolean equals(Object o) {
		if ((name == null && ((BaseCheckedText) o).getName() != null) || (name != null && ((BaseCheckedText) o).getName() == null)) {
			return false;
		}
		if ((name == null && ((BaseCheckedText) o).getName() == null)) {
			return true;
		}
		if (name.equals(((BaseCheckedText) o).getName()) && checked.equals(((BaseCheckedText) o).isChecked())) {
			return true;
		}
		return false; //TODO: расширить с учетом значений null
	}

	/**
	 * ************ Передача данных в интент ***************
	 */

	public BaseCheckedText(Parcel in) {
		checked = in.readByte() == 1;
		name = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (checked ? 1 : 0));
		dest.writeString(name);
	}

	public static final Creator<BaseCheckedText> CREATOR = new Creator<BaseCheckedText>() {
		public BaseCheckedText createFromParcel(Parcel in) {
			return new BaseCheckedText(in);
		}

		public BaseCheckedText[] newArray(int size) {
			return new BaseCheckedText[size];
		}
	};
}
