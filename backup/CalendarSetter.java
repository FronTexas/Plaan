package com.example.plaan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.widget.TextView;

public class CalendarSetter {
	private HashMap<Integer, String> days;
	Calendar cal;
	String day;
	SimpleDateFormat df;
	String date;
		
	public CalendarSetter(){
		initializeDays();
		cal = Calendar.getInstance();
		day = days.get(cal.get(Calendar.DAY_OF_WEEK));
		df = new SimpleDateFormat("dd MMM yyyy");
		date = df.format(cal.getTime());
	}


	private void initializeDays() {
		days = new HashMap<>();
		days.put(1, "Sunday");
		days.put(2, "Monday");
		days.put(3, "Tuesday");
		days.put(4, "Wed");
		days.put(5, "Thursday");
		days.put(6, "Friday");
		days.put(7, "Saturday");
	}
	
	public void setCalendar(TextView tvToday,TextView tvTodaysDate){
		tvToday.setText(day + ",");
		tvTodaysDate.setText(date);
	}
}
