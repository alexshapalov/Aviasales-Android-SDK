package ru.aviasales.template.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.aviasales.template.R;

public class PassengersPickerView extends RelativeLayout {

	public static final int ADULTS = 0;
	public static final int CHILDREN = 1;
	public static final int INFANTS = 2;
	private int passengerTypeValue = ADULTS;

	private PassengerPickerImageView btnDecrease;
	private PassengerPickerImageView btnIncrease;
	private TextView tvNumber;

	private boolean isIncreaseEnabled = true;
	private boolean isDecreaseEnabled = true;
	private boolean isToastShown = false;

	private Integer number;

	private OnChangeListener onChangeListener;

	public PassengersPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.passengers_picker, this, true);

		TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.PassengersPickerView);
		passengerTypeValue = values.getInt(R.styleable.PassengersPickerView_passengerType, ADULTS);
		values.recycle();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (isInEditMode()) {
			return;
		}

		btnDecrease = (PassengerPickerImageView) findViewById(R.id.btn_decrease);
		btnIncrease = (PassengerPickerImageView) findViewById(R.id.btn_increase);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		ImageView passengerIcon = (ImageView) findViewById(R.id.ic_passenger);

		tvNumber.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		switch (passengerTypeValue) {
			case ADULTS:
				passengerIcon.setImageResource(R.drawable.passengers_picker_adults);
				break;
			case CHILDREN:
				passengerIcon.setImageResource(R.drawable.passengers_picker_children);
				break;
			case INFANTS:
				passengerIcon.setImageResource(R.drawable.passengers_picker_infants);
				break;
		}

		btnDecrease.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isDecreaseEnabled) {
					if (number == null) {
						number = 0;
					}
					number--;
					tvNumber.setText(number.toString());
					onChangeListener.onChange(number, PassengersPickerView.this.getId());
				}
			}
		});

		btnIncrease.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isIncreaseEnabled) {
					if (number == null) {
						number = 0;
					}
					number++;
					tvNumber.setText(number.toString());
					onChangeListener.onChange(number, PassengersPickerView.this.getId());
				} else if (!isToastShown && passengerTypeValue == INFANTS && number != 8) {
					isToastShown = true;
					Toast.makeText(getContext(), getContext().getResources().getString(R.string.search_toast_infants_regulations), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void setDefaultNumber(int number) {
		this.number = number;
		tvNumber.setText(String.valueOf(number));
	}

	public void setOnChangeListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}

	public void setDecreaseEnabled(boolean flag) {
		isDecreaseEnabled = flag;
		btnDecrease.setSelectorEnabled(flag);
	}

	public void setIncreaseEnabled(boolean flag) {
		isIncreaseEnabled = flag;
		isToastShown = false;
		btnIncrease.setSelectorEnabled(flag);
	}

	public void decreaseNumber() {
		number--;
		tvNumber.setText(number.toString());
	}

	public interface OnChangeListener {
		void onChange(int newValue, int id);
	}
}
