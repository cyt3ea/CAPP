package com.example.capp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEvent extends Activity implements OnClickListener, OnItemSelectedListener {

	CustomDialog startDatePicker, endDatePicker;
	int month, day, year;
	Calendar c;
	String[] months = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November","December" };
	TextView startDateText, endDateText;
	SimpleDateFormat df = new SimpleDateFormat("h:mm a");
	String currentTime = "";
	LayoutInflater inflater;
	View newEventLayout;
	Spinner spinner, calendarSpinner, colorSpinner;
	EditText evTitle, evLocation, evDescrip;
	CheckBox allDayCheck, privateCheck;
	int remind = -1;
	Event editEvent;
	MyEventsDataBaseAdapter eventsDBAdapter;
	Intent intentExtras;	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().hide();
		setContentView(R.layout.create_new_event);

		eventsDBAdapter = new MyEventsDataBaseAdapter(this);
		eventsDBAdapter = eventsDBAdapter.open();
		
		spinner = (Spinner) findViewById(R.id.remind); //When to remind
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.reminder_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);

		calendarSpinner = (Spinner) findViewById(R.id.calendar); //Which calendar the event should go on
		ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this,
				R.array.calendar_array, android.R.layout.simple_spinner_item);
		adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		calendarSpinner.setAdapter(adapterC);
		calendarSpinner.setSelection(0);

		colorSpinner = (Spinner) findViewById(R.id.color); //Event color
		final ArrayList<String> colorList = new ArrayList<String>(Arrays.asList("Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Black"));
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, colorList) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					Context mContext = this.getContext();
					LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            v = vi.inflate(android.R.layout.simple_spinner_item, parent, false);
				}

				TextView tv = ((TextView) v);
				tv.setText(colorList.get(position));

				switch (position) {
				case 0:
					tv.setTextColor(getResources().getColor(R.color.reds));
					break;
				case 1:
					tv.setTextColor(getResources().getColor(R.color.oranges));
					break;
				case 2:
					tv.setTextColor(getResources().getColor(R.color.yellows));
					break;
				case 3:
					tv.setTextColor(getResources().getColor(R.color.greens));
					break;
				case 4:
					tv.setTextColor(getResources().getColor(R.color.blues));
					break;
				case 5:
					tv.setTextColor(getResources().getColor(R.color.purples));
					break;
				case 6:
					tv.setTextColor(getResources().getColor(R.color.pinks));
					break;
				case 7:
					tv.setTextColor(getResources().getColor(R.color.blacks));
					break;
				}
				return v;  
		    }
			
			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent){
				View v = convertView;
				if (v == null) {
					Context mContext = this.getContext();
					LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            v = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
				}

				TextView tv = ((TextView) v);
				tv.setText(colorList.get(position));

				switch (position) {
				case 0:
					tv.setTextColor(getResources().getColor(R.color.reds));
					break;
				case 1:
					tv.setTextColor(getResources().getColor(R.color.oranges));
					break;
				case 2:
					tv.setTextColor(getResources().getColor(R.color.yellows));
					break;
				case 3:
					tv.setTextColor(getResources().getColor(R.color.greens));
					break;
				case 4:
					tv.setTextColor(getResources().getColor(R.color.blues));
					break;
				case 5:
					tv.setTextColor(getResources().getColor(R.color.purples));
					break;
				case 6:
					tv.setTextColor(getResources().getColor(R.color.pinks));
					break;
				case 7:
					tv.setTextColor(getResources().getColor(R.color.blacks));
					break;
				}
				return v;  
			}              
		}; 
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colorSpinner.setAdapter(spinnerAdapter);
		colorSpinner.setSelection(0);
		
		c = Calendar.getInstance();
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
		year = c.get(Calendar.YEAR);
		currentTime = df.format(Calendar.getInstance().getTime());

		startDateText = (TextView) findViewById(R.id.startDateAndTime);
		endDateText = (TextView) findViewById(R.id.endDateAndTime);

		startDateText.setText("Starts: " + months[month] + " " + day + ", " + year + " | " + currentTime);
		endDateText.setText("Ends: " + months[month] + " " + day + ", " + year + " | " + currentTime);

		endDatePicker = new CustomDialog(CreateEvent.this, "End Date and Time", endDateText, "Ends: ", null);	
		startDatePicker = new CustomDialog(CreateEvent.this, "Start Date and Time", startDateText, "Starts: ", endDatePicker);
		
		Button addButton = (Button) findViewById(R.id.addEvent);
		Button backButton = (Button) findViewById(R.id.backButton);
		addButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		
		evTitle=(EditText)findViewById(R.id.eventTitle);
		evLocation=(EditText)findViewById(R.id.eventLocation);
		evDescrip=(EditText)findViewById(R.id.description);
		allDayCheck = (CheckBox)findViewById(R.id.allDayCheckBox);
		allDayCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	int startDay = startDatePicker.getDate().getDayOfMonth();
            	int startMonth = startDatePicker.getDate().getMonth();
            	int startYear = startDatePicker.getDate().getYear();
            	int endDay = endDatePicker.getDate().getDayOfMonth();
            	int endMonth = endDatePicker.getDate().getMonth();
            	int endYear = endDatePicker.getDate().getYear();
            	int startHour = startDatePicker.getTime().getCurrentHour();
            	int startMin = startDatePicker.getTime().getCurrentMinute();
            	int endHour = endDatePicker.getTime().getCurrentHour();
            	int endMin = endDatePicker.getTime().getCurrentMinute();
                if(allDayCheck.isChecked()){
                	endDatePicker.getTime().setEnabled(false);
                	startDatePicker.getTime().setEnabled(false);
                	endDatePicker.setCount(1);
                	startDatePicker.setCount(1);
                	startDatePicker.getTV().setText("Starts: " + months[startMonth] + " " + startDay + ", " + startYear + " | " + "All Day");
            		endDatePicker.getTV().setText("Ends: " + months[endMonth] + " " + endDay + ", " + endYear + " | " + "All Day");
    				//Toast toast = Toast.makeText(getApplicationContext(), "Cannot change time since 'All Day' is checked.", Toast.LENGTH_LONG);
    				//toast.show();
                }
                else{
                	endDatePicker.getTime().setEnabled(true);
                	startDatePicker.getTime().setEnabled(true);
                	c.set(Calendar.HOUR_OF_DAY, startHour);
                	c.set(Calendar.MINUTE, startMin);
            		currentTime = df.format(c.getTime());
                	startDatePicker.getTV().setText("Starts: " + months[startMonth] + " " + startDay + ", " + startYear + " | " + currentTime);
                	c.set(Calendar.HOUR_OF_DAY, endHour);
                	c.set(Calendar.MINUTE, endMin);
            		currentTime = df.format(c.getTime());
            		endDatePicker.getTV().setText("Ends: " + months[endMonth] + " " + endDay + ", " + endYear + " | " + currentTime);
                }
            }
        });
		privateCheck = (CheckBox)findViewById(R.id.privateCheckBox);
		
		intentExtras = getIntent();
		if(intentExtras.hasExtra("EVENT")) {
			editEvent = (Event) intentExtras.getParcelableExtra("EVENT");

			Log.v("EDIT EVENT", editEvent.toString());
			editEvent = eventsDBAdapter.getSingleEntry_byID(editEvent.getID());
			Log.v("EDIT EVENT", editEvent.toString());
			evTitle.setText(editEvent.getEventName());
			evLocation.setText(editEvent.getLocation());
			evDescrip.setText(editEvent.getDescrip());
			addButton.setText("Edit");
			Log.v("START AND END TIMES: ", editEvent.getStartTimeHour() + ":" + editEvent.getStartTimeMin() + " " + editEvent.getEndTimeHour() + ":" + editEvent.getEndTimeMin());
			endDatePicker.setTime(editEvent.getEndTimeHour(), editEvent.getEndTimeMin());
			endDatePicker.setDate(editEvent.getEndDate()[0], editEvent.getEndDate()[1], editEvent.getEndDate()[2]);
			startDatePicker.setTime(editEvent.getStartTimeHour(), editEvent.getStartTimeMin());
			startDatePicker.setDate(editEvent.getStartDate()[0], editEvent.getStartDate()[1], editEvent.getStartDate()[2]);
			endDatePicker.setCount(1);
			startDatePicker.setCount(1);
			month = editEvent.getStartDate()[0];
			day = editEvent.getStartDate()[1];
			year = editEvent.getStartDate()[2];
			int endMonth = editEvent.getEndDate()[0];
			int endDay = editEvent.getEndDate()[1];
			int endYear = editEvent.getEndDate()[2];

			if(editEvent.getAllDay() == 1)
			{
				allDayCheck.setChecked(true);
				startDatePicker.getTime().setEnabled(false);
				endDatePicker.getTime().setEnabled(false);
				startDatePicker.getTV().setText("Starts: " + months[month] + " " + day + ", " + year + " | " + "All Day");
				endDatePicker.getTV().setText("Ends: " + months[endMonth] + " " + endDay + ", " + endYear + " | " + "All Day");
			}
			else {
				allDayCheck.setChecked(false);
				startDatePicker.getTime().setEnabled(true);
				endDatePicker.getTime().setEnabled(true);
				c.set(Calendar.HOUR_OF_DAY, editEvent.getStartTimeHour());
				c.set(Calendar.MINUTE, editEvent.getStartTimeMin());
				currentTime = df.format(c.getTime());
				startDatePicker.getTV().setText("Starts: " + months[month] + " " + day + ", " + year + " | " + currentTime);
				c.set(Calendar.HOUR_OF_DAY, editEvent.getEndTimeHour());
				c.set(Calendar.MINUTE, editEvent.getEndTimeMin());
				currentTime = df.format(c.getTime());
				endDatePicker.getTV().setText("Ends: " + months[endMonth] + " " + endDay + ", " + endYear + " | " + currentTime);
			}
			if(editEvent.getPriv() == 1)
				privateCheck.setChecked(true);
			else
				privateCheck.setChecked(false);
			String tempColor = editEvent.getColor();
			if(tempColor.equals("#FF0000"))
				colorSpinner.setSelection(0);
			else if(tempColor.equals("#FF6A00"))
				colorSpinner.setSelection(1);
			else if(tempColor.equals("#FFDB2B"))
				colorSpinner.setSelection(2);
			else if(tempColor.equals("#A5FF7F"))
				colorSpinner.setSelection(3);
			else if(tempColor.equals("#7F92FF"))
				colorSpinner.setSelection(4);
			else if(tempColor.equals("#8900F9"))
				colorSpinner.setSelection(5);
			else if(tempColor.equals("#FFA7DC"))
				colorSpinner.setSelection(6);
			else if(tempColor.equals("#000000"))
				colorSpinner.setSelection(7);
			int tempRemind = editEvent.getRemind();
			if(tempRemind == -1)
				spinner.setSelection(0);
			if(tempRemind == 5)
				spinner.setSelection(1);
			if(tempRemind == 15)
				spinner.setSelection(2);
			if(tempRemind == 30)
				spinner.setSelection(3);
			if(tempRemind == 60)
				spinner.setSelection(4);
			if(tempRemind == 120)
				spinner.setSelection(5);
			if(tempRemind == 60*24)
				spinner.setSelection(6);
			if(tempRemind == 60*24*2)
				spinner.setSelection(7);
			if(tempRemind == 60*24*7)
				spinner.setSelection(8);
			
			if(editEvent.getCalendar().equals("Default"))
				calendarSpinner.setSelection(0);
			if(editEvent.getCalendar().equals("Academic"))
				calendarSpinner.setSelection(1);
			//What to do with calendar spinner???
		}
	}

	public void onClick(View v) { 
		if(v.getId() == R.id.startDateAndTime) {
			startDatePicker.show();
		}
		else if(v.getId() == R.id.endDateAndTime) {
			endDatePicker.show();
		}
		else if(v.getId() == R.id.addEvent) {
			String title = evTitle.getText().toString();
			String location = evLocation.getText().toString();
			String description = evDescrip.getText().toString();
			int startTimeH = startDatePicker.getTime().getCurrentHour();
			int startTimeM = startDatePicker.getTime().getCurrentMinute();
			int endTimeH = endDatePicker.getTime().getCurrentHour();
			int endTimeM = endDatePicker.getTime().getCurrentMinute();
			int startMonth = startDatePicker.getDate().getMonth();
			int startDay = startDatePicker.getDate().getDayOfMonth();
			int startYear = startDatePicker.getDate().getYear();
			int endMonth = endDatePicker.getDate().getMonth();
			int endDay = endDatePicker.getDate().getDayOfMonth();
			int endYear = endDatePicker.getDate().getYear();
			int allDay = 0;
			int priv = 0;
			String remindSpin = spinner.getSelectedItem().toString();
			String calendar = calendarSpinner.getSelectedItem().toString();
			
			Log.v("START DATE: ", startDatePicker.getDate().getMonth() + "/" + startDatePicker.getDate().getDayOfMonth() + startDatePicker.getDate().getYear());
			
			if(remindSpin.equals("None"))
				remind = -1;
			else if(remindSpin.equals("At Time of Event"))
				remind = 0;
			else if(remindSpin.equals("5 Minutes Before"))
				remind = 5;
			else if(remindSpin.equals("15 Minutes Before"))
				remind = 15;
			else if(remindSpin.equals("30 Minutes Before"))
				remind = 30;
			else if(remindSpin.equals("1 Hour Before"))
				remind = 60;
			else if(remindSpin.equals("2 Hours Before"))
				remind = 120;
			else if(remindSpin.equals("1 Day Before"))
				remind = 60*24;
			else if(remindSpin.equals("2 Days Before"))
				remind = 60*24*2;
			else if(remindSpin.equals("1 Week Before"))
				remind = 60*24*7;
			String color = colorSpinner.getSelectedItem().toString();
			if(color.equals("Red"))
				color = "#FF0000";
			else if(color.equals("Orange"))
				color = "#FF6A00";
			else if(color.equals("Yellow"))
				color = "#FFDB2B";
			else if(color.equals("Green"))
				color = "#A5FF7F";
			else if(color.equals("Blue"))
				color = "#7F92FF";
			else if(color.equals("Purple"))
				color = "#8900F9";
			else if(color.equals("Pink"))
				color = "#FFA7DC";
			else if(color.equals("Black"))
				color = "#000000";
			
			if(allDayCheck.isChecked()) {
				allDay = 1;
				startTimeH = -1;
				startTimeM = -1;
				endTimeH = -1;
				endTimeH = -1;
			}
			if(privateCheck.isChecked()) {
				priv = 1;
			}
			Context context = getApplicationContext();
			CharSequence text = "";
			int duration = Toast.LENGTH_SHORT;

			if(title.equals("")) {
				text = "Cannot make event with no title.";
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			else if((endYear < startYear) ||
					(endMonth < startMonth && endYear >= startYear) ||
					(endDay < startDay && endMonth >= startMonth && endYear >= startYear)) {
				Log.d("Invalid Date: END", endMonth + " " + endDay + " " + endYear);
				Log.d("Invalid Date: START", startMonth + " " + startDay + " " + startDay);
				text = "End Date Cannot be earlier than Start Date.";
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			else if (((endTimeH < startTimeH) ||
					(endTimeM < startTimeM && endTimeH == startTimeH)) && startDay == endDay && startMonth == endMonth && startYear == endYear) {
				Log.d("Invalid Time: END", endTimeH + " " + endTimeM);
				Log.d("Invalid Time: START", startTimeH + " " + startTimeM);
				text = "End Time Cannot be earlier than Start Time.";
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			else if(intentExtras.hasExtra("EVENT")) { 
				Event newEvent = new Event(title, location, description,
						startTimeH, startTimeM, endTimeH, endTimeM,
						startMonth, startDay, startYear,
						allDay, remind, priv, 
						endMonth, endDay, endYear,
						calendar, color, editEvent.getID());
				Intent i = new Intent();
				i.putExtra("newEvent", newEvent);
				setResult(Activity.RESULT_OK, i);
				eventsDBAdapter.close();
				finish();
			}
			else {
				Event newEvent = new Event(title, location, description,
						startTimeH, startTimeM, endTimeH, endTimeM,
						startMonth, startDay, startYear,
						allDay, remind, priv, 
						endMonth, endDay, endYear,
						calendar, color, -1);
				Intent i = new Intent();
				i.putExtra("newEvent", newEvent);
				setResult(Activity.RESULT_OK, i);

				finish();
			}
		}
		else if(v.getId() == R.id.backButton) { 
			finish();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}


	public class CustomDialog extends Dialog implements OnClickListener
	{
		DatePicker datePicker;
		TimePicker timePicker;
		TextView tv;
		SimpleDateFormat df = new SimpleDateFormat("h:mm a");
		String text;
		CustomDialog dialog;
		int count;
		Calendar calendar;

		public CustomDialog(final Context context, String title, TextView newTV, String label, CustomDialog d) {
			super(context);

			setTitle(title);
			setContentView(R.layout.custom_dialog_layout);
			
			calendar = Calendar.getInstance();

			text = label;
			dialog = d;
			
			// get our tabHost from the xml
			TabHost tabHost = (TabHost)findViewById(R.id.TabHost01);
			tabHost.setup();	            
			
			// create tab 1
			TabHost.TabSpec spec1 = tabHost.newTabSpec("tab1");
			spec1.setIndicator("Date");
			spec1.setContent(R.id.datePickerLayout);
			tabHost.addTab(spec1);
			//create tab2
			TabHost.TabSpec spec2 = tabHost.newTabSpec("tab2");
			spec2.setIndicator("Time");
			spec2.setContent(R.id.timePickerLayout);
			tabHost.addTab(spec2);

			Button doneButton = (Button) findViewById(R.id.doneButton1);
			Button doneButton2 = (Button) findViewById(R.id.doneButton2);
			doneButton.setOnClickListener(this);
			doneButton2.setOnClickListener(this);

			datePicker = (DatePicker) findViewById(R.id.datePicker);
			timePicker = (TimePicker) findViewById(R.id.timePicker);

			tv = newTV;

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.doneButton1 || v.getId() == R.id.doneButton2) {
				int day = datePicker.getDayOfMonth();
				int month = datePicker.getMonth();
				int year = datePicker.getYear();
				if(tv.getText().toString().contains("All Day")) {
					tv.setText(text + months[month] + " " + day + ", " + year + " | " + "All Day");
				}
				else {
					calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
					calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
					String currentTime = df.format(calendar.getTime());
					tv.setText(text + months[month] + " " + day + ", " + year + " | " + currentTime);
				}
				count++;
				if(dialog != null && dialog.getCount() < 1) {
					DatePicker tempDate = dialog.getDate();
					tempDate.updateDate(year, month, day);
					TimePicker tempTime = dialog.getTime();
					tempTime.setCurrentHour(timePicker.getCurrentHour());
					tempTime.setCurrentMinute(timePicker.getCurrentMinute());
					calendar.set(Calendar.HOUR_OF_DAY, tempTime.getCurrentHour());
					calendar.set(Calendar.MINUTE, tempTime.getCurrentMinute());
					currentTime = df.format(calendar.getTime());
					dialog.getTV().setText(dialog.getText() + months[month] + " " + day + ", " + year + " | " + currentTime);
				}
				dismiss();
			}
		}
		public void setDate(int month, int day, int year) {
			datePicker.updateDate(year, month, day);
		}
		public void setTime(int hour, int min) {
			Log.v("SEetting time: ", hour + ":" + min + " " + calendar.HOUR_OF_DAY + ":" + calendar.MINUTE);
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(min);
			Log.v("Setting time: ", timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());

		}
		public DatePicker getDate() { 
			return datePicker;
		}
		public TimePicker getTime() { 
			return timePicker;
		}
		public TextView getTV() { 
			return tv;
		}
		public String getText() { 
			return text;
		}
		public int getCount() { 
			return count;
		}
		public void setCount(int c) {
			count = c;
		}
	}
}

