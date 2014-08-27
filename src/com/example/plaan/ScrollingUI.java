package com.example.plaan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.plaan.ActivityCard.onSetClickListener;
import com.zenkun.datetimepicker.time.RadialPickerLayout;
import com.zenkun.datetimepicker.time.TimePickerDialog;
import com.zenkun.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class ScrollingUI extends FragmentActivity implements OnClickListener,
		OnTimeSetListener {

	private int CARD_WIDTH;
	private int CARD_HEIGHT;

	ScrollView svScrollingUI;
	TypefacePlaan tfp;

	// SleepingCard
	RelativeLayout rlLayoutSleepingCard, rlAvailableTimeClock, rlSleepingCard,
			rlSleepingCloudGroup, rlYellowClock;
	ImageView ivAvailableTimeClock, ivBlueTimeFrame, ivSleepingCloud;
	TextView tvAvailableTime, tvSLEEP, tvSetSleepTime;

	// ActivityCard
	LinearLayout llLayoutActivityCard;
	final int AC_ID_HEADER = 224;
	int acIDCounter;
	LinkedList<ActivitiesPlaan> activitiesList;
	LinkedList<ActivityCard> activityCardList;
	int activitiesListIndex;
	int LAYOUT_POSITION;
	final int TAG_HEADER = 276;
	int tag_counter;
	static final int notificationId = 1394885;

	// CountDown
	CountDownTimerPlaan cdtPlaan;

	// freeTimeCard
	private final int FTC_ID_HEADER = 457;
	int ftc_id_counter;
	LinkedList<FreeTimeCard> ftcList;

	// plus_button
	ImageView ivPlusButton;

	// start_button
	RelativeLayout rlSTARTButton;
	TextView tvSTARTButton;

	int clickedActivityType;
	public static TimePickerDialog timePickerDialog;

	float scaleDP;

	// long timeRemaining;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrolling_ui_main);
		tfp = new TypefacePlaan(getApplicationContext());

		scaleDP = getApplicationContext().getResources().getDisplayMetrics().density;
		CARD_WIDTH = (int) (300 * scaleDP + 0.5f);
		CARD_HEIGHT = (int) (375 * scaleDP + 0.5f);
		acIDCounter = 0;
		activitiesList = new LinkedList<>();
		activityCardList = new LinkedList<>();
		LAYOUT_POSITION = 0;
		ftc_id_counter = 0;
		tag_counter = 0;
		ftcList = new LinkedList<FreeTimeCard>();
		cdtPlaan = new CountDownTimerPlaan(getApplicationContext(),
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE));

		activitiesListIndex = 0;

		initializeViews();
		setTypefaces();

		// sleepCard
		tvSetSleepTime.setOnClickListener(this);
		Animation comeIn = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.down_from_top_w_alpha);
		rlSleepingCard.setAnimation(comeIn);

		// plusButton
		ivPlusButton.setOnClickListener(this);

		// startButton
		rlSTARTButton.setOnClickListener(this);

	}

	private void setTypefaces() {
		tfp.setTypeface(tvAvailableTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvSLEEP, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvSetSleepTime, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvSTARTButton, TypefacePlaan.LEAGUE_GOTHIC);
	}

	private void initializeViews() {
		svScrollingUI = (ScrollView) findViewById(R.id.svScrollingUI);

		// SleepingCard
		llLayoutActivityCard = (LinearLayout) findViewById(R.id.llLayoutActivityCard);
		rlLayoutSleepingCard = (RelativeLayout) findViewById(R.id.rlLayoutSleepingCard);
		rlSleepingCard = (RelativeLayout) findViewById(R.id.rlSleepingCard);
		rlSleepingCloudGroup = (RelativeLayout) findViewById(R.id.rlSleepingCloudGroup);

		ivSleepingCloud = (ImageView) findViewById(R.id.ivSleepingCloud);
		tvSLEEP = (TextView) findViewById(R.id.tvSLEEP);
		tvSetSleepTime = (TextView) findViewById(R.id.tvSetSleepTime);
		tvAvailableTime = (TextView) findViewById(R.id.tvAvailableTime);

		// plusButton
		ivPlusButton = (ImageView) findViewById(R.id.ivPlusButton);
		ivPlusButton.setImageResource(R.drawable.blue_plus_button_selector);

		// startButton
		rlSTARTButton = (RelativeLayout) findViewById(R.id.rlStartButton);
		rlSTARTButton
				.setBackgroundResource(R.drawable.start_yellow_button_selector);
		tvSTARTButton = (TextView) findViewById(R.id.tvSTARTButton);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == tvSetSleepTime.getId()) {
			// SET_SLEEPTIME
			timePickerDialog = TimePickerDialog.newInstance(this, 0, 0, false);
			timePickerDialog.show(getSupportFragmentManager(), "timepicker");
		} else if (v.getId() == R.id.ivPlusButton) {
			// PLUS_BUTTON -- ADD_ACTIVITY
			LayoutParams lp_NewAc = new LayoutParams(CARD_WIDTH, CARD_HEIGHT);
			lp_NewAc.weight = 1.0f;
			lp_NewAc.gravity = Gravity.CENTER_HORIZONTAL;
			inflateActivityCard(lp_NewAc);
		} else if (v.getId() == rlSTARTButton.getId()) {
			// START_BUTTON OR PAUSE_BUTTON OR RESUME_BUTTON
			if (tvSTARTButton.getText().toString().equals("START")) {
				// START_BUTTON
				cdtPlaan.startCountDown();
				cdtPlaan.getActivityCardOnFocus().setCountDownImageView(
						R.drawable.yellow_plaanish_clock);
				tvSTARTButton.setText("PAUSE");
			} else if (tvSTARTButton.getText().toString().equals("PAUSE")) {
				// PAUSE_BUTTON
				cdtPlaan.pause();
				cdtPlaan.getActivityCardOnFocus().setCountDownImageView(
						R.drawable.blue_plaan_clock);
				tvSTARTButton.setText("RESUME");
			} else if (tvSTARTButton.getText().toString().equals("RESUME")) {
				// RESUME_BUTTON
				// resume the cdtPlaan
				if (cdtPlaan.getActivityCardOnFocus().getActivitiesPlaan()
						.getType() == ActivitiesPlaan.TYPE_LOOPING
						&& cdtPlaan.getActivityCardOnFocus()
								.getActivitiesPlaan().state == ActivitiesPlaan.BREAK_STATE) {
					// if Looping AND is in the break state, the countdown image is green
					cdtPlaan.getActivityCardOnFocus().setCountDownImageView(
							R.drawable.green_play_clock);
				} else {
					// else is yellow
					cdtPlaan.getActivityCardOnFocus().setCountDownImageView(
							R.drawable.yellow_plaanish_clock);
				}
				cdtPlaan.startCountDown();
				tvSTARTButton.setText("PAUSE");
			}
		}

	}

	@Override
	public void onTimeSet(RadialPickerLayout v, int hourOfDay, int minute) {

		tvSetSleepTime.setText(addZeros("" + hourOfDay) + ":"
				+ addZeros("" + minute));

		// timeRemaining is in minutes
		// timeRemaining = calculateTimeRemaining(hourOfDay, minute);

		// inflate the activityCard
		LayoutParams lp_ActivityCard = new LayoutParams(CARD_WIDTH, CARD_HEIGHT);
		lp_ActivityCard.weight = 1.0f;
		lp_ActivityCard.gravity = Gravity.CENTER_HORIZONTAL;
		lp_ActivityCard.setMargins(0, (int) (10 * scaleDP + 0.5f), 0, 0);
		inflateActivityCard(lp_ActivityCard);

		ivPlusButton.setVisibility(View.VISIBLE);
		rlSTARTButton.setVisibility(View.VISIBLE);

	}

	private void inflateActivityCard(LayoutParams params) {
		// Initalize(inflate) the ActivityCard
		ActivityCard theCard = null;
		if (LAYOUT_POSITION - 1 >= 0) {
			// If the card above is exist
			// To access previous activities endTime
			// And set it to theCard startTime
			ActivityCard aboveTheCard = (ActivityCard) llLayoutActivityCard
					.getChildAt(LAYOUT_POSITION - 1);
			ActivitiesPlaan aboveActivitiesPlaan = aboveTheCard
					.getActivitiesPlaan();
			theCard = new ActivityCard(this, getSupportFragmentManager(),
					getLayoutInflater(), Integer.parseInt("" + TAG_HEADER
							+ tag_counter), svScrollingUI,
					aboveActivitiesPlaan.getEndTime());
		} else {
			// If there is no card above
			theCard = new ActivityCard(this, getSupportFragmentManager(),
					getLayoutInflater(), Integer.parseInt("" + TAG_HEADER
							+ tag_counter), svScrollingUI);
		}
		tag_counter++;
		theCard.setId(Integer.parseInt("" + AC_ID_HEADER + acIDCounter));
		activityCardList.add(theCard);

		// Animation for theCard
		Animation comeIn = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.down_from_top_w_alpha);
		comeIn.setFillAfter(true);
		theCard.setAnimation(comeIn);

		// Add theCard(UNSET) TO llActivityCard(just above sleepingCard)
		llLayoutActivityCard
				.addView(
						theCard,
						llLayoutActivityCard.indexOfChild(rlLayoutSleepingCard),
						params);

		// request focus to new card
		Rect rectangle = new Rect(rlLayoutSleepingCard.getLeft(),
				rlLayoutSleepingCard.getTop(), rlLayoutSleepingCard.getRight(),
				rlLayoutSleepingCard.getBottom());
		svScrollingUI.requestChildRectangleOnScreen(theCard, rectangle, false);

		// If the card below is exist
		if (LAYOUT_POSITION + 1 < llLayoutActivityCard.getChildCount()) {
			// Animation for BELOW theCard
			Animation comeInWoAlpha = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.down_from_top_wt_alpha);
			View belowTheCard = llLayoutActivityCard
					.getChildAt(LAYOUT_POSITION + 1);
			belowTheCard.setAnimation(comeInWoAlpha);
		}

		// listen for a SET click from activityCard
		theCard.setOnSetClickListener(new onSetClickListener() {
			@Override
			public void onSetClick(ActivitiesPlaan theActivity,
					boolean firstTimeVisit, ActivityCard theCard) {
				if (!activitiesList.contains(theActivity)) {
					// If the activity is new
					if (LAYOUT_POSITION - 1 >= 0) {
						// if the card above is already exist
						ActivityCard aboveCard = (ActivityCard) llLayoutActivityCard
								.getChildAt(activitiesList.size() - 1);
						String aboveEndTime = aboveCard.getActivitiesPlaan()
								.getEndTime();
						String theActivityStartTime = theActivity
								.getStartTime();

						int aboveEndTimeINT = Integer.parseInt(aboveEndTime
								.substring(0, 2) + aboveEndTime.substring(3));
						int theActivityStartTimeINT = Integer
								.parseInt(theActivityStartTime.substring(0, 2)
										+ theActivityStartTime.substring(3));

						if (theActivityStartTimeINT - aboveEndTimeINT >= 0) {
							// Will add to the last activity just before sleep
							// No need to break through FreeTimeCards
							activitiesList.add(theActivity);
							cdtPlaan.addActivitiesPlaan(theActivity);
							// // timeRemaining = millisLeft;
							if (activitiesList.size() - 2 >= 0) {
								setUpFreTimeCard(activitiesList
										.get(activitiesList.size() - 2),
										activitiesList.getLast(),
										activitiesList.size() - 1);
							}
							LAYOUT_POSITION++;

						} else {
							// if the activity is use up freeTime
							int acListIndex = 0;
							boolean found = false;

							// loop through freeTimeCard
							while (!found
									&& acListIndex < activitiesList.size()) {
								ActivitiesPlaan theFreeTimeActivities = activitiesList
										.get(acListIndex);
								if (theFreeTimeActivities.name
										.equals("Free Time")) {
									FreeTimeCard theFreeTimeCard = (FreeTimeCard) theFreeTimeActivities
											.getActivityCard();
									if (calculateTimeDifference(
											theFreeTimeActivities
													.getStartTime(),
											theActivity.getStartTime()) <= 0) {
										// if theActivity.startTime is after freeTime startTime
										if (calculateTimeDifference(
												theFreeTimeActivities
														.getEndTime(),
												theActivity.getEndTime()) >= 0) {
											// if theActivity.startTime is before freeTime.endTime
											// It's possible to break the current freeTime

											// Change the current freeTimeCard info
											found = true;
											theFreeTimeActivities.setInterval(
													theFreeTimeActivities
															.getStartTime(),
													theActivity.getStartTime());
											long timeDuration = theFreeTimeActivities
													.getInterval() * 60000;
											theFreeTimeCard.setFreeTimeCardTextViews(
													theFreeTimeActivities
															.getStartTime(),
													theActivity.getStartTime(),
													timeDuration);

											// add new activity
											llLayoutActivityCard
													.removeView(theCard);
											int indexOfFtc = llLayoutActivityCard
													.indexOfChild(theFreeTimeCard);

											llLayoutActivityCard.addView(
													theCard, indexOfFtc + 1);
											activitiesList.add(acListIndex + 1,
													theActivity);
											cdtPlaan.addActivitiesPlaan(
													acListIndex + 1,
													theActivity);
											// add new freeTime, if neccesary
											ActivityCard otherCard = (ActivityCard) llLayoutActivityCard
													.getChildAt(indexOfFtc + 2);
											setUpFreTimeCard(
													theCard.getActivitiesPlaan(),
													otherCard
															.getActivitiesPlaan(),
													acListIndex + 2);
										}

									}
								}

								acListIndex++;
							}

						}
					} else {
						// if its the first activity.
						activitiesList.add(theActivity);
						cdtPlaan.addActivitiesPlaan(theActivity);
						LAYOUT_POSITION++;
					}
				} else {
					// if its an old activity

					// NOTE : ActivitesPlaan object equals method compare the position of the card in scroling UI.
					int i = activitiesList.indexOf(theActivity);

					// br = before Recalibration
					ActivitiesPlaan brActiviies = activitiesList.get(i);

					// Update all NEXT activities start & endTime
					adjustStartEndHoursAfterChange(
							calculateExactTimeDifference(
									brActiviies.getEndTime(),
									theActivity.getEndTime()), i);

					// will update it's activity information in the list
					activitiesList.set(i, theActivity);

					if (cdtPlaan.isCardCountDownRunning(theActivity
							.getActivityCard())) {
						// If the timer already running, it will add the running time
						// to this particular activity
						cdtPlaan.addTime(theActivity.getInterval(), theActivity);
					} else
						cdtPlaan.setActivitiesPlaan(i, theActivity);
				}

			}

			/**
			 * Will calculate exact time difference between earliearTime and
			 * laterTime in millisSecond
			 * 
			 * @param earlierTime
			 *            The earlier time to compare.(xx:xx)
			 * @param laterTime
			 *            The later time to compare.(xx:xx)
			 * @return Difference between earlierTime and laterTime in
			 *         millisSecond
			 * **/
			@SuppressLint("SimpleDateFormat")
			private long calculateExactTimeDifference(String earlierTime,
					String laterTime) {
				Date d1 = null;
				Date d2 = null;
				try {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					d1 = format.parse(earlierTime);
					d2 = format.parse(laterTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return d2.getTime() - d1.getTime();
			}

			/**
			 * 
			 * Will parse time1 and time2 into literal String, and
			 * take the difference.
			 * Example : time1 = 10:00 , time2 = 9:30 , returned =
			 * 1000-930 = 70
			 * 
			 * @param time1
			 *            Earlier time
			 * @param time2
			 *            Later time
			 * **/
			private int calculateTimeDifference(String time1, String time2) {
				int startTimeINT = Integer.parseInt(time1.substring(0, 2)
						+ time1.substring(3));
				int startTime2INT = Integer.parseInt(time2.substring(0, 2)
						+ time2.substring(3));
				return startTimeINT - startTime2INT;
			}

			private void setUpFreTimeCard(ActivitiesPlaan activityAbove,
					ActivitiesPlaan currentActivity, int posOnActivitiesList) {
				// ActivitiesList lastIndex
				int lastIndex = activitiesList.size() - 1;

				// Inflate freeTimeCard if neccesary
				if (lastIndex > 0 // make sure sleepcard is instantiated
						&& !activityAbove.getEndTime().equals(
								currentActivity.getStartTime())) {
					// Inflate the freeTimeCard
					LayoutParams ftcParams = new LayoutParams(CARD_WIDTH,
							CARD_HEIGHT);
					ftcParams.weight = 1.0f;
					ftcParams.gravity = Gravity.CENTER_HORIZONTAL;
					ftcParams.setMargins(0, (int) (10 * scaleDP + 0.5f), 0, 0);
					FreeTimeCard ftc = inflateFreeTimeCard(currentActivity,
							ftcParams, activityAbove.getEndTime(),
							currentActivity.getStartTime());

					// Setting freeTime activity
					activitiesList.add(posOnActivitiesList,
							ftc.getActivitiesPlaan());
					cdtPlaan.addActivitiesPlaan(posOnActivitiesList,
							ftc.getActivitiesPlaan());
				}
			}

			private void adjustStartEndHoursAfterChange(long elapsedTime,
					int index) {
				LinkedList<ActivitiesPlaan> activitiesList = cdtPlaan
						.getActivitiesList();
				int i = index;
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

			private void updateStartEndTimeTextViews(
					ActivitiesPlaan theActivitiesPlaan) {
				String start = theActivitiesPlaan.getStartTime();
				String end = theActivitiesPlaan.getEndTime();
				theActivitiesPlaan.getActivityCard().tvStartEndCD.setText(start
						+ " - " + end);
			}
		});
		acIDCounter++;

	}

	private FreeTimeCard inflateFreeTimeCard(ActivitiesPlaan currentActivity,
			LayoutParams ftcParams, String startTime, String endTime) {
		// Constructor also make theActivitiesPlaan of freeTimeCard
		FreeTimeCard ftc = new FreeTimeCard(getApplicationContext(), startTime,
				endTime, svScrollingUI);
		Animation comeIn = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.down_from_top_w_alpha);
		ftc.setAnimation(comeIn);
		int ftc_LAYOUT_POSITION = llLayoutActivityCard
				.indexOfChild(currentActivity.getActivityCard());
		llLayoutActivityCard.addView(ftc, ftc_LAYOUT_POSITION, ftcParams);
		ftc.setId(Integer.parseInt(FTC_ID_HEADER + "" + ftc_id_counter));
		ftc_id_counter++;
		// If the card below is exist
		if (ftc_LAYOUT_POSITION + 1 < llLayoutActivityCard.getChildCount()) {
			// Animation for BELOW theCard
			Animation comeInWoAlpha = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.down_from_top_wt_alpha);
			View belowTheCard = llLayoutActivityCard
					.getChildAt(LAYOUT_POSITION + 1);
			belowTheCard.setAnimation(comeInWoAlpha);

		}

		LAYOUT_POSITION++;
		ftcList.add(ftc);

		return ftc;
	}

	private String addZeros(String s) {
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	@Override
	public void onBackPressed() {
		Intent backtoHome = new Intent(Intent.ACTION_MAIN);
		backtoHome.addCategory(Intent.CATEGORY_HOME);
		backtoHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(backtoHome);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		// Restoring view hierarchy
		super.onSaveInstanceState(outState);
	}

}
