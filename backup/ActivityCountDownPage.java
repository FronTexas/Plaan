package com.example.plaan;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityCountDownPage extends Activity {

	TextView tvActivitesName, tvTimer, tvDONE, tvBreak, tvLoopLeft, tvX;
	RelativeLayout rlLoopInfo;
	NotificationManager nm;
	static final int notificationId = 1394885;

	int timeRemaining;
	ArrayList<ActivitiesPlaan> activitiesList;
	Handler handler;
	int activitiesListIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count_down_page);
		handler = new Handler();
		activitiesListIndex = 0;
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(notificationId);

		initializeViews();
		setTypeface();
		retreiveBundles();

		runActivitiesTimerRecursive(activitiesList.get(activitiesListIndex));
	}

	private void runActivitiesTimerRecursive(ActivitiesPlaan theActivities) {
		long time = theActivities.interval;
		tvActivitesName.setText(theActivities.name);
		if (theActivities.getType() == ActivitiesPlaan.TYPE_LOOPING) {
			// spawn looping layout
			rlLoopInfo.setVisibility(View.VISIBLE);
			tvLoopLeft.setText("" + theActivities.getLoopLeft());
			// break or looping
			if (theActivities.getCurrentState() == ActivitiesPlaan.LOOPING_STATE) {
				tvBreak.setVisibility(View.INVISIBLE);
				time = theActivities.loopTime();
			} else if (theActivities.getCurrentState() == ActivitiesPlaan.BREAK_STATE) {
				tvBreak.setVisibility(View.VISIBLE);
				time = theActivities.breakTime();
			}
		} else {
			rlLoopInfo.setVisibility(View.INVISIBLE);
			tvBreak.setVisibility(View.INVISIBLE);
		}

		// run the actual timer
		new CountDownTimer(time, 1000) {
			public void onTick(long millisUntilFinished) {
				tvTimer.setText(""
						+ addZeros("" + ((millisUntilFinished / 1000) / 60))
						+ ":"
						+ addZeros("" + ((millisUntilFinished / 1000)) % 60));
			}

			private String addZeros(String s) {
				if (s.length() == 1) {
					s = "0" + s;
				}
				return s;
			}

			public void onFinish() {
				ActivitiesPlaan theActivies = activitiesList
						.get(activitiesListIndex);
				String body = "";
				String title = "";

				if (theActivies.type == ActivitiesPlaan.TYPE_LOOPING
						&& theActivies.getLoopLeft() != 0) {
					// incumbent running activity is TYPE_LOOPING
					if (theActivies.getCurrentState() == ActivitiesPlaan.BREAK_STATE) {
						theActivies.decreaseLoops();
						// run the activty
						body = "Do " + theActivies.name + ",for "
								+ theActivies.loopTime + " Minutes";
						title = theActivies.name + " started";
						showNotification(body, title);

					} else {
						// run the break
						body = "Break for " + theActivies.breakTime
								+ " Minutes";
						title = "Break Time Started";
						showNotification(body, title);
					}
					theActivies.alterState(theActivies.getCurrentState());
					runActivitiesTimerRecursive(theActivies);

				} else {
					// go to the next activity in the list
					activitiesListIndex++;
					theActivies = activitiesList.get(activitiesListIndex);
					// checking wether continue to recurse
					if (activitiesListIndex < activitiesList.size()) {
						body = "Do " + theActivies.name + ",for "
								+ theActivies.interval + " Minutes";
						title = theActivies.name + " started";
						showNotification(body, title);
						runActivitiesTimerRecursive(activitiesList
								.get(activitiesListIndex));
					}
				}
			}

			private void showNotification(String body, String title) {
				Intent intent = new Intent("com.example.plaan.ACDP");
				PendingIntent pi = PendingIntent.getActivity(
						getApplicationContext(), 0, intent, 0);
				Notification n = new Notification(R.drawable.plaan_icon2, body,
						System.currentTimeMillis());
				n.setLatestEventInfo(getApplicationContext(), title, body, pi);
				n.defaults = Notification.DEFAULT_ALL;
				nm.notify(notificationId, n);
			}
		}.start();
	}

	private void initializeViews() {
		tvActivitesName = (TextView) findViewById(R.id.tvActivitiesName);
		tvTimer = (TextView) findViewById(R.id.tvTimer);
		tvDONE = (TextView) findViewById(R.id.tvDONE);
		tvBreak = (TextView) findViewById(R.id.tvBreak);
		tvLoopLeft = (TextView) findViewById(R.id.tvLoopLeft);
		tvX = (TextView) findViewById(R.id.tvX);
		rlLoopInfo = (RelativeLayout) findViewById(R.id.rlLoopInfo);
	}

	private void setTypeface() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(tvActivitesName, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvTimer, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvDONE, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvBreak, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvLoopLeft, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvX, TypefacePlaan.LEAGUE_GOTHIC);
	}

	private void retreiveBundles() {
		Bundle extras = getIntent().getExtras();
		timeRemaining = extras.getInt("timeRemaining");
		activitiesList = extras.getParcelableArrayList("activitiesList");
	}
}
