package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ru.aviasales.template.R;

public class TicketRelativeLayout extends RelativeLayout {
	private LinearLayout cardViewLayout;

	public TicketRelativeLayout(Context context) {
		super(context);
	}

	public TicketRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TicketRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		cardViewLayout = (LinearLayout) findViewById(R.id.ll_ticket_details_card_view);
		super.onFinishInflate();
	}

	public void addView(View view) {
		if (cardViewLayout != null) {
			cardViewLayout.addView(view);
		}
	}
}
