package com.example.plaan;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SummaryPage extends Activity implements OnClickListener {

	TextView tvTodayText;
	TextView tvTodaysDateText;
	TextView tvMinutesLeft;
	TextView tvMin;

	float scaleDP;

	ArrayList<ActivitiesPlaan> activitiesList;
	int activitiesListIndex;
	ActivitiesPlaan activitiesOnFocus;
	int timeRemaining;

	TextView tvSummary;
	TextView tvSTARTinSummaryPage;
	RelativeLayout rlForSummaryBox;
	RelativeLayout rlSTART;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_page);
		activitiesListIndex = 0;
		scaleDP = getApplicationContext().getResources().getDisplayMetrics().density;

		initializeViews();
		retreiveBundles();
		setTypefaces();
		setDates();

		activitiesOnFocus = activitiesList.get(activitiesListIndex);
		int i = 0;
		for (i = 0; i < activitiesList.size(); i++) {
			createSummaryBox(i);
		}
		rlSTART.setOnClickListener(this);
	}

	private void initializeViews() {
		tvMinutesLeft = (TextView) findViewById(R.id.tvMinutesLeft);
		tvMin = (TextView) findViewById(R.id.tvMin);
		tvTodayText = (TextView) findViewById(R.id.tvToday);
		tvTodaysDateText = (TextView) findViewById(R.id.tvTodaysDate);
		tvSummary = (TextView) findViewById(R.id.tvSummary);
		rlForSummaryBox = (RelativeLayout) findViewById(R.id.rlForSummaryBox);
		tvSTARTinSummaryPage = (TextView) findViewById(R.id.tvSTARTinSummaryPage);
		rlSTART = (RelativeLayout) findViewById(R.id.rlSTART);
	}

	private void createSummaryBox(int i) {
		int tvHeight = (int) (50 * scaleDP + 0.5f);
		TypefacePlaan tfp = new TypefacePlaan();

		RelativeLayout newRlSummaryBox = new RelativeLayout(this);
		int newRlSummaryBoxId = Integer.parseInt("777" + i);
		newRlSummaryBox.setId(newRlSummaryBoxId);
		LayoutParams paramsForNewRlSummaryBox = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (i != 0) {
			paramsForNewRlSummaryBox.addRule(RelativeLayout.BELOW,
					newRlSummaryBoxId - 1);
			paramsForNewRlSummaryBox.addRule(RelativeLayout.ALIGN_LEFT,
					newRlSummaryBoxId - 1);
			paramsForNewRlSummaryBox.setMargins(0, (int) (25 * scaleDP + 0.5f),
					0, 0);
		}

		// rlForTvActivities
		RelativeLayout newRlForTvActivities = new RelativeLayout(this);
		int newRlForTvActivitiesId = Integer.parseInt("274" + i);
		newRlForTvActivities.setId(newRlForTvActivitiesId);
		LayoutParams paramsForNewRlForTvActivities = new LayoutParams(
				LayoutParams.MATCH_PARENT, tvHeight);
		newRlForTvActivities.setBackgroundColor(Color.rgb(230, 230, 230));

		// tvActivities
		TextView newTvActivities = new TextView(this);
		LayoutParams paramsForTvActivities = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsForTvActivities.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsForTvActivities.addRule(RelativeLayout.CENTER_VERTICAL);
		int newTvActivitiesId = Integer.parseInt("888" + i);
		newTvActivities.setId(newTvActivitiesId);
		newTvActivities.setText(activitiesList.get(i).name);
		newTvActivities.setTextColor(Color.rgb(77, 77, 77));
		newTvActivities.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
		tfp.setTypeface(newTvActivities, tfp.LEAGUE_GOTHIC);
		newRlForTvActivities.addView(newTvActivities, paramsForTvActivities);

		// adding newRlForTvActivities to rlsummarybox
		newRlSummaryBox.addView(newRlForTvActivities,
				paramsForNewRlForTvActivities);

		// llYellowInformation
		RelativeLayout newllYellowInformation = new RelativeLayout(this);
		LayoutParams paramsFornewllYellowInformation = new LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (20 * scaleDP + 0.5f));
		int newLlYellowInformationId = Integer.parseInt("999" + i);
		newllYellowInformation.setId(newLlYellowInformationId);
		paramsFornewllYellowInformation.addRule(RelativeLayout.BELOW,
				newRlForTvActivities.getId());
		newllYellowInformation.setBackgroundColor(Color.rgb(255, 204, 0));
		{
			// tvTimeSummary
			TextView tvTimeSummary = new TextView(this);
			int tvTimeSummaryId = Integer.parseInt("185" + i);
			tvTimeSummary.setId(tvTimeSummaryId);
			LayoutParams paramsForTvTimeSummary = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramsForTvTimeSummary.addRule(RelativeLayout.CENTER_IN_PARENT);
			tvTimeSummary.setText("Start : "
					+ activitiesList.get(i).getStartTime() + "/End : "
					+ activitiesList.get(i).getEndTime() + "/"
					+ activitiesList.get(i).getInterval() + "MIN" + "/"
					+ activitiesList.get(i).getTypeString());
			tvTimeSummary.setTextColor(Color.rgb(249, 249, 249));
			tvTimeSummary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			tfp.setTypeface(tvTimeSummary, tfp.LEAGUE_GOTHIC);
			newllYellowInformation.addView(tvTimeSummary,
					paramsForTvTimeSummary);
		}
		// adding newllYellowinformation to rlsummarybox
		newRlSummaryBox.addView(newllYellowInformation,
				paramsFornewllYellowInformation);
		rlForSummaryBox.addView(newRlSummaryBox, paramsForNewRlSummaryBox);
	}

	private void retreiveBundles() {
		Bundle extras = getIntent().getExtras();
		timeRemaining = extras.getInt("timeRemaining");
		activitiesList = extras.getParcelableArrayList("activitiesList");
	}

	private void setDates() {
		CalendarSetter cs = new CalendarSetter();
		cs.setCalendar(tvTodayText, tvTodaysDateText);
	}

	private void setTypefaces() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(tvTodaysDateText, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvTodayText, tfp.TITILIUM);
		tfp.setTypeface(tvMinutesLeft, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMin, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvSummary, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvSTARTinSummaryPage, tfp.LEAGUE_GOTHIC);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent("com.example.plaan.ACDP");
		i.putParcelableArrayListExtra("activitiesList", activitiesList);
		startActivity(i);
	}

}
