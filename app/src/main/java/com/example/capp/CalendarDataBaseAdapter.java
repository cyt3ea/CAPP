package com.example.capp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class CalendarDataBaseAdapter
{
    static final String DATABASE_NAME = "calendartable.db";

    static final int DATABASE_VERSION = 1;

    public static final int NAME_COLUMN = 1;
    public static final int COLOR_COLUN = 2;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + "CALENDARTABLE" +
            "( " + "ID" + " integer primary key autoincrement," + "CALENDAR  varchar(255), COLOR varchar(255)); ";

    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelper dbHelper;
    public  CalendarDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_CREATE);
    }

    // Method to open the Database
    public  CalendarDataBaseAdapter open() throws SQLException
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
    public boolean insertEntry(String calendar, String color)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each column.
        newValues.put("CALENDAR", calendar);
        newValues.put("COLOR", color);

        if(getSingleEntry(calendar).equals("NOT EXIST"))
        {
            // Insert the row into your table
            db.insert("CALENDARTABLE", null, newValues);
            Toast.makeText(context, "New Calendar Created", Toast.LENGTH_LONG).show();
            return true;
        }
        else
        {
            Toast.makeText(context, "Calendar name already exists.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // method to delete a Record of UserName
    public boolean deleteEntry(String calendar)
    {
        String where = "CALENDAR=?";
        int numberOFEntriesDeleted= db.delete("CALENDARTABLE", where, new String[]{calendar}) ;
        Toast.makeText(context, "Calendar Deleted Successfully ", Toast.LENGTH_LONG).show();// + numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return true;
    }

    // method to get the color of calendar
    public String getSingleEntry(String calendar)
    {
        Cursor cursor = db.query("CALENDARTABLE", null, " CALENDAR=?", new String[]{calendar}, null, null, null);
        if(cursor.getCount()<1) // Calendar Not Exist
            return "NOT EXIST";
        cursor.moveToFirst();
        String color = cursor.getString(cursor.getColumnIndex("COLOR"));
        //Log.v("Calendar Recieved: ", color);
        return color;
    }

    // Method to Update an Existing Record
    public void  updateEntry(String calendar, String newName, String newColor)
    {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        updatedValues.put("CALENDAR", newName);
        updatedValues.put("COLOR", newColor);

        String where="CALENDAR = ?";
        Log.v("UPDATED CALENDAR: ", newName + ", COLOR: " + newColor);
        db.update("CALENDARTABLE", updatedValues, where, new String[]{calendar});
    }

    public ArrayList<String> getAllCalendars() {
        ArrayList<String> calendarList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + "CALENDARTABLE";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.v("Getting all calendars: ", cursor.getString(0) + " " + cursor.getString(1));
                // Adding contact to list
                calendarList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return contact list
        Log.v("ALL CALENDARS: ", calendarList.toString() + "COLORS: " + getSingleEntry(calendarList.get(0)) + ", " + getSingleEntry(calendarList.get(1)) + ", " + getSingleEntry(calendarList.get(2)));
        return calendarList;
    }
}
