package com.example.plaan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class FreeTimeCard extends ActivityCard {
	TextView tvFREEText, tvFreeTimeText, tvIntervalFT;
	long startHours;
	long startMinutes;
	long endHours;
	long endMinutes;

	public FreeTimeCard(Context context, String startTime, String endTime) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addView(inflater.inflate(R.layout.free_time_card, null));
		initializeViews();
		setTypefaces();
		createFreeTimeActivitiesPlaan(startTime, endTime);
		setFreeTimeCardTextViews(startTime, endTime, super.theActivity.interval);
	}

	private void createFreeTimeActivitiesPlaan(String startTime, String endTime) {
		// setting freeTime activity
		super.theActivity = new ActivitiesPlaan("Free Time");
		super.theActivity.setType(ActivitiesPlaan.TYPE_ONE_TIME);
		super.theActivity.setInterval(startTime, endTime);
		super.theActivity.setActivityCard(this);
	}

	public void setFreeTimeCardTextViews(String startTime, String endTime,
			long timeDuration) {
		// freeTimeTime
		int[] format = new int[3];
		formatMillis(format, timeDuration);
		String timeFormatted = "";
		String timeFormattedText = "";
		if (format[0] != 0) {
			timeFormatted += addZeros("" + format[0]) + ":";
			timeFormattedText += format[0] + "H";
		}
		if (format[0] != 0 || format[1] != 0) {
			timeFormatted += addZeros("" + format[1]) + ":";
			timeFormattedText += format[1] + "M";
		}
		timeFormatted += addZeros("" + format[2]);
		if (format[2] != 0)
			timeFormattedText += format[2] + "S";
		tvCountDown.setText(timeFormatted);

		// startEnd
		super.tvStartEndCD.setText(startTime + " - " + endTime);

		// interval
		tvIntervalFT.setText(timeFormattedText);
	}

	private void formatMillis(int[] format, long millisUntilFinished) {
		// format[0] = hours, format[1] = minutes , format[2] = seconds
		format[0] = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
		format[1] = (int) ((millisUntilFinished / (1000 * 60)) % 60);
		format[2] = (int) (millisUntilFinished / 1000) % 60;
	}

	private String addZeros(String s) {
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	private void setTypefaces() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(tvCountDown, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvFREEText, TypefacePlaan.OPEN_SANS_ITALIC);
		tfp.setTypeface(tvFreeTimeText, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(super.tvStartEndCD, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvIntervalFT, TypefacePlaan.LEAGUE_GOTHIC);
	}

	private void initializeViews() {
		super.llRootActivityCard = (LinearLayout) findViewById(R.id.llRootFreeTimeCard);
		super.tvCountDown = (TextView) findViewById(R.id.tvFreeTimeTime);
		super.tvStartEndCD = (TextView) findViewById(R.id.tvStartEndFT);
		tvFREEText = (TextView) findViewById(R.id.tvFREEText);
		tvFreeTimeText = (TextView) findViewById(R.id.tvFreeTimeText);
		tvIntervalFT = (TextView) findViewById(R.id.tvIntervalFT);
	}

}
