package com.example.plaan;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;

public class CalendarActivity extends Activity {

	final DisplayMetrics metrics = new DisplayMetrics();
	private GridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_layout);
		int[] mToday = new int[3];

		Calendar mCalendar = Calendar.getInstance();
		mToday[0] = mCalendar.get(Calendar.DAY_OF_MONTH);
		mToday[1] = mCalendar.get(Calendar.MONTH);
		mToday[2] = mCalendar.get(Calendar.YEAR);

		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		mGridView = (GridView) findViewById(R.id.gvCalendar);
		MonthAdapter mA = new MonthAdapter(this, mToday[1], mToday[2], metrics) {

			@Override
			protected void onDate(int[] date, int position, View item) {

			}
		};

		mGridView.setAdapter(mA);
	}
}
