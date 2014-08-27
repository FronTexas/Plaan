package com.example.plaan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class TimeTypeSpinner extends Activity {
	String[] spinnerValues = { "MIN","HRS"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_spinner_page);
		Spinner mySpinner = (Spinner) findViewById(R.id.spinner_show);
		mySpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner,
				spinnerValues));
	}

	public class MyAdapter extends ArrayAdapter<String> {
		public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
			super(ctx, txtViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
					false);
			TextView main_text = (TextView) mySpinner
					.findViewById(R.id.text_main_seen);
			main_text.setText(spinnerValues[position]);
			TextView subSpinner = (TextView) mySpinner
					.findViewById(R.id.sub_text_seen);
			return mySpinner;
		}
	}
}
