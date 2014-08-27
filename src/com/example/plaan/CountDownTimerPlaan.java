package com.example.plaan;

import java.util.LinkedList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

public class CountDownTimerPlaan extends Service {
	Context context;

	private CountDownTimer activitiesCDT;
	private ActivityCard activityCardOnFocus;
	private LinkedList<ActivitiesPlaan> activitiesList;
	private ActivitiesPlaan theActivities;
	private int activitiesListIndex;
	private long before_time;

	//Notification neccessary
	NotificationManager nm;
	static final int notificationId = 1394885;
	// cd = CountDown
	NotificationCompat.Builder cdBuilder;
	// na = newActivity
	NotificationCompat.Builder naBuilder;
	int cdBuilderId = 1234;
	int naBuilderId = 4567;
	Intent scrollingUIActivity;
	PendingIntent piScrollingUIActivity;
	long runningTime;
	private boolean isTimerRunning;

	//PAUSE
	private Handler mHandler = new Handler();
	private long startTime;
	private long elapsedTime;
	private final int REFRESH_RATE = 100;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public CountDownTimerPlaan(Context ctx, NotificationManager nm) {
		activitiesList = new LinkedList<>();
		activitiesListIndex = 0;
		this.nm = nm;
		this.context = ctx;
		cdBuilder = new NotificationCompat.Builder(context);
		naBuilder = new NotificationCompat.Builder(context);
		scrollingUIActivity = new Intent(context, ScrollingUI.class);
		piScrollingUIActivity = PendingIntent.getActivity(context, 0,
				scrollingUIActivity, 0);
		isTimerRunning = false;
	}

	public LinkedList<ActivitiesPlaan> getActivitiesList() {
		return activitiesList;
	}

	public void addActivitiesPlaan(ActivitiesPlaan a) {
		activitiesList.add(a);
	}

	public void addActivitiesPlaan(int pos, ActivitiesPlaan a) {
		activitiesList.add(pos, a);
	}

	public void setActivitiesPlaan(int pos, ActivitiesPlaan a) {
		activitiesList.set(pos, a);
	}

	public void startCountDown() {
		if (mHandler != null)
			mHandler.removeCallbacks(startTimer);

		if (activitiesCDT != null)
			activitiesCDT.cancel();

		// Declare the running activity
		theActivities = activitiesList.get(activitiesListIndex);

		// Adjust the next start and/or endHours because of PAUSE elapsedTime
		if (elapsedTime > 0)
			adjustStartEndHoursAfterPause();

		// Make sure adjustStartEndHoursAfterPause only called after hitting Pause button
		elapsedTime = 0;

		// set the running time to be the activity's interval
		long time = setRunningTime(theActivities);
		before_time = time;

		// run the actual timer
		try {
			activitiesCDT = new CountDownTimer(time, 1000) {
				public void onTick(long millisUntilFinished) {
					// is the  countdown running
					isTimerRunning = true;

					runningTime = millisUntilFinished;

					// declare activitiesPlaan
					ActivitiesPlaan theActivities = activityCardOnFocus
							.getActivitiesPlaan();

					// format millisUntilFineished to be like hh:mm:ss
					int[] format = new int[3];
					formatMillis(format, millisUntilFinished);
					String s = formatStringMillis(format);

					// set count_down_text
					if (theActivities.name != "Free Time") {
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

					// Put RUNNING timer to notification bar
					spawnRuningTimerNotification(s);

				}

				private void spawnRuningTimerNotification(String s) {
					cdBuilder.setSmallIcon(R.drawable.plaan_icon_small);
					if (theActivities.type == ActivitiesPlaan.TYPE_ONE_TIME) {
						cdBuilder.setContentTitle(theActivities.name);
					} else {
						if (theActivities.getState() == ActivitiesPlaan.LOOPING_STATE) {
							cdBuilder.setContentTitle(theActivities.name + ", "
									+ theActivities.getLoopLeft()
									+ " Loop left");
						} else {
							cdBuilder.setContentTitle("Break of "
									+ theActivities.name + " , "
									+ theActivities.getLoopLeft()
									+ " Loop left");
						}
					}
					cdBuilder.setContentIntent(piScrollingUIActivity);
					cdBuilder.setContentText(s);
					nm.notify(cdBuilderId, cdBuilder.build());
				}

				private String formatStringMillis(int[] format) {
					String s = "";
					if (format[0] != 0)
						s += addZeros("" + format[0]) + ":";
					if (format[0] != 0 || format[1] != 0)
						s += addZeros("" + format[1]) + ":";
					s += addZeros("" + format[2]);
					return s;
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

					if (theActivies.type == ActivitiesPlaan.TYPE_LOOPING
							&& theActivies.getLoopLeft() != 0) {
						// incumbent running activity is TYPE_LOOPING

						// reset LoopTime and BreakTime
						theActivies.resetLoopTime();
						theActivies.resetBreakTime();

						if (theActivies.getCurrentState() == ActivitiesPlaan.BREAK_STATE) {
							// Just finish Break time
							// Will notify to Run Looping time

							// Decreasing the loops
							theActivies.decreaseLoops();

							// Decrease loopLeft text
							theActivies.getActivityCard()
									.decreaseLoopLeftText();

							// Notif to run Looping time
							cdBuilder.setTicker(theActivies.name + " , "
									+ theActivies.getLoopLeft() + " Loop left");
							showNotification("", "");

						} else {
							// Just finish Looping time
							// Will notify to Run Break  time
							cdBuilder.setTicker("Break of " + theActivies.name
									+ " , " + theActivies.getLoopLeft()
									+ " Loop left");
							showNotification("", "");
						}

						// Altering the state from Looping to break, vice versa
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

							// Set ticker with the next activities name
							cdBuilder.setTicker(nextActivities.name
									+ " Started");
							showNotification("", "");

							// recursive call
							startCountDown();
						} else {
							nm.cancel(cdBuilderId);
							nm.cancel(naBuilderId);
						}
					}
				}

				private void markFinishedActivity(ActivitiesPlaan theActivies) {
					ActivityCard finishedCard = theActivies.getActivityCard();
					finishedCard.llRootActivityCard.setAlpha(0.5f);
					if (!finishedCard.getActivitiesPlaan().name
							.equals("Free Time")) {
						finishedCard.tvCountDown.setVisibility(View.GONE);
						finishedCard.tvDONE.setVisibility(View.VISIBLE);
					}
				}

				private void showNotification(String body, String title) {
					naBuilder.setContentTitle(title);
					naBuilder.setContentText(body);
					naBuilder.setDefaults(Notification.DEFAULT_ALL);
					naBuilder.setAutoCancel(true);
					naBuilder.setTicker(title);
					nm.notify(naBuilderId, naBuilder.build());
				}

			}.start();
		} catch (Exception e) {
			Log.d("The Exception", " THE EXCEPTION -- " + e.toString());
		}

	}

