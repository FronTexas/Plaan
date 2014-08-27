package com.example.plaan;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TodoPage extends Activity implements OnEditorActionListener,
		OnClickListener {

	ArrayList<ActivitiesPlaan> activitiesList;

	float scaleDP;
	EditText etActivities;
	RelativeLayout todoPageLayout;
	RelativeLayout rlForETActivities;
	TextView tvTodayText;
	TextView tvTodaysDateText;
	TextView tvMinutesLeft;
	TextView tvMin;
	ImageButton bNextButton;
	int uniqueETid;

	int timeRemaining;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_page);
		activitiesList = new ArrayList<ActivitiesPlaan>();
		uniqueETid = 0;
		scaleDP = getApplicationContext().getResources().getDisplayMetrics().density;

		initializeViews();
		retreiveBundles();
		setTypefaces();
		setDates();

		etActivities.setOnEditorActionListener(this);
		bNextButton.setOnClickListener(this);

	}

	private void retreiveBundles() {
		Bundle extras = getIntent().getExtras();
		timeRemaining = extras.getInt("timeRemaining");
		tvMinutesLeft.setText("" + timeRemaining);
	}

	private void setDates() {
		CalendarSetter cs = new CalendarSetter();
		cs.setCalendar(tvTodayText, tvTodaysDateText);
	}

	private void setTypefaces() {
		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(etActivities, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvTodaysDateText, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvTodayText, tfp.TITILIUM);
		tfp.setTypeface(tvMinutesLeft, tfp.LEAGUE_GOTHIC);
		tfp.setTypeface(tvMin, tfp.LEAGUE_GOTHIC);
	}

	private void initializeViews() {
		tvMinutesLeft = (TextView) findViewById(R.id.tvMinutesLeft);
		tvMin = (TextView) findViewById(R.id.tvMin);
		etActivities = (EditText) findViewById(R.id.etActivities);
		todoPageLayout = (RelativeLayout) findViewById(R.id.rlTodoPage);
		rlForETActivities = (RelativeLayout) findViewById(R.id.rlForETActivities);
		tvTodayText = (TextView) findViewById(R.id.tvToday);
		tvTodaysDateText = (TextView) findViewById(R.id.tvTodaysDate);
		bNextButton = (ImageButton) findViewById(R.id.bNextButton);
	}

	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_NEXT) {
			upperCaseFirstLetter(view);
			int etWidth = (int) (340 * scaleDP + 0.5f);
			int etHeight = (int) (45 * scaleDP + 0.5f);
			EditText newEditText = createNewEditText(etWidth, etHeight);

			LayoutParams paramsForNewEditText = new LayoutParams(etWidth,
					etHeight);
	
			if (view.getId() == R.id.etActivities) {
				paramsForNewEditText.addRule(RelativeLayout.BELOW,
						R.id.etActivities);
				paramsForNewEditText.addRule(RelativeLayout.ALIGN_LEFT,
						R.id.etActivities);
			} else {
				paramsForNewEditText.addRule(RelativeLayout.BELOW,
						newEditText.getId() - 1);
				paramsForNewEditText.addRule(RelativeLayout.ALIGN_LEFT,
						newEditText.getId() - 1);
			}

			paramsForNewEditText.setMargins(0, (int) (10 * scaleDP + 0.5f), 0, 0);
			setNewEditTextProperties(newEditText);

			rlForETActivities.addView(newEditText, paramsForNewEditText);
			uniqueETid++;
		}
		return false;
	}

	private void upperCaseFirstLetter(TextView view) {
		if (view.getText().toString().length() >= 1) {
			String firstLetter = "" + view.getText().toString().charAt(0);
			view.setText(firstLetter.toUpperCase(Locale.ENGLISH)
					+ view.getText().toString().substring(1));
		}
	}

	private EditText createNewEditText(int etWidth, int etHeight) {
		// create new EditText
		EditText newEditText = new EditText(this);
		newEditText.setWidth(etWidth);
		newEditText.setHeight(etHeight);
		String newEditTextId = "111" + uniqueETid;
		newEditText.setId(Integer.parseInt(newEditTextId));

		return newEditText;
	}

	private void setNewEditTextProperties(EditText newEditText) {
		newEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		newEditText.setSingleLine();
		newEditText.setBackgroundColor(Color.rgb(82, 148, 165));
		newEditText.setPadding((int) (10 * scaleDP + 0.5f), 0, 0, 0);
		newEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
		newEditText.setTextColor(Color.rgb(242, 242, 242));
		newEditText.setOnEditorActionListener(this);

		TypefacePlaan tfp = new TypefacePlaan();
		tfp.setTypeface(newEditText, tfp.LEAGUE_GOTHIC);

		Animation comeIn = AnimationUtils.loadAnimation(
				getApplicationContext(), android.R.anim.slide_in_left);
		newEditText.setAnimation(comeIn);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bNextButton:
			scanEditText();
			Intent i = new Intent("com.example.plaan.SETTIMEPAGE");
			Bundle extras = new Bundle();
			extras.putInt("timeRemaining", timeRemaining);
			extras.putParcelableArrayList("activitiesList", activitiesList);
			i.putExtras(extras);
			startActivity(i);
			break;
		}
	}

	private void scanEditText() {
		EditText addedEditText = etActivities;
		int newEditTextId = 1109;
		while (addedEditText != null) {
			if (addedEditText.getText().toString().length() >= 1) {
				// if the et has activities name on it
				activitiesList.add(new ActivitiesPlaan(addedEditText.getText()
						.toString()));
			}
			newEditTextId++;
			addedEditText = (EditText) findViewById(newEditTextId);
		}
	}

}
