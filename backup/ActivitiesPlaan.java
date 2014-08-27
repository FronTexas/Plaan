package com.example.plaan;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

	long startHours;
	long startMinutes;

	long endHours;
	long endMinutes;

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
		startHours = in.readLong();
		startMinutes = in.readLong();
		endHours = in.readLong();
		endMinutes = in.readLong();
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
		startHours = -1;
		startMinutes = -1;
		endHours = -1;
		endMinutes = -1;
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
		startHours = -1;
		startMinutes = -1;
		endHours = -1;
		endMinutes = -1;
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
	public void setInterval(int startHours, int startMinutes, int endHours,
			int endMinutes, String startMeridiem, String endMeridiem) {
		if (this.type == TYPE_ONE_TIME) {
			this.startHours = startHours;
			this.startMinutes = startMinutes;

			this.endHours = endHours;
			this.endMinutes = endMinutes;

			int hoursInterval = endHours - startHours;
			int minutesInterval = endMinutes - startMinutes;

			this.interval = (hoursInterval * 60) + minutesInterval;
			if (interval <= 0) {
				int endHoursAdjusted = endHours + 24;
				hoursInterval = endHoursAdjusted - startHours;
				interval = (hoursInterval * 60) + minutesInterval;
			}
			interval *= 60000;
			// debugging
			Log.d("intervalError", "IE -- ActivitiesPlaan interval = "
					+ interval);
		} else if (this.type == TYPE_LOOPING) {
			throw new IllegalArgumentException(
					"setInterval pre-condition not met. type is not ONE_TIME");
		}
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
		int startHours = Integer.parseInt(startTime.substring(0, 2));
		int startMinutes = Integer.parseInt(startTime.substring(3));
		int endHours = Integer.parseInt(endTime.substring(0, 2));
		int endMinutes = Integer.parseInt(endTime.substring(3));

		if (this.type == TYPE_ONE_TIME) {
			this.startHours = startHours;
			this.startMinutes = startMinutes;

			this.endHours = endHours;
			this.endMinutes = endMinutes;

			int hoursInterval = endHours - startHours;
			int minutesInterval = endMinutes - startMinutes;

			this.interval = (hoursInterval * 60) + minutesInterval;
			if (interval <= 0) {
				int endHoursAdjusted = endHours + 24;
				hoursInterval = endHoursAdjusted - startHours;
				interval = (hoursInterval * 60) + minutesInterval;
			}
			interval *= 60000;
		} else if (this.type == TYPE_LOOPING) {
			throw new IllegalArgumentException(
					"setInterval pre-condition not met. type is not ONE_TIME");
		}
	}

	/**
	 * * PRE : type == TYPE_LOOPING
	 * 
	 * @param start
	 *            = the start time in the form of xxxxAM or xxxxPM
	 */
	public void setInterval(int startHours, int startMinutes, int loops,
			int loopTime, int loopType, int breakTime, int breakTimeType) {
		if (this.type == TYPE_LOOPING) {
			this.startHours = startHours;
			this.startMinutes = startMinutes;
			this.loops = loops;

			this.loopTime = loopTime;
			this.original_looptime = loopTime;
			this.loopTimeType = loopType;
			this.breakTime = breakTime;
			this.original_breaktime = breakTime;
			this.breakType = breakTimeType;

			switch (loopType) {
			case LOOPTYPE_MINUTE:
				interval = loops * (loopTime + breakTime); // time is in minutes
				interval *= 60000;
				this.loopTime *= 60000;
				break;
			case LOOPTYPE_HOUR:
				interval = loops * (loopTime * 60 + breakTime); // time is in
				interval *= 60000;
				this.loopTime *= 1000;
				break;
			}
			this.original_looptime = this.loopTime;

			switch (breakTimeType) {
			case LOOPTYPE_MINUTE:
				this.breakTime *= 60000;
				break;
			case LOOPTYPE_HOUR:
				this.breakTime *= 1000;
			}
			this.original_breaktime = this.breakTime;

			long[] endHours = new long[1];
			long[] endMinutes = new long[1];
			long[] hoursIncreased = new long[1];
			endMinutes[0] = startMinutes + (interval / (1000 * 60));

			checkForMoreThan(endMinutes, 60, hoursIncreased);
			endHours[0] += startHours + hoursIncreased[0];
			checkForMoreThan(endHours, 24, hoursIncreased);

			this.endHours = endHours[0];
			this.endMinutes = endMinutes[0];

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
		String startTime = addZeros("" + startHours) + ":"
				+ addZeros("" + startMinutes);
		return startTime;
	}

	public void addStartTime(int hoursExtra, int minutesExtra) {
		startHours += hoursExtra;
		startMinutes += minutesExtra;

		long[] hours = new long[1];
		hours[0] = startHours;

		long[] minutes = new long[1];
		minutes[0] = startMinutes;

		long[] hoursIncreased = new long[1];

		checkForMoreThan(minutes, 60, hoursIncreased);
		hours[0] += hoursIncreased[0];
		checkForMoreThan(hours, 24, hoursIncreased);

		startHours = hours[0];
		startMinutes = minutes[0];
	}

	public void addEndTime(int hoursExtra, int minutesExtra) {
		endHours += hoursExtra;
		endMinutes += minutesExtra;

		long[] hours = new long[1];
		hours[0] = endHours;

		long[] minutes = new long[1];
		minutes[0] = endMinutes;

		long[] hoursIncreased = new long[1];

		checkForMoreThan(minutes, 60, hoursIncreased);
		hours[0] += hoursIncreased[0];
		checkForMoreThan(hours, 24, hoursIncreased);

		endHours = hours[0];
		endMinutes = minutes[0];
	}

	public String getEndTime() {
		String endTime = addZeros("" + endHours) + ":"
				+ addZeros("" + endMinutes);
		return endTime;
	}

	public String getStartHours() {
		return addZeros("" + startHours);
	}

	public String getStartMinutes() {
		return addZeros("" + startMinutes);
	}

	public String getEndHours() {
		return addZeros("" + endHours);
	}

	public String getEndMinutes() {
		return addZeros("" + endMinutes);
	}

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
		out.writeLong(startHours);
		out.writeLong(startMinutes);
		out.writeLong(endHours);
		out.writeLong(endMinutes);
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
