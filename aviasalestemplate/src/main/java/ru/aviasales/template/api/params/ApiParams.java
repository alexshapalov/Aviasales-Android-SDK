package ru.aviasales.template.api.params;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

public class ApiParams {
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	protected void addParam(List<NameValuePair> params, String name, String value) {
		if (value == null) {
			return;
		} else {
			params.add(new BasicNameValuePair(name, value));
		}
	}
}
