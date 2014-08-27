package com.example.plaan;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.plaan.PlaanGeneralClock.GeneralClockListener;

public class SetTimePage extends Activity implements OnClickListener,
		OnEditorActionListener, OnItemSelectedListener {
	
	TextView tvOneTime;
	TextView tvRepetitive;
	TextView tvActivitiesName;
	ImageView ivSetTimeBox;
	ImageButton ibUncheckedOTBox;
	ImageButton ibUncheckedRBox;
	LinearLayout llOneTime;
	TextView tvHoursStart;
	TextView tvMinutesStart;
	TextView tvHoursEnd;
	TextView tvMinutesEnd;
	TextView tvTimeRemaining;
	TextView tvHoursR;
	TextView tvMinutesR;
	TextView tvStart;
	TextView tvLoops;
	EditText etLoops;
	TextView tvLoopTime;
	EditText etLoopTime;
	EditText etLoopTimeType;
	TextView tvBreak;
	EditText etBreak;
	EditText etBreakType;
	TextView tvX;
	ImageButton ibNextButtonBreak;
	ImageView ivArrowForward;
	ImageView ivArrowBack;
	RelativeLayout rlMainContent;
	TextView tvDONE;
	TextView tvEnd;
	TextView tvEndHoursR;
	TextView tvEndMinutesR;
	LinearLayout llEndTimeR;

	Spinner spinMeridiemStart;
	Spinner spinMeridiemEnd;

	Animation animSlideIn;
	Animation animSlideOut;

	final String[] spinnerMeridiemValues = { "AM", "PM" };

	LinearLayout llRepetitive;

	int clickedButton;
	boolean itemSelected;

	TextView tvToday, tvTodaysDate, tvActivityMinutes, tvMin;
	float scaleDP;
	int timeRemaining;
	ArrayList<ActivitiesPlaan> activitiesList;
	int activitiesListIndex;
	ActivitiesPlaan activitiesOnFocus;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_time_page);
		activitiesListIndex = 0;
		clickedButton = 0;
		itemSelected = false;
		animSlideIn = AnimationUtils.loadAnimation(getApplicationContext(),
				android.R.anim.slide_in_left);
		animSlideOut = AnimationUtils.loadAnimation(getApplicationContext(),
				android.R.anim.slide_out_right);
		scaleDP = getApplicationContext().getResources().getDisplayMetrics().density;

		initializeViews();
		setDates();
		setTypeface();
		retreiveBundles();

		// activate the activities that will be edited
		activitiesOnFocus = activitiesList.get(activitiesListIndex);

		tvActivitiesName.setText(activitiesOnFocus.name);
		tvTimeRemaining.setText("" + timeRemaining);

		if (activitiesListIndex == activitiesList.size() - 1)
			ivArrowForward.setVisibility(View.GONE);

		// ibNextButton.setOnClickListener(this);
		ibNextButtonBreak.setOnClickListener(this);

		ibUncheckedOTBox.setOnClickListener(this);
		ibUncheckedRBox.setOnClickListener(this);

		tvOneTime.setOnClickListener(this);
		tvRepetitive.setOnClickListener(this);

		tvHoursStart.setOnClickListener(this);
		tvMinutesStart.setOnClickListener(this);
		tvHoursEnd.setOnClickListener(this);
		tvMinutesEnd.setOnClickListener(this);

		tvHoursR.setOnClickListener(this);
		tvMinutesR.setOnClickListener(this);

		ivArrowForward.setOnClickListener(this);
		ivArrowBack.setOnClickListener(this);

		tvDONE.setOnClickListener(this);

		// etLoops.setOnEditorActionListener(this);
		etLoopTime.setOnEditorActionListener(this);
		etLoopTimeType.setOnEditorActionListener(this);

		etBreak.setOnEditorActionListener(this);
		etBreakType.setOnEditorActionListener(this);

	}

	private void retreiveBundles() {
		Bundle extras = getIntent().getExtras();
		timeRemaining = extras.getInt("timeRemaining");
		activitiesList = extras.getParcelableArrayList("activitiesList");
	}

	private void initializeViews() {
		tvToday = (TextView) findViewById(R.id.tvToday);
		tvTodaysDate = (TextView) findViewById(R.id.tvTodaysDate);
		tvOneTime = (TextView) findViewById(R.id.tvOneTime);
		tvRepetitive = (TextView) findViewById(R.id.tvRepetitive);
		tvActivitiesName = (TextView) findViewById(R.id.tvActivitiesName);
		ivSetTimeBox = (ImageView) findViewById(R.id.ivSetTimeBox);
		ibUncheckedOTBox = (ImageButton) findViewById(R.id.ibUncheckedOneTimeBox);
		ibUncheckedRBox = (ImageButton) findViewById(R.id.ibUncheckedRepetitiveBox);
		tvHoursStart = (TextView) findViewById(R.id.tvHoursStart);
		tvMinutesStart = (TextView) findViewById(R.id.tvMinutesStart);
		llOneTime = (LinearLayout) findViewById(R.id.llOneTime);
		tvHoursEnd = (TextView) findViewById(R.id.tvHoursEnd);
		tvMinutesEnd = (TextView) findViewById(R.id.tvMinutesEnd);
		tvActivityMinutes = (TextView) findViewById(R.id.tvActivityMinutes);
		tvTimeRemaining = (TextView) findViewById(R.id.tvTimeRemaining);
		tvMin = (TextView) findViewById(R.id.tvMin);
		tvHoursR = (TextView) findViewById(R.id.tvHoursR);
		tvMinutesR = (TextView) findViewById(R.id.tvMinutesR);
		tvStart = (TextView) findViewById(R.id.tvStart);
		tvLoops = (TextView) findViewById(R.id.tvLoops);
		tvLoopTime = (TextView) findViewById(R.id.tvLoopTime);
		tvBreak = (TextView) findViewById(R.id.tvBreak);
		llRepetitive = (LinearLayout) findViewById(R.id.llRepetitive);
		etLoops = (EditText) findViewById(R.id.etLoops);
		etLoopTime = (EditText) findViewById(R.id.etLoopTime);
		etLoopTimeType = (EditText) findViewById(R.id.etLoopTimeType);
		etBreak = (EditText) findViewById(R.id.etBreak);
		etBreakType = (EditText) findViewById(R.id.etBreakType);
		tvX = (TextView) findViewById(R.id.tvX);
		ibNextButtonBreak = (ImageButton) findViewById(R.id.ibNextButtonBreak);
		ivArrowForward = (ImageView) findViewById(R.id.ivArrowForward);
		ivArrowBack = (ImageView) findViewById(R.id.ivArrowBack);
		rlMainContent = (RelativeLayout) findViewById(R.id.rlMainContent);
		tvDONE = (TextView) findViewById(R.id.tvDONE);
		tvEnd = (TextView) findViewById(R.id.tvEnd);
		tvEndHoursR = (TextView) findViewById(R.id.tvEndHoursR);
		tvEndMinutesR = (TextView) findViewById(R.id.tvEndMinutesR);
		llEndTimeR = (LinearLayout) findViewById(R.id.llEndTimeR);
	}

	private void setDates() {
		CalendarSetter cs = new CalendarSetter();
		cs.setCalendar(tvToday, tvTodaysDate);
	}

	private void setTypeface() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(tvToday, tfp.TITILIUM);
		tfp.setTypeface(tvTodaysDate, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvOneTime, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvRepetitive, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvActivitiesName, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvHoursStart, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMinutesStart, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvHoursEnd, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMinutesEnd, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvActivityMinutes, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvTimeRemaining, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMin, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvHoursR, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMinutesR, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvStart, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvLoops, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvLoopTime, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvBreak, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(etLoops, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvX, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(etLoopTime, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(etLoopTimeType, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(etBreak, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(etBreakType, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvDONE, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEnd, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEndHoursR, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEndMinutesR, tfp.LEAGUE_GOTHIC);
	}

	@Override
	public void onClick(View v) {
		PlaanGeneralClock pgc = new PlaanGeneralClock(this,
				activitiesOnFocus.name);
		Handler handler = new Handler();

		switch (v.getId()) {
		case (R.id.tvHoursStart):
		case (R.id.tvMinutesStart):
			pgc.show();
			pgc.setDialogResult(new GeneralClockListener() {
				@Override
				public void finish(String result) {
					SetTimePage.this.tvHoursStart.setText("" + result.charAt(0)
							+ result.charAt(1));
					SetTimePage.this.tvMinutesStart.setText(""
							+ result.charAt(2) + result.charAt(3));
				}
			});
			break;

		case (R.id.tvHoursEnd):
		case (R.id.tvMinutesEnd):
			pgc.show();
			pgc.setDialogResult(new GeneralClockListener() {
				@Override
				public void finish(String result) {
					SetTimePage.this.tvHoursEnd.setText("" + result.charAt(0)
							+ result.charAt(1));
					SetTimePage.this.tvMinutesEnd.setText("" + result.charAt(2)
							+ result.charAt(3));

					setActivitiesPlaanInterval();
					Animation animSlideIn = AnimationUtils.loadAnimation(
							getApplicationContext(),
							android.R.anim.slide_in_left);

					tvActivityMinutes.setText(activitiesOnFocus.getInterval()
							+ "Min");
					tvActivityMinutes.setAnimation(animSlideIn);
					tvActivityMinutes.setVisibility(View.VISIBLE);
					timeRemaining -= activitiesOnFocus.getInterval();
					tvTimeRemaining.setText("" + timeRemaining);
				}
			});

			break;
		case (R.id.tvHoursR):
		case (R.id.tvMinutesR):
			pgc.show();
			pgc.setDialogResult(new GeneralClockListener() {
				@Override
				public void finish(String result) {
					SetTimePage.this.tvHoursR.setText("" + result.charAt(0)
							+ result.charAt(1));
					SetTimePage.this.tvMinutesR.setText("" + result.charAt(2)
							+ result.charAt(3));

					setActivitiesPlaanInterval();
					SetTimePage.this.timeRemaining -= SetTimePage.this.activitiesOnFocus
							.getInterval();
					SetTimePage.this.tvTimeRemaining.setText(""
							+ SetTimePage.this.timeRemaining);

					if (repetitiveFormAllFiled()) {
						tvEndHoursR.setText(activitiesOnFocus.getEndHours());
						tvEndMinutesR.setText(activitiesOnFocus.getEndMinutes());
						Animation animSlideIn = AnimationUtils.loadAnimation(
								getApplicationContext(),
								android.R.anim.slide_in_left);
						llEndTimeR.setAnimation(animSlideIn);
						llEndTimeR.setVisibility(View.VISIBLE);
					}
				}
			});
			break;
		case (R.id.ivArrowForward):
			Animation animSlideOut = AnimationUtils.loadAnimation(
					getApplicationContext(), android.R.anim.slide_out_right);
			rlMainContent.setAnimation(animSlideOut);
			rlMainContent.setVisibility(View.INVISIBLE);

			handler.postDelayed(new Runnable() {
				public void run() {
					Animation animSlideIn = AnimationUtils.loadAnimation(
							getApplicationContext(),
							android.R.anim.slide_in_left);
					Animation animSlideOut = AnimationUtils.loadAnimation(
							getApplicationContext(),
							android.R.anim.slide_out_right);
					activitiesListIndex++;
					activitiesOnFocus = activitiesList.get(activitiesListIndex);

					resetViews();
					if (activitiesOnFocus.type == activitiesOnFocus.TYPE_ONE_TIME
							|| activitiesOnFocus.type == activitiesOnFocus.TYPE_LOOPING)
						retreiveActivitiesPlaanState();

					ActivitiesPlaan prevActivities = activitiesList
							.get(activitiesListIndex - 1);
					setStartUsingLastEndTime(tvHoursStart,
							prevActivities.getEndHours());
					setStartUsingLastEndTime(tvMinutesStart,
							prevActivities.getEndMinutes());
					setStartUsingLastEndTime(tvHoursR,
							prevActivities.getEndHours());
					setStartUsingLastEndTime(tvMinutesR,
							prevActivities.getEndMinutes());

					tvActivitiesName.setText(activitiesOnFocus.name);

					rlMainContent.setAnimation(animSlideIn);
					rlMainContent.setVisibility(View.VISIBLE);

					if (activitiesListIndex == activitiesList.size() - 1) {
						ivArrowForward.setAnimation(animSlideOut);
						ivArrowForward.setVisibility(View.GONE);
						tvDONE.setVisibility(View.VISIBLE);
					}

					if (ivArrowBack.getVisibility() == View.INVISIBLE) {
						ivArrowBack.setAnimation(animSlideIn);
						ivArrowBack.setVisibility(View.VISIBLE);
					}
				}

				private void setStartUsingLastEndTime(TextView v, String endTime) {
					if (!endTime.equals("-1"))
						v.setText(endTime);
				}

			}, 500);

			break;
		case (R.id.ivArrowBack):
			Animation animSlideOut2 = AnimationUtils.loadAnimation(
					getApplicationContext(), android.R.anim.slide_out_right);
			rlMainContent.setAnimation(animSlideOut2);
			rlMainContent.setVisibility(View.INVISIBLE);

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Animation animSlideIn2 = AnimationUtils.loadAnimation(
							getApplicationContext(),
							android.R.anim.slide_in_left);
					Animation animSlideOut2 = AnimationUtils.loadAnimation(
							getApplicationContext(),
							android.R.anim.slide_out_right);
					rlMainContent.setAnimation(animSlideIn2);
					rlMainContent.setVisibility(View.VISIBLE);

					activitiesListIndex--;
					activitiesOnFocus = activitiesList.get(activitiesListIndex);

					if (activitiesListIndex == 0) {
						ivArrowBack.setAnimation(animSlideOut2);
						ivArrowBack.setVisibility(View.INVISIBLE);
					}

					if (ivArrowForward.getVisibility() == View.INVISIBLE
							|| ivArrowForward.getVisibility() == View.GONE) {
						ivArrowForward.setAnimation(animSlideIn2);
						ivArrowForward.setVisibility(View.VISIBLE);
					}

					resetViews();
					retreiveActivitiesPlaanState();
				}
			}, 500);
			break;
		case (R.id.tvRepetitive):
		case (R.id.ibUncheckedRepetitiveBox):
			setVisibilityAndType(R.id.ibUncheckedRepetitiveBox,
					ibUncheckedRBox, ibUncheckedOTBox,
					activitiesOnFocus.TYPE_LOOPING);
			break;
		case (R.id.tvOneTime):
		case (R.id.ibUncheckedOneTimeBox):
			setVisibilityAndType(R.id.ibUncheckedOneTimeBox, ibUncheckedOTBox,
					ibUncheckedRBox, activitiesOnFocus.TYPE_ONE_TIME);
			break;
		case (R.id.tvDONE):
			Intent i = new Intent("com.example.plaan.SUMMARYPAGE");
			i.putParcelableArrayListExtra("activitiesList", activitiesList);
			startActivity(i);

			break;
		}
	}

	private void retreiveActivitiesPlaanState() {
		tvActivitiesName.setText(activitiesOnFocus.name);
		if (activitiesOnFocus.type == activitiesOnFocus.TYPE_ONE_TIME) {
			llOneTime.setVisibility(View.VISIBLE);
			clickedButton = ibUncheckedOTBox.getId();
			tvHoursStart.setText("" + activitiesOnFocus.startHours);
			tvMinutesStart.setText("" + activitiesOnFocus.startMinutes);
			tvHoursEnd.setText("" + activitiesOnFocus.endHours);
			tvMinutesEnd.setText("" + activitiesOnFocus.endMinutes);

			ibUncheckedOTBox
					.setBackgroundResource(R.drawable.yellow_blue_checked_box);
			checkMinusOne(tvHoursStart);
			checkMinusOne(tvMinutesStart);
			checkMinusOne(tvHoursEnd);
			checkMinusOne(tvMinutesEnd);

			if (!isEmptyET()) {
				tvActivityMinutes.setVisibility(View.VISIBLE);
				tvActivityMinutes.setText(activitiesOnFocus.interval + "MIN");
			}

			addzeros(tvHoursStart);
			addzeros(tvMinutesStart);
			addzeros(tvHoursEnd);
			addzeros(tvMinutesEnd);

		} else if (activitiesOnFocus.type == activitiesOnFocus.TYPE_LOOPING) {
			llRepetitive.setVisibility(View.VISIBLE);
			ibUncheckedRBox
					.setBackgroundResource(R.drawable.yellow_blue_checked_box);
			clickedButton = ibUncheckedRBox.getId();
			etLoops.setText("" + activitiesOnFocus.loops);
			etLoopTime.setText("" + activitiesOnFocus.loopTime);
			etLoopTimeType
					.setText((activitiesOnFocus.loopTimeType == activitiesOnFocus.LOOPTYPE_MINUTE) ? "MIN"
							: "");
			etBreak.setText("" + activitiesOnFocus.breakTime);
			etBreakType
					.setText((activitiesOnFocus.breakType == activitiesOnFocus.LOOPTYPE_MINUTE) ? "MIN"
							: "");
			tvHoursR.setText("" + activitiesOnFocus.startHours);
			tvMinutesR.setText("" + activitiesOnFocus.startMinutes);
			checkMinusOne(etLoops);
			checkMinusOne(etLoopTime);
			checkMinusOne(etLoopTimeType);
			checkMinusOne(etBreak);
			checkMinusOne(etBreakType);
			checkMinusOne(tvHoursR);
			checkMinusOne(tvMinutesR);
			addzeros(etLoopTime);
			addzeros(etBreak);
			addzeros(tvHoursR);
			addzeros(tvMinutesR);
		}
	}

	private boolean isEmptyET() {
		return activitiesOnFocus.startHours == -1
				|| activitiesOnFocus.startMinutes == -1
				|| activitiesOnFocus.endHours == -1
				|| activitiesOnFocus.endMinutes == -1;
	}

	private void checkMinusOne(TextView v) {
		if (v.getText().toString().equals("-1")) {
			v.setText("");
		}
	}

	private void addzeros(TextView v) {
		if (v.getText().length() == 1) {
			v.setText("0" + v.getText());
		}
	}

	private void setVisibilityAndType(int clickedButtonComp,
			ImageButton onFocusButton, ImageButton notOnFocusButon, int type) {
		if (clickedButton == clickedButtonComp) {
			// clicked button clicked again
			onFocusButton
					.setBackgroundResource(R.drawable.light_blue_check_box);
			clickedButton = 0;
			llOneTime.setVisibility(View.INVISIBLE);
			tvActivityMinutes.setVisibility(View.INVISIBLE);
			llRepetitive.setVisibility(View.INVISIBLE);
			tvActivityMinutes.setVisibility(View.INVISIBLE);
			activitiesOnFocus.setType(activitiesOnFocus.TYPE_NONE);
		} else if (clickedButton == 0) {
			// no button clicked
			activitiesOnFocus.setType(type);
			onFocusButton
					.setBackgroundResource(R.drawable.yellow_blue_checked_box);
			clickedButton = onFocusButton.getId();

		} else {
			// other button clicked
			activitiesOnFocus.setType(type);
			onFocusButton
					.setBackgroundResource(R.drawable.yellow_blue_checked_box);
			notOnFocusButon
					.setBackgroundResource(R.drawable.light_blue_check_box);
			clickedButton = onFocusButton.getId();
		}

		Animation animSlideIn = AnimationUtils.loadAnimation(
				getApplicationContext(), android.R.anim.slide_in_left);
		if (clickedButton == R.id.ibUncheckedOneTimeBox) {
			llOneTime.setAnimation(animSlideIn);
			llOneTime.setVisibility(View.VISIBLE);
			if (!tvActivityMinutes.getText().equals("")) {
				tvActivityMinutes.setAnimation(animSlideIn);
				tvActivityMinutes.setVisibility(View.VISIBLE);
			}
			llRepetitive.setVisibility(View.INVISIBLE);
		} else if (clickedButton == R.id.ibUncheckedRepetitiveBox) {
			llRepetitive.setAnimation(animSlideIn);
			llRepetitive.setVisibility(View.VISIBLE);
			llOneTime.setVisibility(View.INVISIBLE);
			tvActivityMinutes.setVisibility(View.INVISIBLE);
		}

	}

	private void resetViews() {
		ibUncheckedOTBox.setBackgroundResource(R.drawable.light_blue_check_box);
		ibUncheckedRBox.setBackgroundResource(R.drawable.light_blue_check_box);
		clickedButton = 0;

		llOneTime.setVisibility(View.INVISIBLE);
		tvHoursStart.setText("");
		tvMinutesStart.setText("");
		tvHoursEnd.setText("");
		tvMinutesEnd.setText("");
		tvActivityMinutes.setVisibility(View.INVISIBLE);
		tvActivityMinutes.setText("");

		llRepetitive.setVisibility(View.INVISIBLE);
		etLoops.setText("");
		etLoopTime.setText("");
		etLoopTimeType.setText("");
		etBreak.setText("");
		etBreakType.setText("");
		tvHoursR.setText("");
		tvMinutesR.setText("");
		tvEndHoursR.setText("");
		tvEndMinutesR.setText("");
		llEndTimeR.setVisibility(View.INVISIBLE);

		tvDONE.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent arg2) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_NEXT:
			if (v.getText().toString().length() == 1) {
				v.setText("0" + v.getText());
			} else {
				v.setText(v.getText().toString().toUpperCase(Locale.ENGLISH));
			}
			break;
		case EditorInfo.IME_ACTION_DONE:
			v.setText(v.getText().toString().toUpperCase(Locale.ENGLISH));
			if (repetitiveFormAllFiled()) {
				setActivitiesPlaanInterval();
				tvEndHoursR.setText(activitiesOnFocus.getEndHours());
				tvEndMinutesR.setText(activitiesOnFocus.getEndMinutes());
				Animation animSlideIn = AnimationUtils.loadAnimation(
						getApplicationContext(), android.R.anim.slide_in_left);
				llEndTimeR.setAnimation(animSlideIn);
				llEndTimeR.setVisibility(View.VISIBLE);
			}
			break;
		}
		return false;
	}

	private boolean repetitiveFormAllFiled() {
		return !(etLoops.getText().toString().equals(""))
				&& !(etLoopTime.getText().toString().equals(""))
				&& !(etLoopTimeType.getText().toString().equals(""))
				&& !(etBreak.getText().toString().equals(""))
				&& !(etBreakType.getText().toString().equals(""))
				&& !(tvHoursR.getText().equals(""))
				&& !(tvMinutesR.getText().equals(""));
	}

	private void setActivitiesPlaanInterval() {
		if (activitiesOnFocus.type == activitiesOnFocus.TYPE_ONE_TIME) {
			int startHours = Integer
					.parseInt(tvHoursStart.getText().toString());
			int startMinutes = Integer.parseInt(tvMinutesStart.getText()
					.toString());

			int endHours = Integer.parseInt(tvHoursEnd.getText().toString());
			int endMinutes = Integer
					.parseInt(tvMinutesEnd.getText().toString());
			// TODO fix this AM PM THINGY

			activitiesOnFocus.setInterval(startHours, startMinutes, endHours,
					endMinutes, "AM", "AM");
		} else if (activitiesOnFocus.type == activitiesOnFocus.TYPE_LOOPING) {
			int startHours = Integer.parseInt(tvHoursR.getText().toString());
			int startMinutes = Integer
					.parseInt(tvMinutesR.getText().toString());
			int loops = Integer.parseInt(etLoops.getText().toString());
			int loopTime = Integer.parseInt(etLoopTime.getText().toString());
			String sLoopType = etLoopTimeType.getText().toString();
			int loopType = 0;
			if (sLoopType.equals("MIN"))
				loopType = activitiesOnFocus.LOOPTYPE_MINUTE;
			else
				loopType = activitiesOnFocus.LOOPTYPE_HOUR;

			String sBreakType = etBreakType.getText().toString();
			int breakTimeType = 0;
			if (sBreakType.equals("MIN")) {
				breakTimeType = activitiesOnFocus.LOOPTYPE_MINUTE;
			} else {
				breakTimeType = activitiesOnFocus.LOOPTYPE_HOUR;
			}
			int breakTime = Integer.parseInt(etBreak.getText().toString());

			activitiesOnFocus.setInterval(startHours, startMinutes, loops,
					loopTime, loopType, breakTime, breakTimeType);

		}

	}

	public class MyAdapter extends ArrayAdapter<String> {
		public MyAdapter(Context ctx, int customSpinnerId, String[] objects) {
			super(ctx, customSpinnerId, objects);
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
			View mySpinner = inflater.inflate(R.layout.time_type_spinner,
					parent, false);
			TextView tvMeridiemSpinner = (TextView) mySpinner
					.findViewById(R.id.tvTimeType);
			TypefacePlaan tfp = new TypefacePlaan();
			tfp.setTypeface(tvMeridiemSpinner, tfp.LEAGUE_GOTHIC);
			tvMeridiemSpinner.setText(spinnerMeridiemValues[position]);
			return mySpinner;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (itemSelected) {
			setActivitiesPlaanInterval();
			tvActivityMinutes.setText(activitiesOnFocus.getInterval() + "Min");
			tvActivityMinutes.setVisibility(View.VISIBLE);

			timeRemaining -= activitiesOnFocus.getInterval();
			tvTimeRemaining.setText("" + timeRemaining);

			// if (activitiesListIndex != activitiesList.size() - 1)
			// ibNextButton.setVisibility(View.VISIBLE);
			// else
			// ibNextButton.setVisibility(View.INVISIBLE);
		}
		itemSelected = true;

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
}
