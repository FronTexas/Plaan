package com.example.plaan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.view.ViewGroup;
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

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.zenkun.datetimepicker.time.RadialPickerLayout;
import com.zenkun.datetimepicker.time.TimePickerDialog;
import com.zenkun.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

@SuppressLint({ "ViewConstructor", "SimpleDateFormat" })
public class ActivityCard extends RelativeLayout implements OnClickListener,
		OnTimeSetListener, TextWatcher, OnEditorActionListener {

	TypefacePlaan tfp;

	/** The root in the xml */
	LinearLayout llRootActivityCard;

	/** A ScrollView coming from ScrollingUI. */
	ScrollView parentScrollView;

	/** The ActivitiesPlaan that correspons to this ActivityCard */
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
			tvEndOneTime, tvEndHoursOneTime, tvSetPicker_oneTime;

	// looping
	LinearLayout llLooping;
	LinearLayout llStartEndLooping, rlStartSectionLooping, rlEndSectionLooping;
	LinearLayout llLoopingSettings;
	TextView tvStartLooping, tvStartHoursLooping, tvEndLooping,
			tvEndHoursLooping, tvX, tvSetPicker_looping,
			tvSetPicker_breakLooping;
	EditText etLoopingFreq;

	// setButton
	RelativeLayout rlSET_Button;
	TextView tvSET;
	onSetClickListener osc;

	// ActivityCountDown
	AutoResizeTextView tvActivitiesNameCD;
	ViewGroup vgActivityCountDown;
	TextView tvCountDown, tvStartEndCD, tvIntervalOTCD, tvLoopingFreqCD, tvXCD,
			tvLoopingTimeCD, tvBreakTimeCD, tvActivityTypeCD, tvBreakText,
			tvDONE, tvIntervalLoopCD;
	LinearLayout llOneTimePropertiesCD, llLoopingPropertiesCD;
	ImageView ivToDoButton, ivBackToActivitySetter, ivCountDownClock;
	boolean backToActivtySetterClicked;

	// To Do
	RelativeLayout rlToDo;
	ViewGroup llToDoList, rlTaskLeftIcon;
	TextView tvToDo, tvActivityNameTD;
	ImageView ivArrowBack;
	ScrollView svTodo;
	TextView tvTaskLeft;
	ArrayList<String> taskList;
	final int todobar_idHeader = 112;
	int todobar_idCounter = 0;

	FragmentManager fragmentManager;
	Context context;

	int lastThingClicked;

	float scaleDP = getResources().getDisplayMetrics().density;
	LayoutInflater layoutInflater;

	public ActivityCard(Context context) {
		super(context);
	}

	public ActivityCard(Context context, FragmentManager fragmentManager,
			LayoutInflater layoutInflater, int tag, ScrollView parentScrollView) {
		super(context);
		tfp = new TypefacePlaan(context);
		// this.timeLeft = timeLeft;
		this.context = context;
		this.layoutInflater = layoutInflater;
		this.fragmentManager = fragmentManager;
		lastThingClicked = 0;
		//		scaleDP = getResources().getDisplayMetrics().density;
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

		// oneTime
		rlOneTimeText.setOnClickListener(this);
		rlLoopingText.setOnClickListener(this);
		tvStartHoursOneTime.setOnClickListener(this);
		tvEndHoursOneTime.setOnClickListener(this);
		tvSetPicker_oneTime.setOnClickListener(this);

		// looping
		tvStartHoursLooping.setOnClickListener(this);
		etLoopingFreq.addTextChangedListener(this);
		tvSetPicker_looping.setOnClickListener(this);
		tvSetPicker_breakLooping.setOnClickListener(this);

		// setbutton
		rlSET_Button.setOnClickListener(this);

		// activity count doiwn
		ivBackToActivitySetter.setOnClickListener(this);

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
		tfp.setTypeface(etActivityName, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);

		// one time
		setOneTimeTypefaces();

		// tvSET 
		tfp.setTypeface(tvSET, TypefacePlaan.LEAGUE_GOTHIC);

	}

	public void setActivityCountDownTypefaces() {
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
	}

	public void setLoopingTypefaces() {
		tfp.setTypeface(tvStartLooping, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvStartHoursLooping, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEndLooping, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvEndHoursLooping, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvStartHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvX, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(etLoopingFreq, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvSetPicker_looping, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvSetPicker_breakLooping, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvIntervalLoopCD, TypefacePlaan.LEAGUE_GOTHIC);
	}

	public void setOneTimeTypefaces() {
		tfp.setTypeface(tvOneTime, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);
		tfp.setTypeface(tvLooping, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);
		tfp.setTypeface(tvStartOneTime, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvStartHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEndOneTime, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvEndHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvSetPicker_oneTime, TypefacePlaan.LEAGUE_GOTHIC);
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
		tvSetPicker_looping = (TextView) findViewById(R.id.tvSetPicker_looping);
		tvSetPicker_breakLooping = (TextView) findViewById(R.id.tvSetPicker_breakLooping);
		llOneTime = (LinearLayout) findViewById(R.id.llOneTime);
		rlSET_Button = (RelativeLayout) findViewById(R.id.rlSET_Button);
		tvSET = (TextView) findViewById(R.id.tvSET);
		tvSetPicker_oneTime = (TextView) findViewById(R.id.tvSETpicker_oneTime);
		tvCountDown = (TextView) findViewById(R.id.tvCountDown);
		tvLoopingFreqCD = (TextView) findViewById(R.id.tvLoopingFreqCD);
		tvXCD = (TextView) findViewById(R.id.tvXCD);
		tvLoopingTimeCD = (TextView) findViewById(R.id.tvLoopingTimeCD);
		tvBreakTimeCD = (TextView) findViewById(R.id.tvBreakTimeCD);
		tvActivityTypeCD = (TextView) findViewById(R.id.tvActivityTypeCD);
		tvIntervalOTCD = (TextView) findViewById(R.id.tvIntervalOTCD);
		tvActivitiesNameCD = (AutoResizeTextView) findViewById(R.id.tvActivitiesNameCD);
		tvStartEndCD = (TextView) findViewById(R.id.tvStartEndCD);
		vgActivityCountDown = (RelativeLayout) findViewById(R.id.rlActivityCountDown);
		rlActivitySetter = (RelativeLayout) findViewById(R.id.rlActivitySetter);
		llLoopingPropertiesCD = (LinearLayout) findViewById(R.id.llLoopingPropertiesCD);
		llOneTimePropertiesCD = (LinearLayout) findViewById(R.id.llOneTimePropertiesCD);

		ivBackToActivitySetter = (ImageView) findViewById(R.id.ivBackToActivitySetter);
		tvBreakText = (TextView) findViewById(R.id.tvBreakText);
		tvDONE = (TextView) findViewById(R.id.tvDONE);
		tvIntervalLoopCD = (TextView) findViewById(R.id.tvIntervalLoopCD);
		ivCountDownClock = (ImageView) findViewById(R.id.ivCountDownClock);

		initializeToDo();

	}

	protected void initializeToDo() {
		rlToDo = (RelativeLayout) findViewById(R.id.rlToDo);
		llToDoList = (LinearLayout) findViewById(R.id.llToDoList);
		rlTaskLeftIcon = (RelativeLayout) findViewById(R.id.rlTaskLeftIcon);
		tvToDo = (TextView) findViewById(R.id.tvToDo);
		tvActivityNameTD = (TextView) findViewById(R.id.tvActivityNameTD);
		ivArrowBack = (ImageView) findViewById(R.id.ivArrowBack);
		svTodo = (ScrollView) findViewById(R.id.svTodo);
		tvTaskLeft = (TextView) findViewById(R.id.tvTaskLeft);
		ivToDoButton = (ImageView) findViewById(R.id.ivTodoButton);

		// To Do
		ivToDoButton.setOnClickListener(this);
		ivArrowBack.setOnClickListener(this);
		rlTaskLeftIcon.setOnClickListener(this);

		// To do
		tfp.setTypeface(tvToDo, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvActivityNameTD, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvTaskLeft, TypefacePlaan.OPEN_SANS_BOLD);

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
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvStartHoursOneTime
				|| v.getId() == R.id.tvStartHoursLooping) {
			// oneTime or Looping START HOURS
			if (v.getId() == R.id.tvStartHoursOneTime) {
				showTimePickerDialog(tvStartHoursOneTime);
			} else {
				showTimePickerDialog(tvStartHoursLooping);
			}

		} else if (v.getId() == R.id.tvEndHoursOneTime
				|| v.getId() == R.id.tvEndHoursLooping) {
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

		} else if (v.getId() == R.id.rlOneTimeText) {
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
		} else if (v.getId() == R.id.rlLoopingText) {
			// TYPE_LOOPING setter
			setLoopingTypefaces();

			rlLoopingText
					.setBackgroundResource(R.drawable.one_time_or_looping_blue_selection_bar);
			tvLooping
					.setTextColor(getResources().getColor(R.color.lightergrey));
			rlOneTimeText.setBackgroundColor(Color.TRANSPARENT);
			llLooping.setVisibility(View.VISIBLE);

			tvOneTime.setTextColor(getResources().getColor(R.color.darkgrey));
			llOneTime.setVisibility(View.INVISIBLE);
			activity_type = ActivitiesPlaan.TYPE_LOOPING;
		} else if (v.getId() == R.id.rlSET_Button) {
			// SET_BUTTON : set theActivity properties based on the settings
			// timeleftCD.cancel();
			// rlTimeLeftClock.setVisibility(View.INVISIBLE);

			setActivityCountDownTypefaces();

			// initialize theActivity
			theActivity = new ActivitiesPlaan(etActivityName.getText()
					.toString(), activity_type);
			// Put properties, either OneTime or Looping to theActivity
			if (activity_type == ActivitiesPlaan.TYPE_ONE_TIME) {
				setOneTimeProperties(theActivity);
			} else if (activity_type == ActivitiesPlaan.TYPE_LOOPING) {
				setLoopingProperties(theActivity);
			}

			// Set theActivity's activityCard to this
			theActivity.setActivityCard(this);

			// Broadcast the listener. Used in ScrollingUI
			if (!backToActivtySetterClicked) {
				// if its the firstTime visit
				osc.onSetClick(theActivity, true, this);
			} else {
				// Adjust next activities startTime and endTime
				osc.onSetClick(theActivity, false, this);
			}

			// Unvisualize activity_setter
			rlActivitySetter.setVisibility(View.INVISIBLE);

			// Visualize countdown_page
			vgActivityCountDown.setVisibility(View.VISIBLE);

			// Set the Countdown_page properties
			setActivityCountDownProperties(theActivity);
		} else if (v.getId() == R.id.tvLoopingTimeType
				|| v.getId() == R.id.tvBreakTimeType
				|| v.getId() == R.id.tvTimeTypeOneTime) {
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
			vgActivityCountDown.setVisibility(View.GONE);
			tvActivityNameTD.setText(theActivity.getName());
			rlToDo.setVisibility(View.VISIBLE);

			// make sure no todo_bar already there
			if (todobar_idCounter == 0)
				addTodoBar();

		} else if (v.getId() == ivArrowBack.getId()) {
			// ARROW_BACK from TODO_PAGE to CD_PAGE
			rlToDo.setVisibility(View.GONE);
			vgActivityCountDown.setVisibility(View.VISIBLE);

			int totalToDo = countToDo();
			if (totalToDo > 0) {
				// inflate task_left_icon
				rlTaskLeftIcon.setVisibility(View.VISIBLE);
				// un-inflate todo_button
				ivToDoButton.setVisibility(View.INVISIBLE);
			}
			tvTaskLeft.setText("" + totalToDo);

		} else if (v.getId() == R.id.ivBackToActivitySetter) {
			// ARROW_BACK from CD_PAGE to SETTER_PAGE
			vgActivityCountDown.setVisibility(View.INVISIBLE);
			rlActivitySetter.setVisibility(View.VISIBLE);
			backToActivtySetterClicked = true;
		} else if (v.getId() == R.id.tvSETpicker_oneTime) {
			// Set Button in oneTime
			HmsPickerBuilder hpb = new HmsPickerBuilder()
					.setFragmentManager(fragmentManager)
					.setStyleResId(R.style.BetterPickersDialogFragment)
					.addHmsPickerDialogHandler(new MyCustomHandler())
					.setReference(R.id.tvSETpicker_oneTime);
			hpb.show();
		} else if (v.getId() == R.id.tvSetPicker_looping) {
			// Set Button for loopingTime
			HmsPickerBuilder hpb = new HmsPickerBuilder()
					.setFragmentManager(fragmentManager)
					.setStyleResId(R.style.BetterPickersDialogFragment)
					.addHmsPickerDialogHandler(new MyCustomHandler())
					.setReference(R.id.tvSetPicker_looping);
			hpb.show();
		} else if (v.getId() == R.id.tvSetPicker_breakLooping) {
			// Set Button for breakTime
			HmsPickerBuilder hpb = new HmsPickerBuilder()
					.setFragmentManager(fragmentManager)
					.setStyleResId(R.style.BetterPickersDialogFragment)
					.addHmsPickerDialogHandler(new MyCustomHandler())
					.setReference(R.id.tvSetPicker_breakLooping);
			hpb.show();
		}
		lastThingClicked = v.getId();
	}

	private void showTimePickerDialog(TextView tv) {
		TimePickerDialog timePickerDialog = null;
		Calendar calendar = Calendar.getInstance();
		if (tv.getAlpha() != 1.0) {
			timePickerDialog = TimePickerDialog.newInstance(this,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), false);
			timePickerDialog.show(fragmentManager, "timepicker");
		} else {
			int hourOfDay = Integer.parseInt(tv.getText().toString()
					.substring(0, 2));
			int minute = Integer.parseInt(tv.getText().toString().substring(3));
			timePickerDialog = TimePickerDialog.newInstance(this, hourOfDay,
					minute, false);
			timePickerDialog.show(fragmentManager, "timepicker");
		}
	}

	private int countToDo() {
		int counter = 0;
		for (int i = 0; i < llToDoList.getChildCount(); i++) {
			TodoBar tdb = (TodoBar) llToDoList.getChildAt(i);
			if (tdb.etTodo.getText().toString().length() > 0) {
				counter++;
			}
		}
		return counter;
	}

	private void addTodoBar() {
		TodoBar tdb = new TodoBar(getContext());
		tdb.requestFocus();
		//		tdb.setId(Integer.parseInt("" + todobar_idHeader + todobar_idCounter));
		LayoutParams lpForTdb = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		lpForTdb.setMargins(0, 100, 0, 0);
		todobar_idCounter++;
		tdb.etTodo.setOnEditorActionListener(this);
		Animation comeIn = AnimationUtils.loadAnimation(getContext(),
				android.R.anim.slide_in_left);
		tdb.setAnimation(comeIn);
		tdb.setLayoutParams(lpForTdb);
		llToDoList.addView(tdb);
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
			tvLoopingTimeCD.setText(tvSetPicker_looping.getText().toString());
			tvBreakTimeCD
					.setText(tvSetPicker_breakLooping.getText().toString());
			tvActivityTypeCD.setText("Looping");
			llOneTimePropertiesCD.setVisibility(View.INVISIBLE);

			formatMillis(format, theActivity.loopTime);

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
		String[] startTime = new String[1];

		int[] loops = new int[1];
		long[] loopTime = new long[1];

		long[] breakTime = new long[1];

		retreiveLoopingIntervalParameters(startTime, loops, loopTime, breakTime);

		theActivity.setInterval(startTime[0], loops[0], loopTime[0],
				breakTime[0]);

	}

	private void setOneTimeProperties(ActivitiesPlaan theActivity) {
		theActivity.setInterval(tvStartHoursOneTime.getText().toString(),
				tvEndHoursOneTime.getText().toString());
	}

	@Override
	public void onTimeSet(RadialPickerLayout v, int hourOfDay, int minute) {
		if (lastThingClicked == tvStartHoursOneTime.getId()) {
			// set the starHours of OneTime
			tvStartHoursOneTime.setText(addZeros("" + hourOfDay) + ":"
					+ addZeros("" + minute));
			tvStartHoursOneTime.setAlpha((float) 1.0);
			if (allOneTimePropertiesFilled()
					&& llOneTime.getVisibility() == View.VISIBLE)
				calculateOneTimeInterval();
		} else if (lastThingClicked == tvEndHoursOneTime.getId()) {
			// set the endHours of OneTime
			tvEndHoursOneTime.setText(addZeros("" + hourOfDay) + ":"
					+ addZeros("" + minute));
			tvEndHoursOneTime.setAlpha((float) 1.0);
			tvSetPicker_oneTime.setText("SET");
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
				&& !tvSetPicker_oneTime.getText().toString().equals("SET")
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

	}

	@SuppressWarnings("deprecation")
	private void calculateOneTimeInterval() {
		long interval = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		String tvSetPicker_string = tvSetPicker_oneTime.getText().toString();
		if (tvSetPicker_string.length() == 10) {
			// HH:mm:ss formatter
			formatter = new SimpleDateFormat("HH:mm:ss");
		} else if (tvSetPicker_string.length() == 5) {
			// mm:ss formatter
			formatter = new SimpleDateFormat("mm:ss");
		} else if (tvSetPicker_string.length() == 2) {
			// ss formatter
			formatter = new SimpleDateFormat("ss");
		}

		Date interval_date = null;
		if (!tvSetPicker_string.equals("SET")) {
			// If the tvSetPicker is already setted
			try {
				// parse the tvSetPicker text to make it a Date object
				interval_date = formatter.parse(tvSetPicker_oneTime.getText()
						.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// set the interval
			interval = interval_date.getHours() * 3600000
					+ interval_date.getMinutes() * 60000
					+ interval_date.getSeconds() * 1000;
		}

		formatter = new SimpleDateFormat("HH:mm");
		long startHours = 0;
		try {
			// parse startHours
			Date startDate = formatter.parse(tvStartHoursOneTime.getText()
					.toString());
			startHours = startDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Set up the endHours
		long endHours = startHours + interval;
		Date endHoursDate = new Date(endHours);
		tvEndHoursOneTime.setText(formatter.format(endHoursDate));
		tvEndHoursOneTime.setAlpha(1);
	}

	private void calculateLoopingInterval() {
		String[] startTime = new String[1];

		int[] loops = new int[1];
		long[] loopTime = new long[1];

		long[] breakTime = new long[1];

		retreiveLoopingIntervalParameters(startTime, loops, loopTime, breakTime);

		ActivitiesPlaan toyActivities = new ActivitiesPlaan("toy",
				ActivitiesPlaan.TYPE_LOOPING);
		toyActivities.setInterval(startTime[0], loops[0], loopTime[0],
				breakTime[0]);

		tvEndHoursLooping.setAlpha(1);
		tvEndHoursLooping.setText(toyActivities.getEndTime());
	}

	@SuppressWarnings("deprecation")
	private void retreiveLoopingIntervalParameters(String[] startTime,
			int[] loops, long[] loopTime, long[] breakTime) {
		startTime[0] = tvStartHoursLooping.getText().toString();

		String loopsString = etLoopingFreq.getText().toString();
		String loopTimeString = tvSetPicker_looping.getText().toString();
		SimpleDateFormat formatter = null;

		if (loopTimeString.length() == 8) {
			// HH:mm:ss formatter
			Log.d("Resetting time error", "RTE -- Getting into 8 formatter");
			formatter = new SimpleDateFormat("HH:mm:ss");
		} else if (loopTimeString.length() == 5) {
			// mm:ss formatter
			formatter = new SimpleDateFormat("mm:ss");
		} else if (loopTimeString.length() == 2) {
			// ss formatter
			formatter = new SimpleDateFormat("ss");
		}

		Date loopTime_date = null;
		Log.d("Resetting time error", "RTE -- loopTimeString = "
				+ loopTimeString);
		Log.d("Resetting time error", "RTE -- loopTimeString.length = "
				+ loopTimeString.length());
		Log.d("Resetting time error", "RTE -- formatter = " + formatter);
		try {
			loopTime_date = formatter.parse(loopTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		loopTime[0] = loopTime_date.getHours() * 3600000
				+ loopTime_date.getMinutes() * 60000
				+ loopTime_date.getSeconds() * 1000;
		loops[0] = Integer.parseInt(loopsString);

		String breakTimeString = tvSetPicker_breakLooping.getText().toString();

		if (breakTimeString.length() == 8) {
			// HH:mm:ss formatter
			formatter = new SimpleDateFormat("HH:mm:ss");
		} else if (breakTimeString.length() == 5) {
			// mm:ss formatter
			formatter = new SimpleDateFormat("mm:ss");
		} else if (breakTimeString.length() == 2) {
			// ss formatter
			formatter = new SimpleDateFormat("ss");
		}

		Date breakTime_date = null;
		try {
			breakTime_date = formatter.parse(breakTimeString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		breakTime[0] = breakTime_date.getHours() * 3600000
				+ breakTime_date.getMinutes() * 60000
				+ breakTime_date.getSeconds() * 1000;
	}

	private boolean allLoopingPropertiesFilled() {
		return (etLoopingFreq.getText().toString().length() != 0)
				&& (!tvSetPicker_looping.getText().toString().equals("SET"))
				&& (!tvSetPicker_breakLooping.getText().toString()
						.equals("SET"));
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

	public void setActivitiesPlaan(ActivitiesPlaan theActivity) {
		this.theActivity = theActivity;
	}

	class MyCustomHandler implements
			HmsPickerDialogFragment.HmsPickerDialogHandler {
		@Override
		public void onDialogHmsSet(int reference, int hours, int minutes,
				int seconds) {
			if (reference == R.id.tvSETpicker_oneTime) {
				// SET for oneTime

				// the tvSetpicker text only will be temporary
				tvSetPicker_oneTime
						.setText(addZeros("" + hours) + ":"
								+ addZeros("" + minutes) + ":"
								+ addZeros("" + seconds));
				if (activity_type == ActivitiesPlaan.TYPE_ONE_TIME
						&& tvStartHoursOneTime.getText().toString().length() > 0
						&& !tvSetPicker_oneTime.getText().toString()
								.equals("SET")) {
					// etTimeOneTime is listened
					calculateOneTimeInterval();
				}

				// Update the tvsetpicker text to the better format
				setCorrectTimeFormatText(tvSetPicker_oneTime, hours, minutes,
						seconds);
			} else if (reference == R.id.tvSetPicker_looping) {
				// SET for LoopingTime

				// the tvSetpicker text only will be temporary
				tvSetPicker_looping
						.setText(addZeros("" + hours) + ":"
								+ addZeros("" + minutes) + ":"
								+ addZeros("" + seconds));
				if (activity_type == ActivitiesPlaan.TYPE_LOOPING
						&& allLoopingPropertiesFilled()) {
					calculateLoopingInterval();
				}

				// Update the tvsetpicker text to the better format
				setCorrectTimeFormatText(tvSetPicker_looping, hours, minutes,
						seconds);
			} else if (reference == R.id.tvSetPicker_breakLooping) {
				// SET for breakTime

				// the tvSetpicker text only will be temporary
				tvSetPicker_breakLooping
						.setText(addZeros("" + hours) + ":"
								+ addZeros("" + minutes) + ":"
								+ addZeros("" + seconds));
				if (activity_type == ActivitiesPlaan.TYPE_LOOPING
						&& allLoopingPropertiesFilled()) {
					calculateLoopingInterval();
				}

				// Update the tvsetpicker text to the better format
				setCorrectTimeFormatText(tvSetPicker_breakLooping, hours,
						minutes, seconds);
			}
		}

		private void setCorrectTimeFormatText(TextView tv, int hours,
				int minutes, int seconds) {
			if (hours != 0) {
				// HH:mm:ss
				tv.setText(addZeros("" + hours) + ":" + addZeros("" + minutes)
						+ ":" + addZeros("" + seconds));
			} else if (hours == 0 && minutes != 0) {
				// mm:ss
				tv.setText(addZeros("" + minutes) + ":"
						+ addZeros("" + seconds));
			} else {
				// ss with 's' in the end
				tv.setText(addZeros("" + seconds) + "s");
			}
			tv.setAlpha(1);
		}
	}

}
