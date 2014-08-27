package com.example.plaan;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToyMain {
	public static void main(String[] args){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String fData = df.format(c.getTime());
		System.out.println(fData);
	}
}
