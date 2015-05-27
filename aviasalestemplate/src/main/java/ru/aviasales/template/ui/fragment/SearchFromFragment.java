package ru.aviasales.template.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ru.aviasales.core.AviasalesSDK;
import ru.aviasales.core.http.utils.CoreDateUtils;
import ru.aviasales.core.search.object.SearchData;
import ru.aviasales.core.search.searching.OnTicketsSearchListener;
import ru.aviasales.template.R;
import ru.aviasales.template.ui.dialog.DatePickerDialogFragment;
import ru.aviasales.template.ui.dialog.PassengersDialogFragment;
import ru.aviasales.template.ui.dialog.TripClassDialogFragment;
import ru.aviasales.template.ui.listener.AviasalesInterface;
import ru.aviasales.template.ui.model.Passengers;
import ru.aviasales.template.ui.model.SearchFormData;
import ru.aviasales.template.ui.view.SearchFormDateButton;
import ru.aviasales.template.ui.view.SearchFormPassengersButton;
import ru.aviasales.template.ui.view.SearchFormPlaceButton;
import ru.aviasales.template.utils.DateUtils;
import ru.aviasales.template.utils.Utils;

public class SearchFromFragment extends BaseFragment {

	private final static String EXTRA_CALENDAR_DIALOG_RETURN= "ru.aviasales.calendar_dialog_return";

	private AviasalesInterface mAviasalesInterface;

	private SearchFormPlaceButton btnOrigin;
	private SearchFormPlaceButton btnDestination;
	private SearchFormDateButton btnDepartDate;
	private SearchFormDateButton btnReturnDate;
	private TextView tvOriginIata;
	private TextView tvDestinationIata;
	private View btnChangeDirections;
	private CheckBox btnDateSwitch;
	private SearchFormPassengersButton btnPassengers;
	private ViewGroup btnTripClass;
	private TextView tvTripClass;
	private Button btnSearch;
	private SearchFormData searchFormData;
	private View iatas;

	private boolean afterSearchButtonWasPressed = false;
	private boolean calendarDialogReturn;

	public static SearchFromFragment newInstance() {
		return new SearchFromFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		if(savedInstanceState!=null) {
			calendarDialogReturn = savedInstanceState.getBoolean(EXTRA_CALENDAR_DIALOG_RETURN);
		}

		ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.search_form_fragment, container, false);

		setHasOptionsMenu(true);
		showActionBar(false);

		mAviasalesInterface = (AviasalesInterface) getParentFragment();

		setupViews(layout);