	private long setRunningTime(ActivitiesPlaan theActivities2) {
		long time = theActivities.interval;
		activityCardOnFocus = theActivities.getActivityCard();
		activityCardOnFocus
				.setCountDownImageView(R.drawable.yellow_plaanish_clock);

		// If the actvity type is looping, time is either loopingTime or breakTime
		if (theActivities.getType() == ActivitiesPlaan.TYPE_LOOPING) {
			if (theActivities.getCurrentState() == ActivitiesPlaan.LOOPING_STATE) {
				activityCardOnFocus.setTvBreakVisibility(View.INVISIBLE);
				if (theActivities.getLoopLeft() == 0) {
					// If there is no loop left, there will be no time left
					time = 0;
				} else {
					// If there is loop left, time will be the loopTime
					time = theActivities.loopTime();
				}
			} else if (theActivities.getCurrentState() == ActivitiesPlaan.BREAK_STATE) {
				activityCardOnFocus
						.setCountDownImageView(R.drawable.green_play_clock);
				time = theActivities.breakTime();
			}
		}
		return time;
	}

	private void adjustStartEndHoursAfterPause() {
		int[] format = new int[3];
		formatMillis(format, elapsedTime);
		int i = activitiesListIndex;
		while (i < activitiesList.size()) {
			ActivitiesPlaan theActivitiesPlaan = activitiesList.get(i);
			if (i != activitiesListIndex) {
				theActivitiesPlaan.addStartTime(elapsedTime);
			}
			theActivitiesPlaan.addEndTime(elapsedTime);
			updateStartEndTimeTextViews(theActivitiesPlaan);
			i++;
		}
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

	public boolean isCardCountDownRunning(ActivityCard ac) {
		return isTimerRunning
				&& ac.getActivitiesPlaan().equals(
						activityCardOnFocus.getActivitiesPlaan());
	}

	// ar: after recalibration
	public void addTime(long millisExtra, ActivitiesPlaan arActivities) {

		// Cancel the current countdown time to make a new one.
		activitiesCDT.cancel();

		// NOTE : ActivitesPlaan object equals method compare the position of the card in scroling UI.
		int i = activitiesList.indexOf(arActivities);

		long intervalDifference = arActivities.getInterval() - before_time;

		// the new interval will be the difference between old interval and new interval + the running time
		arActivities.setInterval(intervalDifference + runningTime);
		activityCardOnFocus.setActivitiesPlaan(arActivities);
		setActivitiesPlaan(i, arActivities);

		startCountDown();
	}
}
