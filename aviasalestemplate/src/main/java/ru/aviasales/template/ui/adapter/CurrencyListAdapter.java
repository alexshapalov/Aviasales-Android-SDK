package ru.aviasales.template.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import ru.aviasales.template.R;
import ru.aviasales.template.currencies.Currency;
import ru.aviasales.template.utils.CurrencyUtils;

public class CurrencyListAdapter extends BaseAdapter {
	private Context context;
	private List<Currency> currencyList;

	public CurrencyListAdapter(Context context, List<Currency> currencyList) {
		this.context = context;
		this.currencyList = currencyList;
	}

	public class Holder {
		TextView code;
		CheckedTextView name;
	}

	@Override
	public int getCount() {
		return currencyList.size();
	}

	@Override
	public Object getItem(int position) {
		return currencyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.currency_item, null);

			holder = new Holder();
			holder.name = (CheckedTextView) convertView.findViewById(R.id.tv_currency_item_name);
			holder.code = (TextView) convertView.findViewById(R.id.tv_currency_item_code);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Currency currency = (Currency) getItem(position);

		holder.code.setText(currency.getCode());
		holder.name.setText(currency.getName());
		for (int i = 0; i < holder.name.getCompoundDrawables().length; i++) {
			if (holder.name.getCompoundDrawables()[i] != null) {
				holder.name.getCompoundDrawables()[i].setColorFilter(context.getResources().getColor(R.color.colorAviasalesMain), PorterDuff.Mode.SRC_ATOP);
			}
		}

		holder.name.setChecked(currency.getCode().equalsIgnoreCase(CurrencyUtils.getAppCurrency(context)));

		return convertView;
	}

	public void setItems(List<Currency> items) {
		this.currencyList = items;
		notifyDataSetChanged();
	}
}
