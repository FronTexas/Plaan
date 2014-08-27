package com.example.plaan;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class ActivitiesPlaan implements Parcelable {
	public static int TYPE_NONE = -1;
	public static int TYPE_ONE_TIME = 1;
	public static int TYPE_LOOPING = 2;
	final int MERIDIEM_ADJUSTMENT = 1200;
	final int ONE_HOUR = 60;
	public final static int LOOPTYPE_MINUTE = 1;
	public final static int LOOPTYPE_HOUR = 2;

	public static int BREAK_STATE = 7;
	public static int LOOPING_STATE = 8;

	String name;
	int type;
	ActivityCard activityCard;

	long startTime;
	String startTime_string;

	long endTime;
	String endTime_string;

	long loops;
	long loopTime;
	long original_looptime;
	long original_breaktime;
	long loopTimeType;
	long breakTime;
	long breakType;
	long interval;

	int id;

	int state;

	public static final Parcelable.Creator<ActivitiesPlaan> CREATOR = new Parcelable.Creator<ActivitiesPlaan>() {
		public ActivitiesPlaan createFromParcel(Parcel in) {
			return new ActivitiesPlaan(in);
		}

		public ActivitiesPlaan[] newArray(int size) {
			return new ActivitiesPlaan[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private ActivitiesPlaan(Parcel in) {
		name = in.readString();
		type = in.readInt();
		startTime = in.readLong();
		startTime_string = in.readString();
		endTime = in.readLong();
		endTime_string = in.readString();
		loops = in.readLong();
		loopTime = in.readLong();
		loopTimeType = in.readLong();
		breakTime = in.readLong();
		breakType = in.readLong();
		interval = in.readLong();
		state = in.readInt();
		id = in.readInt();
		original_looptime = in.readLong();
		original_breaktime = in.readLong();
	}

	public ActivitiesPlaan(String name, int type) {
		this.name = name;
		this.type = type;
		startTime = -1;
		endTime = -1;
		loops = -1;
		loopTime = -1;
		loopTimeType = -1;
		breakTime = -1;
		breakType = -1;
		breakType = -1;
		state = LOOPING_STATE;
		this.interval = 0;
		id = 0;
	}

	public ActivitiesPlaan(String name) {
		this.name = name;
		startTime = -1;
		endTime = -1;
		loops = -1;
		loopTime = -1;
		loopTimeType = -1;
		breakTime = -1;
		breakType = -1;
		breakType = -1;
		state = LOOPING_STATE;
		this.interval = 0;
		id = 0;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setActivityCard(ActivityCard activityCard) {
		this.activityCard = activityCard;
	}

	public ActivityCard getActivityCard() {
		return activityCard;
	}

	public int getActivityCardId() {
		return activityCard.getId();
	}

	public void setType(int type) {
		this.type = type;

	}

	public String getTypeString() {
		if (type == TYPE_ONE_TIME)
			return "One Time";
		else
			return "Looping";
	}

	public int getType() {
		return type;
	}

	public String getLoopType() {
		if (loopTimeType == LOOPTYPE_MINUTE)
			return "MIN";
		else
			return "HRS";
	}

	public String getBreakTimeType() {
		if (breakType == LOOPTYPE_MINUTE)
			return "MIN";
		else
			return "HRS";
	}

	public int getCurrentState() {
		return state;
	}

	/**
	 * * PRE : type == ONE_TIME
	 * 
	 * @param start
	 *            = the start time in the form of xxxxAM or xxxxPM
	 * @param end
	 *            = the end time in the form of xxxxAM or xxxxPM * POST :
	 *            startTime,endTime,and interval are set.
	 */
	public void setInterval(String startTime, String endTime) {
		if (this.type == TYPE_ONE_TIME) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			Date start = null;
			Date end = null;
			try {
				start = format.parse(startTime);
				end = format.parse(endTime);
				this.startTime = start.getTime();
				this.endTime = end.getTime();
				startTime_string = startTime;
				endTime_string = endTime;
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.interval = this.endTime - this.startTime;

			if (interval <= 0) {
				try {
					format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
					startTime = "01/01/2001 " + startTime;
					endTime = "01/02/2001 " + endTime;
					start = format.parse(startTime);
					end = format.parse(endTime);
					this.startTime = start.getTime();
					this.endTime = end.getTime();
					interval = this.endTime - this.startTime;
				} catch (Exception e) {
					Log.d("new time calculation error",
							"NTCE -- CAUGHT EXCEPTION = " + e.toString());
					e.printStackTrace();
				}
			}
		} else if (this.type == TYPE_LOOPING) {
			throw new IllegalArgumentException(
					"setInterval pre-condition not met. type is not ONE_TIME");
		}
	}

	/**
	 * * PRE : type == TYPE_LOOPING
	 * 
	 * @param startTime
	 *            a String denote the startTime of this activity in format HH:mm
	 * @param loops
	 *            number of loops
	 * @param loopTime
	 *            The time for each loop running. Unit is in millisseconds.
	 * @param loopType
	 *            Looping type. It's either minutes or hours.
	 * @param breakTime
	 *            The time for each break running. Unit is in milliseconds.
	 * @paraam breakTimeType Break type. It's either minutes or hours.
	 */
	public void setInterval(String startTime, int loops, long loopTime,
			int loopType, long breakTime, int breakTimeType) {
		if (this.type == TYPE_LOOPING) {
			Date start = null;
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			try {
				start = format.parse(startTime);
				this.startTime = start.getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.loops = loops;
			this.loopTime = loopTime;
			this.original_looptime = loopTime;
			this.loopTimeType = loopType;

			this.breakTime = breakTime;
			this.original_breaktime = breakTime;
			this.breakType = breakTimeType;

			// interval is in milliseconds
			interval = loops * (loopTime + breakTime);

			endTime = this.startTime + interval;

			startTime_string = startTime;
			Date end = new Date(endTime);
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			end.setTime(endTime);
			endTime_string = formatter.format(end);

		} else if (this.type == TYPE_ONE_TIME) {
			throw new IllegalArgumentException(
					"setInterval pre-condition not met. type is not REPETITIVE");
		}
	}

	public void resetLoopTime() {
		loopTime = original_looptime;
	}

	public void resetBreakTime() {
		breakTime = original_breaktime;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	private void checkForMoreThan(long[] time, int usualNumber,
			long[] hoursIncreased) {
		while (time[0] >= usualNumber) {
			time[0] -= usualNumber;
			hoursIncreased[0]++;
		}
	}

	public long getInterval() {
		// return (this.interval % 100) + (this.interval / 100) * ONE_HOUR;
		return interval;

	}

	public int getState() {
		return state;
	}

	public String getStartTime() {
		return startTime_string;
	}

	public void addStartTime(long millisExtra) {
		startTime += millisExtra;
		Date date = new Date(startTime);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		startTime_string = formatter.format(date);
	}

	public void addEndTime(long millisExtra) {
		endTime += millisExtra;
		Date date = new Date(endTime);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		endTime_string = formatter.format(date);
	}

	public String getEndTime() {
		return endTime_string;
	}

	//	public String getStartHours() {
	//		return addZeros("" + startHours);
	//	}
	//
	//	public String getStartMinutes() {
	//		return addZeros("" + startMinutes);
	//	}
	//
	//	public String getEndHours() {
	//		return addZeros("" + endHours);
	//	}
	//
	//	public String getEndMinutes() {
	//		//		return addZeros("" + endMinutes);
	//	}

	public void decreaseLoops() {
		loops--;
	}

	public long getLoopLeft() {
		return loops;
	}

	public long loops() {
		return loops;
	}

	public long loopTime() {
		return loopTime;
	}

	public long breakTime() {
		return breakTime;
	}

	public void alterState(int previousState) {
		if (previousState == LOOPING_STATE)
			state = BREAK_STATE;
		else
			state = LOOPING_STATE;
	}

	private String addZeros(String s) {
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeInt(type);
		out.writeLong(startTime);
		out.writeString(startTime_string);
		out.writeLong(endTime);
		out.writeString(endTime_string);
		out.writeLong(loops);
		out.writeLong(loopTime);
		out.writeLong(loopTimeType);
		out.writeLong(breakTime);
		out.writeLong(breakType);
		out.writeLong(interval);
		out.writeInt(state);
		out.writeInt(id);
		out.writeLong(original_looptime);
		out.writeLong(original_breaktime);
	}

	@Override
	public boolean equals(Object o) {
		ActivitiesPlaan otherActivites = (ActivitiesPlaan) o;
		return otherActivites.getActivityCard().getTag() == this
				.getActivityCard().getTag();
	}

}
