package com.example.capp;

import java.text.DecimalFormat;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable, Comparable{

	String eventName, location, descrip, calendar, color;
	int startTimeHour, startTimeMin, endTimeHour, endTimeMin;
	int[] startDate = new int[3]; //month, day, year
	int allDay;
	int remind;
	int priv; //private or public event
	int[] endDate = new int[3]; //month, day, year
	long id = -1;
	DecimalFormat format = new DecimalFormat("00");

	public Event() {
		eventName = "event";
		location = "location";
		descrip = "description";
		startTimeHour = 12;
		startTimeMin = 0;
		endTimeHour = 12;
		endTimeMin = 1;
		startDate[0] = 1;
		startDate[1] = 1;
		startDate[2] = 2011;
		allDay = 0; //not all-day (1 is all day)
		remind = -1; //no reminder
		priv = 1; //private;
		endDate[0] = 1;
		endDate[1] = 1;
		endDate[2] = 2012;
		calendar = "Default";
		color = "Red";
	}
	public Event(String eventName, String location, String descrip, int sth, int stm, int eth,
			int etm, int month, int day, int year, int allDay, int reminder,
			int priv, int endMonth, int endDay, int endYear, String cal, String col, long id) {
		this.eventName = eventName;
		this.location = location;
		this.descrip = descrip;
		startTimeHour = sth;
		startTimeMin = stm;
		endTimeHour = eth;
		endTimeMin = etm;
		startDate[0] = month;
		startDate[1] = day;
		startDate[2] = year;
		this.allDay = allDay;
		remind = reminder; //remind 'reminder' minutes before the event
		this.priv = priv;
		endDate[0] = endMonth;
		endDate[1] = endDay;
		endDate[2] = endYear;
		calendar = cal;
		color = col;
		this.id = id;
	}

	public void setCalendar(String cal) { 
		calendar = cal;
	}
	public String getCalendar() { 
		return calendar;
	}
	public void setColor(String col) { 
		color = col;
	}
	public String getColor() { 
		return color;
	}
	
	public int getPriv() { 
		return priv;
	}
	
	public void setPriv(int priv) {
		this.priv = priv;
	}
	
	public int getRemind() { 
		return remind;
	}
	public long getID() { 
		return id;
	}
	public void setID(long entry_id) {
		this.id = entry_id;
	}

	public void setRemind(int reminder) { 
		remind = reminder;
	}

	public int getAllDay() { 
		return allDay;
	}

	public void setAllDay(int allDay) { 
		this.allDay = allDay;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public int[] getStartDate() {
		return startDate;
	}

	public void setStartDate(int month, int day, int year) {
		startDate[0] = month;
		startDate[1] = day;
		startDate[2] = year;
	}
	
	public int[] getEndDate() {
		return endDate;
	}

	public void setEndDate(int month, int day, int year) {
		endDate[0] = month;
		endDate[1] = day;
		endDate[2] = year;
	}

	public int getStartTimeHour() {
		return startTimeHour;
	}

	public void setStartTimeHour(int startTime) {
		this.startTimeHour = startTime;
	}

	public int getEndTimeHour() {
		return endTimeHour;
	}

	public void setEndTimeHour(int endTime) {
		this.endTimeHour = endTime;
	}
	public int getStartTimeMin() {
		return startTimeMin;
	}

	public void setStartTimeMin(int startTime) {
		this.startTimeMin = startTime;
	}

	public int getEndTimeMin() {
		return endTimeMin;
	}

	public void setEndTimeMin(int endTime) {
		this.endTimeMin = endTime;
	}

	public String toString() {
		//return eventName + " at " + location;
		return eventName + " " + location + " " + descrip + " " + startTimeHour + " " +startTimeMin + " " + 
		endTimeHour + " " + endTimeMin + " " + startDate[0]+ "/" + startDate[1] + "/" + startDate[2] + " " + 
		allDay + " " + remind + " " + priv + " " + endDate[0] + "/" + endDate[1] + "/" + endDate[2] + " " + 
		calendar + " " + color + " " + id;
	}
	@Override
	public int compareTo(Object e) {
		Event event = (Event) e;
        // TODO Auto-generated method stub
		if(startTimeHour < event.getStartTimeHour()) 
			return -1;
		else if(startTimeHour > event.getStartTimeHour()) 
			return 1;
		else {
			if(startTimeMin < event.getStartTimeMin()) 
				return -1;
			else if(startTimeMin > event.getStartTimeMin()) 
				return 1;
			else 
				return 0;
		}
	}
	
	public Event(Parcel in){
		String[] data = new String[19];

		in.readStringArray(data);
		this.eventName = data[0];
		this.location = data[1];
		this.descrip = data[2];
		this.startTimeHour = Integer.parseInt(data[3]);
		this.startTimeMin = Integer.parseInt(data[4]);
		this.endTimeHour = Integer.parseInt(data[5]);
		this.endTimeMin = Integer.parseInt(data[6]);
		this.startDate[0] = Integer.parseInt(data[7]);
		this.startDate[1] = Integer.parseInt(data[8]);
		this.startDate[2] = Integer.parseInt(data[9]);
		this.allDay = Integer.parseInt(data[10]);
		this.remind = Integer.parseInt(data[11]);
		this.priv = Integer.parseInt(data[12]);
		this.endDate[0] = Integer.parseInt(data[13]);
		this.endDate[1] = Integer.parseInt(data[14]);
		this.endDate[2] = Integer.parseInt(data[15]);
		this.calendar = data[16];
		this.color = data[17];
		this.id = Long.parseLong(data[18]);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {this.eventName,
				this.location,
				this.descrip,
				Integer.toString(this.startTimeHour),
				Integer.toString(this.startTimeMin),
				Integer.toString(this.endTimeHour),
				Integer.toString(this.endTimeMin),
				Integer.toString(this.startDate[0]),
				Integer.toString(this.startDate[1]),
				Integer.toString(this.startDate[2]),
				Integer.toString(this.allDay),
				Integer.toString(this.remind),
				Integer.toString(this.priv),
				Integer.toString(this.endDate[0]),
				Integer.toString(this.endDate[1]),
				Integer.toString(this.endDate[2]),
				this.calendar,
				this.color,
				Long.toString(this.id)});
	}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Event createFromParcel(Parcel in) {
			return new Event(in); 
		}

		public Event[] newArray(int size) {
			return new Event[size];
		}
	};

}


