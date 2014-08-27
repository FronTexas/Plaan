package com.example.plaan;

import java.util.LinkedList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;

public class CountDownTimerPlaan {
	Context context;

	private CountDownTimer activitiesCDT;
	private ActivityCard activityCardOnFocus;
	private LinkedList<ActivitiesPlaan> activitiesList;
	private ActivitiesPlaan theActivities;
	private int activitiesListIndex;

	//Notification
	NotificationManager nm;
	static final int notificationId = 1394885;

	//PAUSE
	private Handler mHandler = new Handler();
	private long startTime;
	private long stopTime;
	private long elapsedTime;
	private final int REFRESH_RATE = 100;

	public CountDownTimerPlaan(Context ctx, NotificationManager nm) {
		activitiesList = new LinkedList<>();
		activitiesListIndex = 0;
		this.nm = nm;
		this.context = ctx;
	}

	public void addActivitiesPlaan(ActivitiesPlaan a) {
		activitiesList.add(a);
	}

	public void addActivitiesPlaan(int pos, ActivitiesPlaan a) {
		activitiesList.add(pos, a);
	}

	public void startCountDown() {
		if (mHandler != null)
			mHandler.removeCallbacks(startTimer);

		if (activitiesCDT != null)
			activitiesCDT.cancel();

		// Declare the running activity
		theActivities = activitiesList.get(activitiesListIndex);

		// Adjust the next start and/or endHours because of PAUSE elapsedTime
		int[] format = new int[3];
		formatMillis(format, elapsedTime);
		int i = activitiesListIndex;
		while (i < activitiesList.size()) {
			ActivitiesPlaan theActivitiesPlaan = activitiesList.get(i);
			if (i != activitiesListIndex) {
				theActivitiesPlaan.addStartTime(format[0], format[1]);
			}
			theActivitiesPlaan.addEndTime(format[0], format[1]);
			updateStartEndTimeTextViews(theActivitiesPlaan);
			i++;
		}

		// set the running time to be the activity's interval
		long time = theActivities.interval;
		activityCardOnFocus = theActivities.getActivityCard();

		// If the actvity type is looping, time is either loopingTime or breakTime
		if (theActivities.getType() == ActivitiesPlaan.TYPE_LOOPING) {
			// set looping layout
			// break or looping
			if (theActivities.getCurrentState() == ActivitiesPlaan.LOOPING_STATE) {
				activityCardOnFocus.setTvBreakVisibility(View.INVISIBLE);
				time = theActivities.loopTime();
			} else if (theActivities.getCurrentState() == ActivitiesPlaan.BREAK_STATE) {
				activityCardOnFocus.setTvBreakVisibility(View.VISIBLE);
				time = theActivities.breakTime();
			}
		}

		// run the actual timer
		activitiesCDT = new CountDownTimer(time, 1000) {
			public void onTick(long millisUntilFinished) {

				// declare activitiesPlaan
				ActivitiesPlaan theActivities = activityCardOnFocus
						.getActivitiesPlaan();

				// formatMillis to be like hh:mm:ss
				int[] format = new int[3];
				formatMillis(format, millisUntilFinished);
				String s = "";
				if (format[0] != 0)
					s += addZeros("" + format[0]) + ":";
				if (format[0] != 0 || format[1] != 0)
					s += addZeros("" + format[1]) + ":";
				s += addZeros("" + format[2]);

				if (theActivities.name != "Free Time") {
					// set count_down_text
					activityCardOnFocus.setTvCountDownText(s, true);
				} else {
					activityCardOnFocus.setTvCountDownText(s, false);
				}

				// update interval / looptime / breaktime
				if (theActivities.getType() == ActivitiesPlaan.TYPE_ONE_TIME)
					theActivities.setInterval(millisUntilFinished);
				else if (theActivities.getType() == ActivitiesPlaan.TYPE_LOOPING) {
					if (theActivities.getState() == ActivitiesPlaan.BREAK_STATE) {
						theActivities.breakTime = millisUntilFinished;
					} else {
						theActivities.loopTime = millisUntilFinished;
					}
				}

				//				String body = s;
				//				String title = theActivities.name;
				//								showNotification(body, title);

				//				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				//						context).setSmallIcon(R.drawable.plaan_icon2)
				//						.setContentTitle("AA").setContentText(s);
				//				nm.notify(1, mBuilder.build());

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

					// reset LoopTime and BreakTime
					theActivies.resetLoopTime();
					theActivies.resetBreakTime();

					if (theActivies.getCurrentState() == ActivitiesPlaan.BREAK_STATE) {
						theActivies.decreaseLoops();
						// notif run the next loop
						int[] format = new int[3];
						formatMillis(format, theActivies.loopTime);
						String hours = addZeros("" + format[0]);
						String minutes = addZeros("" + format[1]);
						String seconds = addZeros("" + format[2]);
						String time = hours + ":" + minutes + ":" + seconds;

						// Notification
						body = "For " + time;
						title = theActivies.name + " started";
						int loopingFreq = Integer.parseInt(theActivies
								.getActivityCard().tvLoopingFreqCD.getText()
								.toString());
						theActivies.getActivityCard().tvLoopingFreqCD
								.setText("" + (loopingFreq - 1));
						showNotification(body, title);

					} else {
						// notif run the break
						int[] format = new int[3];
						formatMillis(format, theActivies.breakTime);
						String hours = addZeros("" + format[0]);
						String minutes = addZeros("" + format[1]);
						String seconds = addZeros("" + format[2]);
						String time = hours + ":" + minutes + ":" + seconds;

						// Notification
						body = "Break for " + time;
						title = "Break Time Started";
						showNotification(body, title);
					}
					theActivies.alterState(theActivies.getCurrentState());

					// recursive call
					startCountDown();

				} else {
					// go to the next activity in the list
					markFinishedActivity(theActivies);
					activitiesListIndex++;
					if (activitiesListIndex < activitiesList.size()) {
						// (if) checking wether continue to recurse
						ActivitiesPlaan nextActivities = activitiesList
								.get(activitiesListIndex);
						int[] format = new int[3];
						formatMillis(format, nextActivities.interval);
						String hours = addZeros("" + format[0]);
						String minutes = addZeros("" + format[1]);
						String seconds = addZeros("" + format[2]);
						String time = hours + ":" + minutes + ":" + seconds;

						// Notification
						body = nextActivities.name + " For " + time;
						title = nextActivities.name + " started";
						showNotification(body, title);

						// recursive call
						startCountDown();
					}
				}
			}

			private void markFinishedActivity(ActivitiesPlaan theActivies) {
				ActivityCard finishedCard = theActivies.getActivityCard();
				finishedCard.llRootActivityCard.setAlpha(0.5f);
				if (!finishedCard.getActivitiesPlaan().name.equals("Free Time")) {
					finishedCard.tvCountDown.setVisibility(View.GONE);
					finishedCard.tvDONE.setVisibility(View.VISIBLE);
				}
			}

			private void showNotification(String body, String title) {
				Intent intent = new Intent(context, ScrollingUI.class);
				PendingIntent pi = PendingIntent.getActivity(context, 0,
						intent, 0);
				Notification n = new Notification(R.drawable.plaan_icon2, body,
						System.currentTimeMillis());
				//				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				//						context).setSmallIcon(R.drawable.plaan_icon2)
				//						.setContentTitle(title).setContentText(body);
				n.setLatestEventInfo(context, title, body, pi);
				n.defaults = Notification.DEFAULT_ALL;
				n.flags |= Notification.FLAG_AUTO_CANCEL;
				nm.notify(notificationId, n);
			}
		}.start();

	}

	public void pause() {
		activitiesCDT.cancel();
		startTime = System.currentTimeMillis();
		mHandler.removeCallbacks(startTimer);
		mHandler.postDelayed(startTimer, 0);
	}

	private void updateStartEndTimeTextViews(ActivitiesPlaan theActivitiesPlaan) {
		String start = theActivitiesPlaan.getStartTime();
		String end = theActivitiesPlaan.getEndTime();
		theActivitiesPlaan.getActivityCard().tvStartEndCD.setText(start + " - "
				+ end);
	}

	private Runnable startTimer = new Runnable() {
		public void run() {
			elapsedTime = System.currentTimeMillis() - startTime;
			int[] format = new int[3];
			formatMillis(format, elapsedTime);
			if (activityCardOnFocus.getActivitiesPlaan().name != "Free Time")
				activityCardOnFocus.setTvCountDownText(addZeros("" + format[0])
						+ ":" + addZeros("" + format[1]) + ":"
						+ addZeros("" + format[2]), true);
			else
				activityCardOnFocus.setTvCountDownText(addZeros("" + format[0])
						+ ":" + addZeros("" + format[1]) + ":"
						+ addZeros("" + format[2]), false);
			mHandler.postDelayed(this, REFRESH_RATE);
		}
	};

	private String addZeros(String s) {
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	private void formatMillis(int[] format, long millisUntilFinished) {
		// format[0] = hours, format[1] = minutes , format[2] = seconds
		format[0] = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
		format[1] = (int) ((millisUntilFinished / (1000 * 60)) % 60);
		format[2] = (int) (millisUntilFinished / 1000) % 60;
	}

	public ActivityCard getActivityCardOnFocus() {
		return activityCardOnFocus;
	}

}
