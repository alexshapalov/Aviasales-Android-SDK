package ru.aviasales.template.ui.dialog;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.aviasales.template.R;
import ru.aviasales.template.utils.StringUtils;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BrowserLoadingDialogFragment extends DialogFragment {

	private String agency;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.browser_dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.browser_dialog, container, false);
		((TextView) layout.findViewById(R.id.tv_loading_agency)).setText(getAgencyText());

		getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					if (getActivity() != null) {
						getActivity().onBackPressed();
						return true;
					}
				}
				return false;
			}
		});

		return layout;
	}

	private String getAgencyText() {
		return String.format(getString(R.string.browser_loading_agency), StringUtils.capitalizeFirstLetter(agency));
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}
}
