package com.example.capp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class LoginDataBaseAdapter 
{
	static final String DATABASE_NAME = "login.db";

	static final int DATABASE_VERSION = 1;

	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE = "create table " + "LOGIN" +
			"( " + "ID" + " integer primary key autoincrement," + "USERNAME  varchar(255),PASSWORD varchar(255)); ";

	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  LoginDataBaseAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_CREATE);
	}

	// Method to open the Database  
	public  LoginDataBaseAdapter open() throws SQLException 
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
	public boolean insertEntry(String userName,String password)
	{
		ContentValues newValues = new ContentValues();
		// Assign values for each column.
		newValues.put("USERNAME", userName);
		newValues.put("PASSWORD", password);

		if(getSingleEntry(userName).equals("NOT EXIST"))
		{
		// Insert the row into your table
			db.insert("LOGIN", null, newValues);
			Toast.makeText(context, "User Info Saved", Toast.LENGTH_LONG).show();
			return true;
		}
		else
		{
			Toast.makeText(context, "Username is not available.", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	// method to delete a Record of UserName
	public int deleteEntry(String UserName)
	{        
		String where = "USERNAME=?";
		int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
		Toast.makeText(context, "Number of Entry Deleted Successfully : " + numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
		return numberOFEntriesDeleted;

	}

	// method to get the password  of userName
	public String getSingleEntry(String userName)
	{
		Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
		if(cursor.getCount()<1) // UserName Not Exist
			return "NOT EXIST";
		cursor.moveToFirst();
		String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
		return password;
	}

	// Method to Update an Existing Record 
	public void  updateEntry(String userName,String password)
	{
		//  create object of ContentValues
		ContentValues updatedValues = new ContentValues();
		// Assign values for each Column.
		updatedValues.put("USERNAME", userName);
		updatedValues.put("PASSWORD",password);

		String where="USERNAME = ?";
		db.update("LOGIN",updatedValues, where, new String[]{userName});
	}
}