		return layout;
	}

	@Override
	public void onStart() {
		super.onStart();

		setupData();
	}

	private void setupViews(final ViewGroup layout) {

		btnOrigin = (SearchFormPlaceButton) layout.findViewById(R.id.btn_origin);
		btnDestination = (SearchFormPlaceButton) layout.findViewById(R.id.btn_destination);

		btnDepartDate = (SearchFormDateButton) layout.findViewById(R.id.btn_depart_date);
		btnReturnDate = (SearchFormDateButton) layout.findViewById(R.id.btn_return_date);

		tvOriginIata = (TextView) layout.findViewById(R.id.tv_origin_iata);
		tvDestinationIata = (TextView) layout.findViewById(R.id.tv_destination_iata);
		iatas = (View) tvOriginIata.getParent();

		btnChangeDirections = layout.findViewById(R.id.btn_change_directions);

		btnDateSwitch = (CheckBox) layout.findViewById(R.id.btn_return_date_switch);

		btnPassengers = (SearchFormPassengersButton) layout.findViewById(R.id.btn_passengers);

		btnTripClass = (ViewGroup) layout.findViewById(R.id.btn_trip_class);
		tvTripClass = (TextView) layout.findViewById(R.id.tv_trip_class);

		btnSearch = (Button) layout.findViewById(R.id.btn_search);

		btnOrigin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getActivity() == null) return;
				startFragment(SelectAirportFragment.newInstance(SelectAirportFragment.TYPE_ORIGIN), true);
			}
		});

		btnDestination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getActivity() == null) return;
				startFragment(SelectAirportFragment.newInstance(SelectAirportFragment.TYPE_DESTINATION), true);
			}
		});

		btnDepartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				createDepartDateDialog();

			}
		});
		btnReturnDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnDateSwitch.setChecked(true);
				searchFormData.setReturnEnabled(btnDateSwitch.isChecked());
				createReturnDateDialog();
			}
		});

		btnChangeDirections.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				searchFormData.switchOriginDestination();
				mAviasalesInterface.setOriginData(searchFormData.getOrigin());
				mAviasalesInterface.setDestinationData(searchFormData.getDestination());

				btnOrigin.setData(searchFormData.getOrigin());
				btnDestination.setData(searchFormData.getDestination());

				if (tvOriginIata != null) {
					setupIatas();
				}

			}
		});

		btnDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			}
		});


		btnDateSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (afterSearchButtonWasPressed) return;

				searchFormData.setReturnEnabled(((CheckBox) view).isChecked());


				if (searchFormData.isReturnEnabled()) {
					if (searchFormData.getReturnDate() == null) {
						createReturnDateDialog();
					}
					btnReturnDate.setEnabled(true);
				} else {
					btnReturnDate.setEnabled(false);
				}
			}
		});

		btnPassengers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (afterSearchButtonWasPressed) return;

				createPassengersDialog();
			}
		});

		btnTripClass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (afterSearchButtonWasPressed) return;

				createTripClassPickerDialog();
			}
		});

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (getActivity() == null) return;


				if (!checkForRestrictions()) {
					return;
				}

				if (!Utils.isOnline(getActivity())) {
					Toast.makeText(getActivity(), getString(R.string.search_no_internet_connection), Toast.LENGTH_LONG)
							.show();
					return;
				}

				AviasalesSDK.getInstance().startTicketsSearch(searchFormData.createSearchParams(), new OnTicketsSearchListener() {
					@Override
					public void onSuccess(SearchData searchData) {
					}

					@Override
					public void onProgressUpdate(int i) {

					}

					@Override
					public void onCanceled() {
					}

					@Override
					public void onError(int i, int i2, String s) {
					}
				});

				startFragment(SearchingFragment.newInstance(), true);

			}
		});

	}

	private void setupChangeDirectionsButtonVisibility() {
		if ((searchFormData.getOrigin() == null || searchFormData.getOrigin().getIata().isEmpty()) &&
				(searchFormData.getDestination() == null || searchFormData.getDestination().getIata().isEmpty())) {
			btnChangeDirections.setVisibility(View.GONE);
		} else {
			btnChangeDirections.setVisibility(View.VISIBLE);
		}
	}

	private void setupData() {
		if (getActivity() == null) return;

		searchFormData = mAviasalesInterface.getSearchFormData();

		btnOrigin.setData(searchFormData.getOrigin());
		btnDestination.setData(searchFormData.getDestination());

		btnDepartDate.setData(searchFormData.getDepartDateString());
		btnReturnDate.setData(searchFormData.getReturnDateString());
		adjustReturnGroupLook();
		if (tvOriginIata != null) {
			setupIatas();
		}
		btnPassengers.setData(searchFormData.getPassengers());
		tvTripClass.setText(searchFormData.getTripClassName());

	}

	private void setupIatas() {
		if (searchFormData.getOrigin() != null) {
			setIata(tvOriginIata, searchFormData.getOrigin().getIata());
		}

		if (searchFormData.getDestination() != null) {
			setIata(tvDestinationIata, searchFormData.getDestination().getIata());
		}

		if (searchFormData.getOrigin() == null || searchFormData.getDestination() == null) {
			iatas.setVisibility(View.INVISIBLE);
		} else {
			iatas.setVisibility(View.VISIBLE);
		}

		setupChangeDirectionsButtonVisibility();
	}

	private void setIata(TextView tvIata, String iata) {
		if (tvIata == null) return;

		if (iata == null) {
			tvIata.setVisibility(View.INVISIBLE);
		} else {
			tvIata.setVisibility(View.VISIBLE);
			tvIata.setText(iata.toUpperCase());
		}
	}

	private void createPassengersDialog() {
		PassengersDialogFragment passengersDialogFragment = new PassengersDialogFragment(
				searchFormData.getPassengers(),
				new PassengersDialogFragment.OnPassengersChangedListener() {

					@Override
					public void onPassengersChanged(Passengers passengers) {
						btnPassengers.setData(passengers);

						searchFormData.setPassengers(passengers);
						dismissDialog();
					}

					@Override
					public void onCancel() {
						dismissDialog();
					}
				}
		);

		createDialog(passengersDialogFragment);
	}


	private void createTripClassPickerDialog() {
		TripClassDialogFragment tripClassDialogFragment = TripClassDialogFragment.newInstance(
				searchFormData.getTripClass(),
				new TripClassDialogFragment.OnTripClassChangedListener() {
					@Override
					public void onTripClassChanged(int tripClass) {
						searchFormData.setTripClass(tripClass);
						tvTripClass.setText(searchFormData.getTripClassName());
						dismissDialog();
					}

					@Override
					public void onCancel() {
						dismissDialog();
					}
				}
		);

		createDialog(tripClassDialogFragment);
	}

	private void createDatePickerDialog(Calendar minDate, Calendar maxDate, Calendar currentDate, DatePickerDialogFragment.OnDateChangedListener listener) {
		if (getActivity() == null) return;

		DatePickerDialogFragment dateDialog = DatePickerDialogFragment.newInstance(minDate, maxDate, currentDate);
		dateDialog.setOnDateChangedListener(listener);
		createDialog(dateDialog);
	}

	private void createDepartDateDialog() {
		Calendar minDate = DateUtils.getMinCalendarDate();
		Calendar maxDate = DateUtils.getMaxCalendarDate();
		Calendar currentDate = DateUtils.convertToCalendar(searchFormData.getDepartDateString());

		calendarDialogReturn = false;

		createDatePickerDialog(minDate, maxDate, currentDate, new DatePickerDialogFragment.OnDateChangedListener() {
			@Override
			public void onDateChanged(Calendar calendar) {
				searchFormData.setDepartDate(calendar);
				btnDepartDate.setData(DateUtils.convertToString(calendar));
				if (searchFormData.getReturnDateString() == null) {
					btnReturnDate.setData(searchFormData.getReturnDateString());
					btnDateSwitch.setChecked(false);
					searchFormData.setReturnEnabled(false);
					btnReturnDate.setEnabled(false);
				}
				dismissDialog();
			}

			@Override
			public void onCancel() {
				dismissDialog();
			}
		});
	}

	private void createReturnDateDialog() {
		Calendar minDate = DateUtils.convertToCalendar(searchFormData.getDepartDateString());
		Calendar maxDate = DateUtils.getMaxCalendarDate();
		Calendar currentDate;
		calendarDialogReturn = true;

		if (searchFormData.getReturnDateString() != null) {
			currentDate = DateUtils.convertToCalendar(searchFormData.getReturnDateString());
		} else {
			currentDate = DateUtils.convertToCalendar(searchFormData.getDepartDateString());
		}

		createDatePickerDialog(minDate, maxDate, currentDate, new DatePickerDialogFragment.OnDateChangedListener() {
			@Override
			public void onDateChanged(Calendar calendar) {
				searchFormData.setReturnDate(calendar);
				btnReturnDate.setData(DateUtils.convertToString(calendar));
				btnReturnDate.setEnabled(true);
				dismissDialog();
			}

			@Override
			public void onCancel() {
				if (searchFormData.getReturnDate() == null) {
					btnDateSwitch.setChecked(false);
				}
				dismissDialog();
			}
		});
	}

	private void adjustReturnGroupLook() {
		btnReturnDate.setEnabled(searchFormData.isReturnEnabled());
		btnDateSwitch.setChecked(searchFormData.isReturnEnabled());
	}

	private boolean checkForRestrictions() {
		if (searchFormData.areDestinationsSet()) {
			showConditionFailedToast(R.string.search_toast_destinations);
			return false;
		}
		if (searchFormData.areDestinationsEqual()) {
			showConditionFailedToast(R.string.search_toast_destinations_equality);
			return false;
		}

		if (searchFormData.getDepartDate() == null) {
			showConditionFailedToast(R.string.search_toast_depart_date);
			return false;
		}

		if (CoreDateUtils.isDateBeforeDateShiftLine(searchFormData.getDepartDate())) {
			showConditionFailedToast(R.string.search_toast_wrong_depart_date);
			return false;
		}

		if (searchFormData.isReturnEnabled()) {
			if (searchFormData.getReturnDate() == null) {
				showConditionFailedToast(R.string.search_toast_return_date);
				return false;
			}

			if (CoreDateUtils.isDateBeforeDateShiftLine(searchFormData.getReturnDate())) {
				showConditionFailedToast(R.string.search_toast_wrong_return_date);
				return false;
			}

			if (CoreDateUtils.isFirstDateBeforeSecondDateWithDayAccuracy(searchFormData.getReturnDate(), searchFormData.getDepartDate())) {
				showConditionFailedToast(R.string.search_toast_return_date_less_than_depart);
				return false;
			}

			if (CoreDateUtils.isDateMoreThanOneYearAfterToday(searchFormData.getDepartDate()) ||
					CoreDateUtils.isDateMoreThanOneYearAfterToday(searchFormData.getReturnDate())) {
				showConditionFailedToast(R.string.search_toast_dates_more_than_1year);
				return false;
			}
		}

		return true;
	}

	private void showConditionFailedToast(int stringId) {
		Toast.makeText(getActivity(), getResources().getString(stringId), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onResume() {
		super.onResume();
		afterSearchButtonWasPressed = false;
	}

	@Override
	public void onStop() {
		mAviasalesInterface.saveState();
		super.onStop();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(EXTRA_CALENDAR_DIALOG_RETURN, calendarDialogReturn);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void resumeDialog(String removedDialogFragmentTag) {
		if (removedDialogFragmentTag.equals(PassengersDialogFragment.TAG)) {
			createPassengersDialog();
		} else if (removedDialogFragmentTag.equals(TripClassDialogFragment.TAG)) {
			createTripClassPickerDialog();
		} else if (removedDialogFragmentTag.equals(DatePickerDialogFragment.TAG)) {
			if(calendarDialogReturn == true) {
				createReturnDateDialog();
			} else {
				createDepartDateDialog();
			}
		}
	}

}
