package com.example.plaan;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zenkun.datetimepicker.time.RadialPickerLayout;
import com.zenkun.datetimepicker.time.TimePickerDialog;
import com.zenkun.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

@SuppressLint("ViewConstructor")
public class ActivityCard extends RelativeLayout implements OnClickListener,
		OnTimeSetListener, TextWatcher, OnEditorActionListener {

	// root
	LinearLayout llRootActivityCard;

	// parentScrollView
	ScrollView parentScrollView;
	// timeLeftClcok
	// CountDownTimer timeleftCD;
	// long timeLeft;
	// RelativeLayout rlTimeLeftClock;
	// TextView tvTimeLeft;
	// long millisLeft;

	// the ActivitiesPlaan properties
	ActivitiesPlaan theActivity;
	EditText etActivityName;
	RelativeLayout rlOneTimeText, rlLoopingText;
	int activity_type;

	// ActivityCard properties
	int tag;

	// ActivitiesSetter
	RelativeLayout rlActivitySetter;

	// oneTime
	LinearLayout llOneTime;
	TextView tvOneTime, tvLooping, tvStartOneTime, tvStartHoursOneTime,
			tvEndOneTime, tvEndHoursOneTime, tvTimeTypeOneTime;
	EditText etTimeOneTime;

	// looping
	LinearLayout llLooping;
	LinearLayout llStartEndLooping, rlStartSectionLooping, rlEndSectionLooping;
	LinearLayout llLoopingSettings;
	TextView tvStartLooping, tvStartHoursLooping, tvEndLooping,
			tvEndHoursLooping, tvX, tvLoopingTimeType, tvBreakTimeType,
			tvIntervalLoopCD;
	EditText etLoopingFreq, etLoopingTime, etBreakTime;

	// setButton
	RelativeLayout rlSET_Button;
	TextView tvSET;
	onSetClickListener osc;

	// ActivityCountDown
	AutoResizeTextView tvActivitiesNameCD;
	RelativeLayout rlActivityCountDown;
	TextView tvCountDown, tvStartEndCD, tvIntervalOTCD, tvLoopingFreqCD, tvXCD,
			tvLoopingTimeCD, tvBreakTimeCD, tvActivityTypeCD, tvBreakText,
			tvDONE;
	LinearLayout llOneTimePropertiesCD, llLoopingPropertiesCD;
	ImageView ivToDoButton, ivBackToActivitySetter, ivCountDownClock;
	boolean backToActivtySetterClicked;

	// To Do
	RelativeLayout rlToDo;
	RelativeLayout rlTask, rlTaskLeftIcon;
	TextView tvToDo, tvActivityNameTD;
	// EditText etTask;
	ImageView ivArrowBack;
	ScrollView svTodo;
	TextView tvTaskLeft;
	ArrayList<String> taskList;
	final int todobar_idHeader = 112;
	int todobar_idCounter = 0;

	FragmentManager fragmentManager;
	Context context;

	int lastThingClicked;

	float scaleDP;
	LayoutInflater layoutInflater;

	public ActivityCard(Context context) {
		super(context);
	}

	public ActivityCard(Context context, FragmentManager fragmentManager,
			LayoutInflater layoutInflater, int tag, ScrollView parentScrollView) {
		super(context);
		// this.timeLeft = timeLeft;
		this.context = context;
		this.layoutInflater = layoutInflater;
		this.fragmentManager = fragmentManager;
		lastThingClicked = 0;
		scaleDP = getResources().getDisplayMetrics().density;
		activity_type = ActivitiesPlaan.TYPE_ONE_TIME;
		backToActivtySetterClicked = false;
		this.tag = tag;
		this.setTag(tag);
		taskList = new ArrayList<String>();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addView(inflater.inflate(R.layout.activity_card_layout, null));

		this.parentScrollView = parentScrollView;
		initializeViews();
		setTypefaces();

		// timeLeft clock
		// timeleftCD = new CountDownTimer(this.timeLeft, 1000) {
		// @Override
		// public void onTick(long millisUntilFinished) {
		// // format[0] = hours, format[1] = minutes , format[2] = seconds
		// int[] format = new int[3];
		// formatMillis(format, millisUntilFinished);
		//
		// String s = "";
		// if (format[0] != 0)
		// s += addZeros("" + format[0]) + ":";
		// if (format[0] != 0 || format[1] != 0)
		// s += addZeros("" + format[1]) + ":";
		// s += addZeros("" + format[2]);
		//
		// tvTimeLeft.setText(s);
		// millisLeft = millisUntilFinished;
		// }

		// @Override
		// public void onFinish() {
		//
		// }
		// }.start();

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ActivityCard.this.parentScrollView
						.requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});

		svTodo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		// oneTime
		rlOneTimeText.setOnClickListener(this);
		rlLoopingText.setOnClickListener(this);
		tvStartHoursOneTime.setOnClickListener(this);
		tvEndHoursOneTime.setOnClickListener(this);
		tvTimeTypeOneTime.setOnClickListener(this);
		etTimeOneTime.addTextChangedListener(this);

		// looping
		tvStartHoursLooping.setOnClickListener(this);
		tvLoopingTimeType.setOnClickListener(this);
		tvBreakTimeType.setOnClickListener(this);
		etLoopingFreq.addTextChangedListener(this);
		etLoopingTime.addTextChangedListener(this);
		etBreakTime.addTextChangedListener(this);

		// setbutton
		rlSET_Button.setOnClickListener(this);

		// activity count doiwn
		ivBackToActivitySetter.setOnClickListener(this);

		// To Do
		ivToDoButton.setOnClickListener(this);
		ivArrowBack.setOnClickListener(this);
		rlTaskLeftIcon.setOnClickListener(this);

	}

	public ActivityCard(Context context, FragmentManager fragmentManager,
			LayoutInflater layoutInflater, int tag,
			ScrollView parentScrollView, String startTime) {
		this(context, fragmentManager, layoutInflater, tag, parentScrollView);

		// oneTime
		tvStartHoursOneTime.setText(startTime);
		tvStartHoursOneTime.setAlpha(1);

		// Looping
		tvStartHoursLooping.setText(startTime);
		tvStartHoursLooping.setAlpha(1);

	}

	public interface onSetClickListener {
		public void onSetClick(ActivitiesPlaan theActivity,
				boolean firstTimeVisit, ActivityCard theCard);
	}

	public void setOnSetClickListener(onSetClickListener listener) {
		osc = listener;
	}

	private void setTypefaces() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(etActivityName, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);

		// time left clock
		// tfp.setTypeface(tvTimeLeft, TypefacePlaan.LEAGUE_GOTHIC);

		// one time
		tfp.setTypeface(tvOneTime, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);
		tfp.setTypeface(tvLooping, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);
		tfp.setTypeface(tvStartOneTime, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvStartHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEndOneTime, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvEndHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(etTimeOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvTimeTypeOneTime, TypefacePlaan.LEAGUE_GOTHIC);

		// looping
		tfp.setTypeface(tvStartLooping, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvStartHoursLooping, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEndLooping, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvEndHoursLooping, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvStartHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvX, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(etLoopingFreq, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(etLoopingTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(etBreakTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvLoopingTimeType, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvBreakTimeType, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvIntervalLoopCD, TypefacePlaan.LEAGUE_GOTHIC);

		// tvSET
		tfp.setTypeface(tvSET, TypefacePlaan.LEAGUE_GOTHIC);

		// activityCountDown
		tfp.setTypeface(tvCountDown, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvActivitiesNameCD, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvStartEndCD, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvIntervalOTCD, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvLoopingFreqCD, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvXCD, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvLoopingTimeCD, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvBreakTimeCD, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvActivityTypeCD, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvBreakText, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvDONE, TypefacePlaan.LEAGUE_GOTHIC);

		// To do
		tfp.setTypeface(tvToDo, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvActivityNameTD, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvTaskLeft, TypefacePlaan.OPEN_SANS_BOLD);
	}

	public void setTvBreakVisibility(int visibility) {
		tvBreakText.setVisibility(visibility);
	}

	public void decreaseLoopLeftText() {
		tvLoopingFreqCD.setText(""
				+ (Integer.parseInt(tvLoopingFreqCD.getText().toString()) - 1));
	}

	public void setTvCountDownText(String text, boolean adjust) {
		tvCountDown.setText(text);
		if (adjust)
			adjustTvCountDown();
	}

	private void initializeViews() {
		llRootActivityCard = (LinearLayout) findViewById(R.id.llRootActivityCard);
		// rlTimeLeftClock = (RelativeLayout)
		// findViewById(R.id.rlTimeLeftClock);
		// tvTimeLeft = (TextView) findViewById(R.id.tvTimeLeft);
		etActivityName = (EditText) findViewById(R.id.etActivityName);
		rlOneTimeText = (RelativeLayout) findViewById(R.id.rlOneTimeText);
		rlLoopingText = (RelativeLayout) findViewById(R.id.rlLoopingText);
		tvOneTime = (TextView) findViewById(R.id.tvOneTime);
		tvLooping = (TextView) findViewById(R.id.tvLooping);
		tvStartOneTime = (TextView) findViewById(R.id.tvStartOneTime);
		tvStartHoursOneTime = (TextView) findViewById(R.id.tvStartHoursOneTime);
		tvEndOneTime = (TextView) findViewById(R.id.tvEndOneTime);
		tvEndHoursOneTime = (TextView) findViewById(R.id.tvEndHoursOneTime);
		llLooping = (LinearLayout) findViewById(R.id.llLooping);
		llStartEndLooping = (LinearLayout) findViewById(R.id.llStartEndLooping);
		rlStartSectionLooping = (LinearLayout) findViewById(R.id.rlStartSectionLooping);
		rlEndSectionLooping = (LinearLayout) findViewById(R.id.rlEndSectionLooping);
		llLoopingSettings = (LinearLayout) findViewById(R.id.llLoopingSettings);
		tvStartLooping = (TextView) findViewById(R.id.tvStartLooping);
		tvStartHoursLooping = (TextView) findViewById(R.id.tvStartHoursLooping);
		tvEndLooping = (TextView) findViewById(R.id.tvEndLooping);
		tvEndHoursLooping = (TextView) findViewById(R.id.tvEndHoursLooping);
		tvX = (TextView) findViewById(R.id.tvX);
		etLoopingFreq = (EditText) findViewById(R.id.etLoopingFreq);
		etLoopingTime = (EditText) findViewById(R.id.etLoopingTime);
		etBreakTime = (EditText) findViewById(R.id.etBreakTime);
		llOneTime = (LinearLayout) findViewById(R.id.llOneTime);
		rlSET_Button = (RelativeLayout) findViewById(R.id.rlSET_Button);
		tvSET = (TextView) findViewById(R.id.tvSET);
		tvLoopingTimeType = (TextView) findViewById(R.id.tvLoopingTimeType);
		tvBreakTimeType = (TextView) findViewById(R.id.tvBreakTimeType);
		tvTimeTypeOneTime = (TextView) findViewById(R.id.tvTimeTypeOneTime);
		etTimeOneTime = (EditText) findViewById(R.id.etTimeOneTime);
		tvCountDown = (TextView) findViewById(R.id.tvCountDown);
		tvLoopingFreqCD = (TextView) findViewById(R.id.tvLoopingFreqCD);
		tvXCD = (TextView) findViewById(R.id.tvXCD);
		tvLoopingTimeCD = (TextView) findViewById(R.id.tvLoopingTimeCD);
		tvBreakTimeCD = (TextView) findViewById(R.id.tvBreakTimeCD);
		tvActivityTypeCD = (TextView) findViewById(R.id.tvActivityTypeCD);
		tvIntervalOTCD = (TextView) findViewById(R.id.tvIntervalOTCD);
		tvActivitiesNameCD = (AutoResizeTextView) findViewById(R.id.tvActivitiesNameCD);
		tvStartEndCD = (TextView) findViewById(R.id.tvStartEndCD);
		rlActivityCountDown = (RelativeLayout) findViewById(R.id.rlActivityCountDown);
		rlActivitySetter = (RelativeLayout) findViewById(R.id.rlActivitySetter);
		llLoopingPropertiesCD = (LinearLayout) findViewById(R.id.llLoopingPropertiesCD);
		llOneTimePropertiesCD = (LinearLayout) findViewById(R.id.llOneTimePropertiesCD);
		tvToDo = (TextView) findViewById(R.id.tvToDo);
		tvActivityNameTD = (TextView) findViewById(R.id.tvActivityNameTD);
		// etTask = (EditText) findViewById(R.id.etTask);
		rlToDo = (RelativeLayout) findViewById(R.id.rlToDo);
		rlTask = (RelativeLayout) findViewById(R.id.rlTask);
		ivArrowBack = (ImageView) findViewById(R.id.ivArrowBack);
		ivToDoButton = (ImageView) findViewById(R.id.ivTodoButton);
		ivBackToActivitySetter = (ImageView) findViewById(R.id.ivBackToActivitySetter);
		tvBreakText = (TextView) findViewById(R.id.tvBreakText);
		svTodo = (ScrollView) findViewById(R.id.svTodo);
		rlTaskLeftIcon = (RelativeLayout) findViewById(R.id.rlTaskLeftIcon);
		tvTaskLeft = (TextView) findViewById(R.id.tvTaskLeft);
		tvDONE = (TextView) findViewById(R.id.tvDONE);
		tvIntervalLoopCD = (TextView) findViewById(R.id.tvIntervalLoopCD);
		ivCountDownClock = (ImageView) findViewById(R.id.ivCountDownClock);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == tvStartHoursOneTime.getId()
				|| v.getId() == tvStartHoursLooping.getId()) {
			// oneTime or Looping START HOURS
			Calendar calendar = Calendar.getInstance();
			TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
					this, calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), false);
			timePickerDialog.show(fragmentManager, "timepicker");

		} else if (v.getId() == tvEndHoursOneTime.getId()
				|| v.getId() == tvEndHoursLooping.getId()) {
			// oneTime or Looping END HOURS
			TimePickerDialog timePickerDialog = null;

			// the preset clock setting will be equal to its startHours
			if (lastThingClicked == tvStartHoursLooping.getId()) {
				timePickerDialog = TimePickerDialog.newInstance(
						this,
						Integer.parseInt(tvStartHoursLooping.getText()
								.toString().substring(0, 2)),
						Integer.parseInt(tvStartHoursLooping.getText()
								.toString().substring(3)), false);
			} else {
				timePickerDialog = TimePickerDialog.newInstance(
						this,
						Integer.parseInt(tvStartHoursOneTime.getText()
								.toString().substring(0, 2)),
						Integer.parseInt(tvStartHoursOneTime.getText()
								.toString().substring(3)), false);
			}
			timePickerDialog.show(fragmentManager, "timepicker");

		} else if (v.getId() == rlOneTimeText.getId()) {
			// TYPE_ONE_TIME setter
			rlOneTimeText
					.setBackgroundResource(R.drawable.one_time_or_looping_blue_selection_bar);
			tvOneTime
					.setTextColor(getResources().getColor(R.color.lightergrey));
			rlLoopingText.setBackgroundColor(Color.TRANSPARENT);
			llOneTime.setVisibility(View.VISIBLE);

			tvLooping.setTextColor(getResources().getColor(R.color.darkgrey));
			llLooping.setVisibility(View.INVISIBLE);
			activity_type = ActivitiesPlaan.TYPE_ONE_TIME;
		} else if (v.getId() == rlLoopingText.getId()) {
			// TYPE_LOOPING setter
			rlLoopingText
					.setBackgroundResource(R.drawable.one_time_or_looping_blue_selection_bar);
			tvLooping
					.setTextColor(getResources().getColor(R.color.lightergrey));
			rlOneTimeText.setBackgroundColor(Color.TRANSPARENT);
			llLooping.setVisibility(View.VISIBLE);

			tvOneTime.setTextColor(getResources().getColor(R.color.darkgrey));
			llOneTime.setVisibility(View.INVISIBLE);
			activity_type = ActivitiesPlaan.TYPE_LOOPING;
		} else if (v.getId() == rlSET_Button.getId()) {
			// SET_BUTTON : set theActivity properties based on the settings
			// timeleftCD.cancel();
			// rlTimeLeftClock.setVisibility(View.INVISIBLE);

			theActivity = new ActivitiesPlaan(etActivityName.getText()
					.toString(), activity_type);
			if (activity_type == ActivitiesPlaan.TYPE_ONE_TIME) {
				setOneTimeProperties(theActivity);
			} else if (activity_type == ActivitiesPlaan.TYPE_LOOPING) {
				setLoopingProperties(theActivity);
			}

			theActivity.setActivityCard(this);
			// millisLeft -= theActivity.getInterval() * 60000;
			if (!backToActivtySetterClicked) {
				// if its the firstTime visit
				// debugging
				osc.onSetClick(theActivity, true, this);
			} else {
				osc.onSetClick(theActivity, false, this);
			}
			rlActivitySetter.setVisibility(View.INVISIBLE);
			rlActivityCountDown.setVisibility(View.VISIBLE);
			setActivityCountDownProperties(theActivity);
		} else if (v.getId() == tvLoopingTimeType.getId()
				|| v.getId() == tvBreakTimeType.getId()
				|| v.getId() == tvTimeTypeOneTime.getId()) {
			// MIN or HRS
			TextView tv = (TextView) v;
			if (tv.getText().toString().equals("MIN"))
				tv.setText("HRS");
			else
				tv.setText("MIN");
			if (allOneTimePropertiesFilled()
					&& llOneTime.getVisibility() == View.VISIBLE)
				calculateOneTimeInterval();

		} else if (v.getId() == ivToDoButton.getId()
				|| v.getId() == rlTaskLeftIcon.getId()) {
			// TODO_BUTTON / rlTaskLeft
			rlActivityCountDown.setVisibility(View.GONE);
			tvActivityNameTD.setText(tvActivitiesNameCD.getText());
			rlToDo.setVisibility(View.VISIBLE);

			// make sure no todo_bar already there
			if (todobar_idCounter == 0)
				addTodoBar();

		} else if (v.getId() == ivArrowBack.getId()) {
			// ARROW_BACK from TODO_PAGE to CD_PAGE
			rlToDo.setVisibility(View.GONE);
			rlActivityCountDown.setVisibility(View.VISIBLE);

			// inflate task_left_icon
			rlTaskLeftIcon.setVisibility(View.VISIBLE);
			tvTaskLeft.setText("" + CountToDo());

			// un-inflate todo_button
			ivToDoButton.setVisibility(View.INVISIBLE);
		} else if (v.getId() == ivBackToActivitySetter.getId()) {
			// ARROW_BACK from CD_PAGE to SETTER_PAGE
			rlActivityCountDown.setVisibility(View.INVISIBLE);
			rlActivitySetter.setVisibility(View.VISIBLE);
			backToActivtySetterClicked = true;
		}
		lastThingClicked = v.getId();
	}

	private int CountToDo() {
		int counter = 0;
		for (int i = 0; i < rlTask.getChildCount(); i++) {
			TodoBar tdb = (TodoBar) rlTask.getChildAt(i);
			if (tdb.etTodo.getText().toString().length() > 0) {
				counter++;
			}
		}
		return counter;
	}

	private void addTodoBar() {
		TodoBar tdb = new TodoBar(getContext());
		tdb.requestFocus();
		tdb.setId(Integer.parseInt("" + todobar_idHeader + todobar_idCounter));
		LayoutParams lpForTdb = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if (todobar_idCounter != 0)
			lpForTdb.addRule(RelativeLayout.BELOW, tdb.getId() - 1);
		lpForTdb.setMargins(0, (int) (5 * scaleDP + 0.5f), 0, 0);
		todobar_idCounter++;
		tdb.etTodo.setOnEditorActionListener(this);
		Animation comeIn = AnimationUtils.loadAnimation(getContext(),
				android.R.anim.slide_in_left);
		tdb.setAnimation(comeIn);
		rlTask.addView(tdb, lpForTdb);
	}

	public ActivitiesPlaan getActivitiesPlaan() {
		return theActivity;
	}

	private void setActivityCountDownProperties(ActivitiesPlaan theActivity) {

		tvActivitiesNameCD.setText(etActivityName.getText().toString());
		String start = "";
		String end = "";
		int[] format = new int[3];

		String s = "";

		if (activity_type == ActivitiesPlaan.TYPE_ONE_TIME) {
			start = theActivity.getStartTime();
			end = theActivity.getEndTime();
			llOneTimePropertiesCD.setVisibility(View.VISIBLE);
			tvActivityTypeCD.setText("One Time");
			llLoopingPropertiesCD.setVisibility(View.INVISIBLE);

			formatMillis(format, theActivity.interval);
			if (format[0] != 0)
				s += addZeros("" + format[0]) + ":";
			if (format[0] != 0 || format[1] != 0)
				s += addZeros("" + format[1]) + ":";
			s += addZeros("" + format[2]);
			tvCountDown.setText(s);
			tvIntervalOTCD.setText(s);

		} else if (activity_type == ActivitiesPlaan.TYPE_LOOPING) {
			start = theActivity.getStartTime();
			end = theActivity.getEndTime();
			llLoopingPropertiesCD.setVisibility(View.VISIBLE);
			tvIntervalLoopCD.setText(intervalFormatting(theActivity));
			tvLoopingFreqCD.setText(etLoopingFreq.getText().toString());
			tvLoopingTimeCD.setText(etLoopingTime.getText().toString()
					+ tvLoopingTimeType.getText().toString());
			tvBreakTimeCD.setText(etBreakTime.getText().toString()
					+ tvBreakTimeType.getText().toString());
			tvActivityTypeCD.setText("Looping");
			llOneTimePropertiesCD.setVisibility(View.INVISIBLE);

			formatMillis(format, theActivity.loopTime);
			// debugging
			Log.d("Looping Error", "LE -- hours = " + format[0]);
			Log.d("Looping Error", "LE -- minutes = " + format[1]);
			Log.d("Looping Error", "LE -- seconds = " + format[2]);

			if (format[0] != 0)
				s += addZeros("" + format[0]) + ":";
			if (format[0] != 0 || format[1] != 0)
				s += addZeros("" + format[1]) + ":";
			s += addZeros("" + format[2]);
			tvCountDown.setText(s);

		}
		adjustTvCountDown();
		tvStartEndCD.setText(start + " - " + end);
	}

	private CharSequence intervalFormatting(ActivitiesPlaan a) {
		long interval = a.getInterval(); // in millis
		int[] format = new int[3];
		formatMillis(format, interval);
		String intervalFormatted = "";
		if (format[0] != 0) {
			intervalFormatted += addZeros("" + format[0]) + ":";
		}
		if (format[0] != 0 || format[1] != 0) {
			intervalFormatted += addZeros("" + format[1]) + ":";
		}
		intervalFormatted += addZeros("" + format[2]);
		return intervalFormatted;
	}

	private void adjustTvCountDown() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if (tvCountDown.getText().toString().length() == 8) {
			lp.setMargins((int) (48 * scaleDP + 0.5f),
					(int) (95 * scaleDP + 0.5f), 0, 0);
			tvCountDown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
		} else if (tvCountDown.getText().toString().length() == 5) {
			lp.setMargins((int) (50 * scaleDP + 0.5f),
					(int) (80 * scaleDP + 0.5f), 0, 0);
			tvCountDown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
		} else if (tvCountDown.getText().toString().length() == 2) {
			lp.setMargins((int) (65 * scaleDP + 0.5f),
					(int) (60 * scaleDP + 0.5f), 0, 0);
			tvCountDown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
		}
		tvCountDown.setLayoutParams(lp);
	}

	private void setLoopingProperties(ActivitiesPlaan theActivity) {
		int[] startHours = new int[1];
		int[] startMinutes = new int[1];

		int[] loops = new int[1];
		int[] loopTime = new int[1];
		int[] loopType = new int[1];

		int[] breakTime = new int[1];

		int[] breakTimeType = new int[1];
		retreiveLoopingIntervalParameters(startHours, startMinutes, loops,
				loopTime, loopType, breakTime, breakTimeType);

		theActivity.setInterval(startHours[0], startMinutes[0], loops[0],
				loopTime[0], loopType[0], breakTime[0], breakTimeType[0]);

		// debugging
		Log.d("Looping error", "LE -- theActivity.interval = "
				+ theActivity.interval);
		Log.d("Looping error", "LE -- theActivity.loopTime = "
				+ theActivity.loopTime);
		Log.d("Looping error", "LE -- theActivity.loops = " + theActivity.loops);

	}

	private void setOneTimeProperties(ActivitiesPlaan theActivity) {
		int startHours = Integer.parseInt(tvStartHoursOneTime.getText()
				.toString().substring(0, 2));
		int startMinutes = Integer.parseInt(tvStartHoursOneTime.getText()
				.toString().substring(3));
		int endHours = Integer.parseInt(tvEndHoursOneTime.getText().toString()
				.substring(0, 2));
		int endMinutes = Integer.parseInt(tvEndHoursOneTime.getText()
				.toString().substring(3));
		theActivity.setInterval(startHours, startMinutes, endHours, endMinutes,
				"", "");
	}

	private int checkForType(String ls) {
		if (ls.equals("MIN")) {
			return ActivitiesPlaan.LOOPTYPE_MINUTE;
		}
		return ActivitiesPlaan.LOOPTYPE_HOUR;
	}

	@Override
	public void onTimeSet(RadialPickerLayout v, int hourOfDay, int minute) {
		if (lastThingClicked == tvStartHoursOneTime.getId()) {
			// set the starHours of OneTime
			tvStartHoursOneTime.setText(addZeros("" + hourOfDay) + ":"
					+ addZeros("" + minute));
			tvStartHoursOneTime.setAlpha((float) 1.0);
			tvStartHoursOneTime.setWidth(tvStartHoursOneTime.getWidth()
					+ ((int) (8 * scaleDP + 0.5f)));
			if (allOneTimePropertiesFilled()
					&& llOneTime.getVisibility() == View.VISIBLE)
				calculateOneTimeInterval();
		} else if (lastThingClicked == tvEndHoursOneTime.getId()) {
			// set the endHours of OneTime
			tvEndHoursOneTime.setText(addZeros("" + hourOfDay) + ":"
					+ addZeros("" + minute));
			tvEndHoursOneTime.setAlpha((float) 1.0);
			tvEndHoursOneTime.setWidth(tvEndHoursOneTime.getWidth()
					+ ((int) (8 * scaleDP + 0.5f)));
			if (allOneTimePropertiesFilled()
					&& llOneTime.getVisibility() == View.VISIBLE)
				calculateOneTimeInterval();
		} else if (lastThingClicked == tvStartHoursLooping.getId()) {
			// set the startHours of Looping
			tvStartHoursLooping.setText(addZeros("" + hourOfDay) + ":"
					+ addZeros("" + minute));
			tvStartHoursLooping.setAlpha((float) 1.0);
		} else if (lastThingClicked == tvEndHoursLooping.getId()) {
			// set the endHours of Looping
			tvEndHoursLooping.setText(addZeros("" + hourOfDay) + ":"
					+ addZeros("" + minute));
			tvEndHoursLooping.setAlpha((float) 1.0);
		}

	}

	private boolean allOneTimePropertiesFilled() {
		return tvStartHoursOneTime.getAlpha() == 1
				&& !etTimeOneTime.getText().toString().equals("")
				&& tvEndOneTime.getAlpha() == 1;
	}

	private String addZeros(String s) {
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (activity_type == ActivitiesPlaan.TYPE_LOOPING
				&& allLoopingPropertiesFilled()) {
			calculateLoopingInterval();
		} else if (activity_type == ActivitiesPlaan.TYPE_ONE_TIME) {
			// etTimeOneTime is listened
			if (tvStartHoursOneTime.getText().toString().length() > 0)
				calculateOneTimeInterval();
		}
	}

	private void calculateOneTimeInterval() {
		int interval = 0;
		if (etTimeOneTime.getText().toString().length() > 0)
			interval = Integer.parseInt(etTimeOneTime.getText().toString());
		if (tvTimeTypeOneTime.getText().toString().equals("HRS")) {
			interval = interval * 60;
		}
		int startHours = Integer.parseInt(tvStartHoursOneTime.getText()
				.toString().substring(0, 2));
		int startMinutes = Integer.parseInt(tvStartHoursOneTime.getText()
				.toString().substring(3));
		int[] endHours = new int[1];
		int[] endMinutes = new int[1];
		int[] hoursIncreased = new int[1];
		endHours[0] = startHours + interval / 60;
		endMinutes[0] = startMinutes + interval % 60;

		checkForMoreThan(endMinutes, 60, hoursIncreased);
		endHours[0] += hoursIncreased[0];
		checkForMoreThan(endHours, 24, hoursIncreased);

		tvEndHoursOneTime.setAlpha(1);
		tvEndHoursOneTime.setText(addZeros("" + endHours[0]) + ":"
				+ addZeros("" + endMinutes[0]));

	}

	private void calculateLoopingInterval() {

		int interval = 0;
		int[] startHours = new int[1];
		int[] startMinutes = new int[1];

		int[] loops = new int[1];
		int[] loopTime = new int[1];
		int[] loopType = new int[1];

		int[] breakTime = new int[1];

		int[] breakTimeType = new int[1];

		retreiveLoopingIntervalParameters(startHours, startMinutes, loops,
				loopTime, loopType, breakTime, breakTimeType);

		switch (loopType[0]) {
		case ActivitiesPlaan.LOOPTYPE_MINUTE:
			// interval is in minutes
			interval = loops[0] * (loopTime[0] + breakTime[0]);
			break;
		case ActivitiesPlaan.LOOPTYPE_HOUR:
			// interval is in minutes
			interval = loops[0] * (loopTime[0] * 60 + breakTime[0]);
			break;
		}

		int[] endHours = new int[1];
		int[] endMinutes = new int[1];
		int[] hoursIncreased = new int[1];
		endHours[0] = startHours[0] + interval / 60;
		endMinutes[0] = startMinutes[0] + interval % 60;

		checkForMoreThan(endMinutes, 60, hoursIncreased);
		endHours[0] += hoursIncreased[0];
		checkForMoreThan(endHours, 24, hoursIncreased);

		tvEndHoursLooping.setAlpha(1);
		tvEndHoursLooping.setText(addZeros("" + endHours[0]) + ":"
				+ addZeros("" + endMinutes[0]));
	}

	private void checkForMoreThan(int[] time, int usualNumber,
			int[] hoursIncreased) {
		while (time[0] >= usualNumber) {
			time[0] -= usualNumber;
			hoursIncreased[0]++;
		}
	}

	private void retreiveLoopingIntervalParameters(int[] startHours,
			int[] startMinutes, int[] loops, int[] loopTime, int[] loopType,
			int[] breakTime, int[] breakTimeType) {
		startHours[0] = Integer.parseInt(tvStartHoursLooping.getText()
				.toString().substring(0, 2));
		startMinutes[0] = Integer.parseInt(tvStartHoursLooping.getText()
				.toString().substring(3));

		String loopsString = etLoopingFreq.getText().toString();
		String loopTimeString = etLoopingTime.getText().toString();
		String breakTimeString = etBreakTime.getText().toString();

		loops[0] = Integer.parseInt(loopsString);
		loopTime[0] = Integer.parseInt(loopTimeString);
		String loopTypeString = tvLoopingTimeType.getText().toString();
		loopType[0] = checkForType(loopTypeString);

		breakTime[0] = Integer.parseInt(breakTimeString);

		String breakTimeTypeString = tvBreakTimeType.getText().toString();
		breakTimeType[0] = checkForType(breakTimeTypeString);

	}

	private boolean allLoopingPropertiesFilled() {
		return (etLoopingFreq.getText().toString().length() != 0)
				&& (etLoopingTime.getText().toString().length() != 0)
				&& (etBreakTime.getText().toString().length() != 0);
	}

	private void formatMillis(int[] format, long millisUntilFinished) {
		// format[0] = hours, format[1] = minutes , format[2] = seconds
		format[0] = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
		format[1] = (int) ((millisUntilFinished / (1000 * 60)) % 60);
		format[2] = (int) (millisUntilFinished / 1000) % 60;
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_DONE:
			addTodoBar();
			break;
		default:
			break;
		}
		return false;
	}

	public void setCountDownImageView(int imageResourceId) {
		ivCountDownClock.setImageResource(imageResourceId);
	}

	//	@Override
	//	public boolean onKey(View v, int keyCode, KeyEvent event) {
	//		// debugging
	//		Log.d("Return problem",
	//				"RP -- event.getAction() = " + event.getAction());
	//		switch (event.getAction()) {
	//		case KeyEvent.KEYCODE_ENTER:
	//			addTodoBar();
	//			break;
	//		default:
	//			break;
	//		}
	//		return false;
	//	}
}
