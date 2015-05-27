package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import ru.aviasales.template.R;

public class PassengerPickerImageView extends ImageView {

	private static final int[] STATE_SELECTOR_ENABLED = {R.attr.state_selector_enabled};

	private boolean mIsSelectorEnabled = true;

	public PassengerPickerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (mIsSelectorEnabled) {
			mergeDrawableStates(drawableState, STATE_SELECTOR_ENABLED);
		}
		return drawableState;
	}


	public void setSelectorEnabled(boolean isSelectorEnabled)
	{
		mIsSelectorEnabled = isSelectorEnabled;
		refreshDrawableState();
	}

}
