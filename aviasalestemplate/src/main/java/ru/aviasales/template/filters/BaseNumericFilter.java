package ru.aviasales.template.filters;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseNumericFilter implements Parcelable {

	protected int maxValue = Integer.MIN_VALUE;
	protected int minValue = Integer.MAX_VALUE;
	protected int currentMaxValue;
	protected int currentMinValue;

	public BaseNumericFilter() {

	}

	public BaseNumericFilter(BaseNumericFilter numericFilter) {
		maxValue = numericFilter.getMaxValue();
		minValue = numericFilter.getMinValue();
		currentMaxValue = numericFilter.getCurrentMaxValue();
		currentMinValue = numericFilter.getCurrentMinValue();
	}

	public boolean isActive() {
		if (maxValue == currentMaxValue && minValue == currentMinValue) {
			return false;
		}
		return true;
	}

	public boolean isValid(){
		return maxValue != minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getCurrentMinValue() {
		return currentMinValue;
	}

	public void setCurrentMinValue(int currentMinValue) {
		if(currentMinValue > minValue) {
			this.currentMinValue = currentMinValue;
		} else {
			this.currentMinValue = minValue;
		}
	}

	public int getCurrentMaxValue() {
		return currentMaxValue;
	}

	public void setCurrentMaxValue(int currentMaxValue) {
		if(currentMaxValue < maxValue) {
			this.currentMaxValue = currentMaxValue;
		} else {
			this.currentMaxValue = maxValue;
		}
	}

	public void clearFilter() {
		currentMaxValue = maxValue;
		currentMinValue = minValue;
	}

	protected boolean isActual(int value) {
		return value >= currentMinValue && value <= currentMaxValue;
	}

	protected boolean isActualForMaxValue(int value) {
		return value <= currentMaxValue;
	}

	protected boolean isActualForMinValue(int value) {
		return value >= currentMinValue;
	}

	public BaseNumericFilter(Parcel in) {
		maxValue = in.readInt();
		minValue = in.readInt();
		currentMaxValue = in.readInt();
		currentMinValue = in.readInt();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(maxValue);
		dest.writeInt(minValue);
		dest.writeInt(currentMaxValue);
		dest.writeInt(currentMinValue);
	}

	public static final Creator<BaseNumericFilter> CREATOR = new Creator<BaseNumericFilter>() {
		public BaseNumericFilter createFromParcel(Parcel in) {
			return new BaseNumericFilter(in);
		}

		public BaseNumericFilter[] newArray(int size) {
			return new BaseNumericFilter[size];
		}
	};
}
