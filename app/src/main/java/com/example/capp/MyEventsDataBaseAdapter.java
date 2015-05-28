package com.example.capp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class MyEventsDataBaseAdapter {
	static final String DATABASE_NAME = "myEvents.db";

	static final int DATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";

	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE = "create table " + "EVENTS" +
			"( " + KEY_ID + " integer primary key autoincrement," + "USERNAME  varchar(25),EVENTNAME varchar(100),LOCATION varchar(100), DESCRIPTION varchar(255), START_HOUR varchar(5), START_MIN varchar(5), END_HOUR varchar(5), END_MIN varchar(5), START_DAY varchar(5), START_MONTH varchar(25), START_YEAR varchar(5), ALLDAY varchar(1), REMIND varchar(5), PRIVATE varchar(5), END_DAY varchar(5), END_MONTH varchar(25), END_YEAR varchar(5), CALENDAR varchar(50), COLOR varchar(20), REPEAT varchar(65), REPEATEXCEPT varchar(200));";

	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	
	public  MyEventsDataBaseAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_CREATE);
	}

	// Method to open the Database  
	public  MyEventsDataBaseAdapter open() throws SQLException 
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}

	// Method to close the Database  
	public void close() 
	{
		db.close();
	}

	// Method returns an Instance of the Database 
	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}

	// Method to insert a record in Table
	public long insertEntry(String userName, String eventName, String location, String description, String startHour, String startMin, String endHour, String endMin,
			String startDay, String startMonth, String startYear, String allDay, String remind,
			String priv, String endDay, String endMonth, String endYear, String calendar, String color, String repeat, String repeatExcept)
	{
		ContentValues newValues = new ContentValues();
		// Assign values for each column.
		newValues.put("USERNAME", userName);
		newValues.put("EVENTNAME", eventName);
		newValues.put("LOCATION", location);
		newValues.put("DESCRIPTION", description);
		newValues.put("START_HOUR", startHour);
		newValues.put("START_MIN", startMin);
		newValues.put("END_HOUR", endHour);
		newValues.put("END_MIN", endMin);
		newValues.put("START_DAY", startDay);
		newValues.put("START_MONTH", startMonth);
		newValues.put("START_YEAR", startYear);
		newValues.put("ALLDAY", allDay);
		newValues.put("REMIND", remind);
		newValues.put("PRIVATE", priv);
		newValues.put("END_DAY", endDay);
		newValues.put("END_MONTH", endMonth);
		newValues.put("END_YEAR", endYear);
		newValues.put("CALENDAR", calendar);
		newValues.put("COLOR", color);
		newValues.put("REPEAT", repeat);
		newValues.put("REPEATEXCEPT", repeatExcept);

		//db.insert("EVENTS", null, newValues);
		Toast.makeText(context, "Event Added", Toast.LENGTH_LONG).show();
		return db.insert("EVENTS", null, newValues);
	}

	public Event getSingleEntry_byID(long id)
	{
	    String[] columns = new String[]{KEY_ID, "USERNAME", "EVENTNAME", "LOCATION", "DESCRIPTION", "START_HOUR", "START_MIN", "END_HOUR", "END_MIN", "START_DAY", "START_MONTH", "START_YEAR", "ALLDAY", "REMIND", "PRIVATE", "END_DAY", "END_MONTH", "END_YEAR", "CALENDAR", "COLOR", "REPEAT", "REPEATEXCEPT"};
		Cursor cursor = db.query("EVENTS", columns, KEY_ID + " =?", new String[] { String.valueOf(id) }, null, null, null, null);
		if(cursor.getCount()<1) // UserName Not Exist
			return null;
		cursor.moveToFirst();
		Log.v("GETTING SINGLE ENTRY", cursor.getString(0) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4) + " " + cursor.getString(5) + " " + cursor.getString(6) + " " + cursor.getString(7) + " " + cursor.getString(8) + " " + cursor.getString(9) + " " + cursor.getString(10) + " " + cursor.getString(11) + " " + cursor.getString(12) + " " + cursor.getString(13) + " " + cursor.getString(14) + " " + cursor.getString(15) + " " + cursor.getString(16) + " " + cursor.getString(17) + " " + cursor.getString(18) + " " +  cursor.getString(19));
		Event event = new Event(cursor.getString(2),
	                cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)),
	                Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
	                Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
	                Integer.parseInt(cursor.getString(10)), Integer.parseInt(cursor.getString(11)),
	                Integer.parseInt(cursor.getString(12)), Integer.parseInt(cursor.getString(13)),
	                Integer.parseInt(cursor.getString(14)), Integer.parseInt(cursor.getString(15)),
	                Integer.parseInt(cursor.getString(16)), Integer.parseInt(cursor.getString(17)),
	                cursor.getString(18), cursor.getString(19), Long.parseLong(cursor.getString(0)), cursor.getString(20), cursor.getString(21));
		Log.v("SINGLE ENTRY: ", event.toString());
		return event;
	}
	// method to delete a Record of UserName
	public void delete_byID(long id){
		db.delete("EVENTS", KEY_ID + "=" + id, null);
		Toast.makeText(context, "Entry Deleted Successfully", Toast.LENGTH_LONG).show();
	}
	
	// method to get the password  of userName

	// Method to Update an Existing Record 
	public void  updateEntry(Event event, long id)
	{
		//  create object of ContentValues
		ContentValues newValues = new ContentValues();
		// Assign values for each Column.
		newValues.put("EVENTNAME", event.getEventName());
		newValues.put("LOCATION", event.getLocation());
		newValues.put("DESCRIPTION", event.getDescrip());
		newValues.put("START_HOUR", event.getStartTimeHour() + "");
		newValues.put("START_MIN", event.getStartTimeMin() + "");
		newValues.put("END_HOUR", event.getEndTimeHour() + "");
		newValues.put("END_MIN", event.getEndTimeMin() + "");
		newValues.put("START_DAY", event.getStartDate()[0] + "");
		newValues.put("START_MONTH", event.getStartDate()[1] + "");
		newValues.put("START_YEAR", event.getStartDate()[2] + "");
		newValues.put("ALLDAY", event.getAllDay() + "");
		newValues.put("REMIND", event.getRemind() + "");
		newValues.put("PRIVATE", event.getPriv() + "");
		newValues.put("END_DAY", event.getEndDate()[0] + "");
		newValues.put("END_MONTH", event.getEndDate()[1] + "");
		newValues.put("END_YEAR", event.getEndDate()[2] + "");
		newValues.put("CALENDAR", event.getCalendar());
		newValues.put("COLOR", event.getColor());
		newValues.put("REPEAT", event.getRepeat());
		newValues.put("REPEATEXCEPT", event.getRepeatExcept());

		db.update("EVENTS", newValues, KEY_ID + " = ?", new String[] { String.valueOf(id) });
	}
	
	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> eventList = new ArrayList<Event>();
		String selectQuery = "SELECT  * FROM " + "EVENTS";
		 
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	    		Log.v("EVENTS IN DATABASE: ", cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4) + 
	    				" " + cursor.getString(5) + " " + cursor.getString(6) + " " + cursor.getString(7) + " " + cursor.getString(8) + " "
	    				+ cursor.getString(9) + " " + cursor.getString(10) + " " + cursor.getString(11) + " " + cursor.getString(12) + " " 
	    				+ cursor.getString(13) + " " + cursor.getString(14) + " " + cursor.getString(15) + cursor.getString(16) + " " + 
	    				cursor.getString(17) + " " + cursor.getString(18) + " " + cursor.getString(19) + " " + cursor.getString(20) + " " +
						cursor.getString(21));
	            Event event = new Event(cursor.getString(2),
		                cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)),
		                Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
		                Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
		                Integer.parseInt(cursor.getString(10)), Integer.parseInt(cursor.getString(11)),
		                Integer.parseInt(cursor.getString(12)), Integer.parseInt(cursor.getString(13)),
		                Integer.parseInt(cursor.getString(14)), Integer.parseInt(cursor.getString(15)),
		                Integer.parseInt(cursor.getString(16)), Integer.parseInt(cursor.getString(17)),
		                cursor.getString(18), cursor.getString(19), Long.parseLong(cursor.getString(0)),
						cursor.getString(20), cursor.getString(21));
	            // Adding contact to list
	            eventList.add(event);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return eventList;
	}

}
