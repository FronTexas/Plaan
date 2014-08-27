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

import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment.TimePickerDialogHandler;
import com.zenkun.datetimepicker.time.RadialPickerLayout;
import com.zenkun.datetimepicker.time.TimePickerDialog;
import com.zenkun.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

@SuppressLint("ViewConstructor")
public class ActivityCard extends RelativeLayout implements OnClickListener,
		OnTimeSetListener, TextWatcher, OnEditorActionListener,
		TimePickerDialogHandler {

	TypefacePlaan tfp;

	// root
	LinearLayout llRootActivityCard;

	// parentScrollView
	ScrollView parentScrollView;

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
			tvEndHoursLooping, tvX, tvLoopingTimeType, tvBreakTimeType;
	EditText etLoopingFreq, etLoopingTime, etBreakTime;

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
		tfp.setTypeface(etLoopingTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(etBreakTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvLoopingTimeType, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvBreakTimeType, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvIntervalLoopCD, TypefacePlaan.LEAGUE_GOTHIC);
	}

	public void setOneTimeTypefaces() {
		tfp.setTypeface(tvOneTime, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);
		tfp.setTypeface(tvLooping, TypefacePlaan.LEAGUE_GOTHIC_ITALIC);
		tfp.setTypeface(tvStartOneTime, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvStartHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvEndOneTime, TypefacePlaan.OPEN_SANS_BOLD);
		tfp.setTypeface(tvEndHoursOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(etTimeOneTime, TypefacePlaan.LEAGUE_GOTHIC);
		tfp.setTypeface(tvTimeTypeOneTime, TypefacePlaan.LEAGUE_GOTHIC);
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
			//			TimePickerBuilder btp = new TimePickerBuilder().setFragmentManager(
			//					fragmentManager).setStyleResId(
			//					R.style.BetterPickersDialogFragment);
			//			btp.addTimeSetListener(this);
			//			btp.show();

			//			HmsPickerBuilder hpb = new HmsPickerBuilder().setFragmentManager(
			//					fragmentManager).setStyleResId(
			//					R.style.BetterPickersDialogFragment);
			//			hpb.show();

		} else if (v.getId() == ivToDoButton.getId()
				|| v.getId() == rlTaskLeftIcon.getId()) {
			// TODO_BUTTON / rlTaskLeft
			vgActivityCountDown.setVisibility(View.GONE);
			tvActivityNameTD.setText(theActivity.name);
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
			tvLoopingTimeCD.setText(etLoopingTime.getText().toString()
					+ tvLoopingTimeType.getText().toString());
			tvBreakTimeCD.setText(etBreakTime.getText().toString()
					+ tvBreakTimeType.getText().toString());
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
		int[] loopType = new int[1];

		long[] breakTime = new long[1];

		int[] breakTimeType = new int[1];
		retreiveLoopingIntervalParameters(startTime, loops, loopTime, loopType,
				breakTime, breakTimeType);

		theActivity.setInterval(startTime[0], loops[0], loopTime[0],
				loopType[0], breakTime[0], breakTimeType[0]);

	}

	private void setOneTimeProperties(ActivitiesPlaan theActivity) {
		theActivity.setInterval(tvStartHoursOneTime.getText().toString(),
				tvEndHoursOneTime.getText().toString());
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
			if (allOneTimePropertiesFilled()
					&& llOneTime.getVisibility() == View.VISIBLE)
				calculateOneTimeInterval();
		} else if (lastThingClicked == tvEndHoursOneTime.getId()) {
			// set the endHours of OneTime
			tvEndHoursOneTime.setText(addZeros("" + hourOfDay) + ":"
					+ addZeros("" + minute));
			tvEndHoursOneTime.setAlpha((float) 1.0);
			etTimeOneTime.setText("");
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
			if (tvStartHoursOneTime.getText().toString().length() > 0
					&& etTimeOneTime.getText().length() > 0)
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
		String[] startTime = new String[1];

		int[] loops = new int[1];
		long[] loopTime = new long[1];
		int[] loopType = new int[1];

		long[] breakTime = new long[1];

		int[] breakTimeType = new int[1];

		retreiveLoopingIntervalParameters(startTime, loops, loopTime, loopType,
				breakTime, breakTimeType);

		ActivitiesPlaan toyActivities = new ActivitiesPlaan("toy",
				ActivitiesPlaan.TYPE_LOOPING);
		toyActivities.setInterval(startTime[0], loops[0], loopTime[0],
				loopType[0], breakTime[0], breakTimeType[0]);

		// debugging
		Log.d("new time calculation error", "NTCE -- startTime = "
				+ startTime[0]);
		Log.d("new time calculation error", "NTCE -- loopTime = "
				+ (loopTime[0] / (60 * 1000) % 60));
		Log.d("new time calculation error", "NTCE -- breakTime = "
				+ (breakTime[0] / (60 * 1000) % 60));
		long interval = toyActivities.getInterval();
		Log.d("new time calculation error", "NTCE -- interval = "
				+ (interval / (60 * 60 * 1000) % 24) + ":"
				+ (interval / (60 * 1000) % 60));

		tvEndHoursLooping.setAlpha(1);
		tvEndHoursLooping.setText(toyActivities.getEndTime());
	}

	private void checkForMoreThan(int[] time, int usualNumber,
			int[] hoursIncreased) {
		while (time[0] >= usualNumber) {
			time[0] -= usualNumber;
			hoursIncreased[0]++;
		}
	}

	private void retreiveLoopingIntervalParameters(String[] startTime,
			int[] loops, long[] loopTime, int[] loopType, long[] breakTime,
			int[] breakTimeType) {
		startTime[0] = tvStartHoursLooping.getText().toString();

		String loopsString = etLoopingFreq.getText().toString();
		String loopTimeString = etLoopingTime.getText().toString();
		String breakTimeString = etBreakTime.getText().toString();

		loops[0] = Integer.parseInt(loopsString);

		loopTime[0] = Integer.parseInt(loopTimeString);
		String loopTypeString = tvLoopingTimeType.getText().toString();
		loopType[0] = checkForType(loopTypeString);

		if (loopType[0] == ActivitiesPlaan.LOOPTYPE_HOUR) {
			loopTime[0] *= 1000;
		} else {
			loopTime[0] *= 60000;
		}

		breakTime[0] = Integer.parseInt(breakTimeString);
		String breakTimeTypeString = tvBreakTimeType.getText().toString();
		breakTimeType[0] = checkForType(breakTimeTypeString);

		if (breakTimeType[0] == ActivitiesPlaan.LOOPTYPE_HOUR) {
			breakTime[0] *= 1000;
		} else {
			breakTime[0] *= 60000;
		}
		Log.d("new time calculation error",
				"NTCE -- breakTime(in retreiving) = "
						+ (breakTime[0] / (60 * 1000) % 60));

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

	@Override
	public void onDialogTimeSet(int hourOfDay, int minute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogCancel() {
		// TODO Auto-generated method stub

	}

	public void setActivitiesPlaan(ActivitiesPlaan theActivity) {
		this.theActivity = theActivity;
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
