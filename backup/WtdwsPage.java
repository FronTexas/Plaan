package com.example.plaan;

import java.util.Calendar;

import com.example.plaan.PlaanGeneralClock.GeneralClockListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WtdwsPage extends Activity implements OnClickListener {

	public static  TextView todayText;
	public static  TextView todaysDateText;
	private TextView wtdwsText;
	private TextView tvHours;
	private TextView tvMinutes;
	private ImageButton nextButton;
	private LinearLayout llSleepTimeSetter;
	private int timeRemaining;

	String hoursMinutes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wtdws_page);
		hoursMinutes = "";
		// Weather w = new Weather();

		initializeViews();
		setDates();
		setTypefaces();

		tvHours.setOnClickListener(this);
		tvMinutes.setOnClickListener(this);
		nextButton.setOnClickListener(this);
	}

	private void setTypefaces() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(wtdwsText, tfp.PACIFICO);
		tfp.setTypeface(todayText, tfp.TITILIUM);
		tfp.setTypeface(todaysDateText, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvHours, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMinutes, tfp.LEAGUE_GOTHIC);
	}

	private void setDates() {
		CalendarSetter cs = new CalendarSetter();
		cs.setCalendar(todayText, todaysDateText);
	}

	private void initializeViews() {
		todayText = (TextView) findViewById(R.id.tvToday);
		todaysDateText = (TextView) findViewById(R.id.tvTodaysDate);
		wtdwsText = (TextView) findViewById(R.id.wtdwsText);
		tvHours = (TextView) findViewById(R.id.tvHours);
		tvMinutes = (TextView) findViewById(R.id.tvMinutes);
		nextButton = (ImageButton) findViewById(R.id.bNextButton);
		llSleepTimeSetter = (LinearLayout) findViewById(R.id.llSleepTimeSetter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bNextButton:
			Intent i = new Intent("com.example.plaan.MINUTESLEFTPAGE");
			Bundle extras = new Bundle();
			if (tvHours.getText().toString().equals("")) {
				tvHours.setText("00");
			}
			if (tvMinutes.getText().toString().equals("")) {
				tvMinutes.setText("00");
			}
			// if user does not press IME_DONE when etMeridiem in focus
			if (timeRemaining == 0)
				timeRemaining = calculateTimeRemaining();

			if (tvHours.getText().length() == 1)
				tvHours.setText("0" + tvHours.getText());

			if (tvMinutes.getText().length() == 1)
				tvMinutes.setText("0" + tvMinutes.getText());

			extras.putInt("timeRemaining", timeRemaining);

			i.putExtras(extras);
			startActivity(i);
			break;
		case R.id.tvHours:
		case R.id.tvMinutes:
			PlaanGeneralClock pgc = new PlaanGeneralClock(this,"What time do you want to sleep?");
			pgc.show();
			pgc.setDialogResult(new GeneralClockListener() {
				public void finish(String result) {
					WtdwsPage.this.hoursMinutes = result;
					WtdwsPage.this.tvHours.setText("" + hoursMinutes.charAt(0)
							+ hoursMinutes.charAt(1));
					WtdwsPage.this.tvMinutes.setText(""
							+ hoursMinutes.charAt(2) + hoursMinutes.charAt(3));
					WtdwsPage.this.llSleepTimeSetter
							.setVisibility(View.VISIBLE);
				}
			});
			break;
		// Intent in = new Intent("com.example.plaan.MAT");
		// startActivity(in);
		default:
			break;
		}
	}

	private int calculateTimeRemaining() {
		Calendar c = Calendar.getInstance();
		String time = cGetTime(c);
		int userHours = Integer.parseInt(tvHours.getText().toString());
		int userMinutes = Integer.parseInt(tvMinutes.getText().toString());
//		if (tvHours.getText().toString().equals("12")
//				&& meridiemText.getText().toString().equals("AM")) {
//			userHours = 0;
//		}
		int systemHours = Integer
				.parseInt("" + time.charAt(0) + time.charAt(1));
		int systemMinutes = Integer.parseInt("" + time.charAt(3)
				+ time.charAt(4));

		int hoursDifference = userHours - systemHours;
		int minutesDifference = userMinutes - systemMinutes;
		int timeRemaining = hoursDifference * 60 + minutesDifference;

		if (timeRemaining <= 0) {
			hoursDifference = (userHours + 24) - systemHours;
			timeRemaining = hoursDifference * 60 + minutesDifference;
		}

		return timeRemaining;
	}

	private String cGetTime(Calendar c) {
		String time = c.getTime().toString();
		StringBuffer returned = new StringBuffer();
		for (int i = 11; i < 16; i++) {
			returned.append(time.charAt(i));
		}
		return returned.toString();
	}

}
