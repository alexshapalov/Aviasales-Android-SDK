package ru.aviasales.template.ui.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.aviasales.template.R;


public class ProgressDialogWindow extends BaseDialogFragment {

	public final static String TAG = "fragment.ProgressDialogWindow";

	public ProgressDialogWindow() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.destination_progress_dialog, container);
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
		return view;
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setDismissMessage(null);
		super.onDestroyView();
	}

	@Override
	public String getFragmentTag() {
		return TAG;
	}
}