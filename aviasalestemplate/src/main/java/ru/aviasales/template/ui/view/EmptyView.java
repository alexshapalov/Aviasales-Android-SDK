package ru.aviasales.template.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.aviasales.template.R;

public class EmptyView extends FrameLayout {
	public static final int TYPE_RESULTS = 4;

	private TextView tvTitle;
	private TextView tvContent;
	private Button button;
	private OnClickListener listener;

	private int type = -1;

	public EmptyView(Context context) {
		super(context);
		setupViews();
	}

	public EmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setupViews();
	}

	private void setupViews() {
		View.inflate(getContext(), R.layout.empty_fragment_template, this);
		tvTitle = (TextView) findViewById(R.id.tv_empty_fragment_template_title);
		tvContent = (TextView) findViewById(R.id.tv_empty_fragment_template_content);
		button = (Button) findViewById(R.id.btn_empty_fragment_template);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (type) {
					case TYPE_RESULTS:
						if (listener != null) {
							listener.onClick(button);
						}
						break;
				}
			}
		});
	}

	public void setType(int type) {
		this.type = type;

		switch (type) {
			case TYPE_RESULTS:
				tvTitle.setText(R.string.empty_view_results_title);
				tvContent.setVisibility(View.GONE);
				button.setVisibility(View.VISIBLE);
				button.setText(R.string.empty_view_results_button);
				break;
		}
	}

	public void setOnButtonClickListener(OnClickListener onClickListener) {
		this.listener = onClickListener;
	}
}
