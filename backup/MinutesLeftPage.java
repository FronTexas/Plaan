package com.example.plaan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class MinutesLeftPage extends Activity {

	private TextView tvToday;
	private TextView tvTodaysDate;
	private TextView tvMinutesLeft;
	private TextView tvMin;
	private TextView tvYHTTOf;
	int timeRemaining;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.minutes_left_page);
		
		initializeViews();
		setDates();
		setTypefaces();
		retreiveBundles();

		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Intent i = new Intent("com.example.plaan.TODOPAGE");
					Bundle extras = new Bundle();
					extras.putInt("timeRemaining", timeRemaining);
					i.putExtras(extras);
					startActivity(i);
				}
			}
		};
		timer.start();
	}

	private void retreiveBundles() {
		Bundle extras = getIntent().getExtras();
		timeRemaining = extras.getInt("timeRemaining");
		tvMinutesLeft.setText("" + timeRemaining);
	}

	private void initializeViews() {
		tvToday = (TextView) findViewById(R.id.tvToday);
		tvTodaysDate = (TextView) findViewById(R.id.tvTodaysDate);
		tvMinutesLeft = (TextView) findViewById(R.id.tvMinutesLeft);
		tvMin = (TextView) findViewById(R.id.tvMin);
		tvYHTTOf = (TextView) findViewById(R.id.tvYHTTOf);
	}

	private void setDates() {
		CalendarSetter cs = new CalendarSetter();
		cs.setCalendar(tvToday, tvTodaysDate);
	}

	private void setTypefaces() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(tvToday, tfp.TITILIUM);
		tfp.setTypeface(tvTodaysDate, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMinutesLeft, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMin, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvYHTTOf, tfp.LEAGUE_GOTHIC);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	

	
}
