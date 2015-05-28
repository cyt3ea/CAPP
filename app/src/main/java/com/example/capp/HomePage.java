package com.example.capp;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.view.Display;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class HomePage extends Activity implements OnClickListener{

	protected static final int ActivityTwoRequestCode = 0;

	TextView todaysDateTextView, dayPicked, dayPickedMonth;
	String[] months = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November","December" };
	int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	Calendar c;
	int month, day, year, dayOfWeek;
	String view = "day";
	GridCellAdapter weekAdapter, adapter, adapterYear;
	GridView weekCalendarView, calendarView;
	ImageButton leftToggle, rightToggle, leftToggleWV, rightToggleWV, leftToggleMV, rightToggleMV, leftToggleYV, rightToggleYV, goToDayPicked, goToDayPickedMonth;
	ArrayList<Event> eventList = new ArrayList<Event>();
	ExpandableListView dayViewLV, weekViewLV, monthViewLV;
	ViewFlipper viewFlipper;
	ExpandableListAdapter expListAdapter, expWeekListAdapter, expMonthListAdapter;
	MyEventsDataBaseAdapter myEventsDataBaseAdapter;
	String userName;
	ArrayList<String> dOWL = new ArrayList<String>();
	Map<String, ArrayList<Event>> eventCollections = new HashMap<String, ArrayList<Event>>();
	ImageView point;
	int width, height;
	GridView yearViewJan, yearViewFeb, yearViewMar, yearViewApr, yearViewMay, yearViewJun, yearViewJul, yearViewAug, yearViewSep, yearViewOct, yearViewNov, yearViewDec;
	GridLayout yearViewGridLayout;
	Animation slideOutRight, slideOutLeft, slideInRight, slideInLeft;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);

		setContentView(R.layout.homepage);


		slideOutRight = AnimationUtils.loadAnimation(HomePage.this, android.R.anim.slide_out_right);
		slideOutRight.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation anim) {
			}

			;

			public void onAnimationRepeat(Animation anim) {
			}

			;

			public void onAnimationEnd(Animation anim) {
				slideInLeft.setDuration(200);
				if (view.equals("week")) {
					weekCalendarView.setAnimation(slideInLeft);
					setWeekGridCellAdapterToDate(day, month, year);
				} else if (view.equals("month")) {
					calendarView.setAnimation(slideInLeft);
					setGridCellAdapterToDate(month, year);
				} else if (view.equals("year")) {
					yearViewGridLayout.setAnimation(slideInLeft);
				}
				slideInLeft.start();
			};
		});
		slideOutLeft = AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_out_left);
		slideOutLeft.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation anim)
			{
			};
			public void onAnimationRepeat(Animation anim)
			{
			};
			public void onAnimationEnd(Animation anim)
			{
				slideInRight.setDuration(200);
				if(view.equals("week")) {
					weekCalendarView.setAnimation(slideInRight);
					setWeekGridCellAdapterToDate(day, month, year);
				}
				else if(view.equals("month")) {
					calendarView.setAnimation(slideInRight);
					setGridCellAdapterToDate(month, year);
				}
				else if (view.equals("year")) {
					yearViewGridLayout.setAnimation(slideInRight);
				}
				slideInRight.start();
			};
		});
		slideInRight = AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_in_right);
		slideInLeft = AnimationUtils.loadAnimation(HomePage.this, android.R.anim.slide_in_left);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		//view = "day";
		//openDayView();

		//eventList.add(new Event());

		yearViewGridLayout = (GridLayout) this.findViewById(R.id.yearViewGridLayout);

		weekCalendarView = (GridView) this.findViewById(R.id.weekCalendarView);
		calendarView = (GridView) this.findViewById(R.id.monthCalendarView);
		yearViewJan = (GridView) this.findViewById(R.id.yearViewJan);
		yearViewFeb = (GridView) this.findViewById(R.id.yearViewFeb);
		yearViewMar = (GridView) this.findViewById(R.id.yearViewMar);
		yearViewApr = (GridView) this.findViewById(R.id.yearViewApr);
		yearViewMay = (GridView) this.findViewById(R.id.yearViewMay);
		yearViewJun = (GridView) this.findViewById(R.id.yearViewJun);
		yearViewJul = (GridView) this.findViewById(R.id.yearViewJul);
		yearViewAug = (GridView) this.findViewById(R.id.yearViewAug);
		yearViewSep = (GridView) this.findViewById(R.id.yearViewSep);
		yearViewOct = (GridView) this.findViewById(R.id.yearViewOct);
		yearViewNov = (GridView) this.findViewById(R.id.yearViewNov);
		yearViewDec = (GridView) this.findViewById(R.id.yearViewDec);
		setGridViewParams(yearViewJan);
		setGridViewParams(yearViewFeb);
		setGridViewParams(yearViewMar);
		setGridViewParams(yearViewApr);
		setGridViewParams(yearViewMay);
		setGridViewParams(yearViewJun);
		setGridViewParams(yearViewJul);
		setGridViewParams(yearViewAug);
		setGridViewParams(yearViewSep);
		setGridViewParams(yearViewOct);
		setGridViewParams(yearViewNov);
		setGridViewParams(yearViewDec);


		c = Calendar.getInstance();
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
		year = c.get(Calendar.YEAR);

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

		Intent receiveUsername = getIntent();
		userName = receiveUsername.getExtras().getString("key");

		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.userName);
		mTitleTextView.setText(userName);

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		todaysDateTextView=(TextView)findViewById(R.id.viewDate);
		todaysDateTextView.setText(months[month] + " " + Integer.toString(day) + ", "
				+ Integer.toString(year));
		dayPicked = (TextView) findViewById(R.id.dayPicked);
		dayPickedMonth = (TextView) findViewById(R.id.dayPickedMonth);

		point = (ImageView) findViewById(R.id.pointer);
		point.setVisibility(View.INVISIBLE);

		leftToggle = (ImageButton) findViewById(R.id.toggleLeft);
		leftToggle.setOnClickListener(this);
		rightToggle = (ImageButton) findViewById(R.id.toggleRight);
		rightToggle.setOnClickListener(this);
		leftToggleWV = (ImageButton) findViewById(R.id.toggleLeftWV);
		leftToggleWV.setOnClickListener(this);
		rightToggleWV = (ImageButton) findViewById(R.id.toggleRightWV);
		rightToggleWV.setOnClickListener(this);
		leftToggleMV = (ImageButton) findViewById(R.id.toggleLeftMV);
		leftToggleMV.setOnClickListener(this);
		rightToggleMV = (ImageButton) findViewById(R.id.toggleRightMV);
		rightToggleMV.setOnClickListener(this);
		leftToggleYV = (ImageButton) findViewById(R.id.toggleLeftYV);
		leftToggleYV.setOnClickListener(this);
		rightToggleYV = (ImageButton) findViewById(R.id.toggleRightYV);
		rightToggleYV.setOnClickListener(this);

		viewFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);
		viewFlipper.setOnTouchListener(new OnSwipeTouchListener(HomePage.this) {
			public void onSwipeTop() {
				Toast.makeText(HomePage.this, "top", Toast.LENGTH_SHORT).show();
			}

			public void onSwipeRight() {
				Toast.makeText(HomePage.this, "right", Toast.LENGTH_SHORT).show();
				viewFlipper.setInAnimation(slideInLeft);
				viewFlipper.setOutAnimation(slideOutRight);
				viewFlipper.showPrevious();
			}

			public void onSwipeLeft() {
				Toast.makeText(HomePage.this, "left", Toast.LENGTH_SHORT).show();
				viewFlipper.setInAnimation(slideInRight);
				viewFlipper.setOutAnimation(slideOutLeft);
				viewFlipper.showNext();
			}
			public void onSwipeBottom() {
				Toast.makeText(HomePage.this, "bottom", Toast.LENGTH_SHORT).show();
			}

			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

		dayViewLV = (ExpandableListView) findViewById(R.id.defaultDayViewEvents);
		expListAdapter = new ExpandableListAdapter(this, eventList);
		dayViewLV.setAdapter(expListAdapter);

		weekViewLV = (ExpandableListView) findViewById(R.id.weekViewEvents);
		expWeekListAdapter = new ExpandableListAdapter(this, eventList);
		weekViewLV.setAdapter(expWeekListAdapter);

		monthViewLV = (ExpandableListView) findViewById(R.id.monthViewEvents);
		expMonthListAdapter = new ExpandableListAdapter(this, eventList);
		monthViewLV.setAdapter(expMonthListAdapter);

		myEventsDataBaseAdapter = new MyEventsDataBaseAdapter(this);
		myEventsDataBaseAdapter = myEventsDataBaseAdapter.open();

		this.dayViewLV.setEmptyView(findViewById(R.id.empty));
		this.weekViewLV.setEmptyView(findViewById(R.id.emptyWeek));
		this.monthViewLV.setEmptyView(findViewById(R.id.emptyMonth));

		ImageButton addNewEvent = (ImageButton)mCustomView.findViewById(R.id.addEventButton);
		addNewEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HomePage.this, "New Event Button Clicked!", Toast.LENGTH_SHORT).show();
				Intent makeNewEvent = new Intent(HomePage.this, CreateEvent.class);
				startActivityForResult(makeNewEvent, ActivityTwoRequestCode);
			}
		});

		goToDayPicked = (ImageButton)findViewById(R.id.goToDayPicked);
		goToDayPicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HomePage.this, "Going to " + dayPicked.getText(), Toast.LENGTH_SHORT).show();
				String[] temp = dayPicked.getText().toString().split(" ");
				day = Integer.parseInt(temp[1].substring(0, temp[1].length() - 1));
				month = Arrays.asList(months).indexOf(temp[0]);
				year = Integer.parseInt(temp[2]);
				//point.setVisibility(View.INVISIBLE);
				openDayView();
			}
		});

		goToDayPickedMonth = (ImageButton)findViewById(R.id.goToDayPickedMonth);
		goToDayPickedMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HomePage.this, "Going to " + dayPickedMonth.getText(), Toast.LENGTH_SHORT).show();
				String[] temp = dayPickedMonth.getText().toString().split(" ");
				day = Integer.parseInt(temp[1].substring(0, temp[1].length() - 1));
				month = Arrays.asList(months).indexOf(temp[0]);
				year = Integer.parseInt(temp[2]);
				//point.setVisibility(View.INVISIBLE);
				openDayView();
			}
		});
		openDayView();
	}

	public void setGridViewParams(GridView x) {
		RelativeLayout leftRightToggleYV = (RelativeLayout) findViewById(R.id.leftRightToggleYV);
		ViewGroup.LayoutParams toggleYV = leftRightToggleYV.getLayoutParams();
		TextView monthLabel = (TextView) findViewById(R.id.january);
		ViewGroup.LayoutParams month = monthLabel.getLayoutParams();
		ViewGroup.LayoutParams layoutParams = x.getLayoutParams();
		layoutParams.width = width/3 - 16; //this is in pixels
		layoutParams.height = (height-toggleYV.height*2-4*month.height)/5;
		x.setLayoutParams(layoutParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.view, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		c = Calendar.getInstance();
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
		year = c.get(Calendar.YEAR);
		dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		switch (item.getItemId()) {
			case R.id.action_home:
				openDayView();
				return true;
			case R.id.action_profile:
				return true;
			case R.id.action_search:
				return true;
			case R.id.action_notifications:
				return true;
			case R.id.action_more:
				return true;
			/*
			case R.id.action_day:
				openDayView();
				return true;
			case R.id.action_week:
				openWeekView();
				return true;
			case R.id.action_month:
				openMonthView();
				return true;
			case R.id.action_year:
				openYearView();
				return true;
				*/
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openYearView() {
		// TODO Auto-generated method stub

		view = "year";

		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 0, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewJan.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 1, year, expMonthListAdapter, dayPickedMonth,view);
		yearViewFeb.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 2, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewMar.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 3, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewApr.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 4, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewMay.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 5, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewJun.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 6, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewJul.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 7, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewAug.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 8, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewSep.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 9, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewOct.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 10, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewNov.setAdapter(adapterYear);
		adapterYear = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, 11, year, expMonthListAdapter, dayPickedMonth, view);
		yearViewDec.setAdapter(adapterYear);

		viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.yearview)));
		todaysDateTextView=(TextView)findViewById(R.id.viewDateYV);
		todaysDateTextView.setText(Integer.toString(year));
	}

	private void openMonthView() {
		// TODO Auto-generated method stub

		if(!view.equals("month")) {
			dayPickedMonth.setText(months[month] + " " + day + ", " + year);
			Log.d("DAY PICKED MONTH", months[month] + " " + day + ", " + year);
			//point.setVisibility(View.GONE);
		}

		view = "month";

		if(adapter == null) {
			adapter = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, month, year, expMonthListAdapter, dayPickedMonth, view);
			calendarView.setAdapter(adapter);
		}
		else {
			setGridCellAdapterToDate(month, year);
		}

		ArrayList<Event> tempList = new ArrayList<Event>();
		tempList = myEventsDataBaseAdapter.getAllEvents();
		Log.v("EVENTS IN DATABASE: ", tempList.toString());

		String[] temp = dayPickedMonth.getText().toString().split(" ");
		int tempDay = Integer.parseInt(temp[1].substring(0, temp[1].length() - 1));
		int tempMonth = Arrays.asList(months).indexOf(temp[0]);
		int tempYear = Integer.parseInt(temp[2]);

		Log.v("DATE: ", month + " " + day + " " + year);

		getEventsForThatDay(expMonthListAdapter, tempDay, tempMonth, tempYear);

		viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.monthview)));

		todaysDateTextView=(TextView)findViewById(R.id.viewDateMV);
		todaysDateTextView.setText(months[month] + " " + Integer.toString(year));
	}

	private void openWeekView() {
		// TODO Auto-generated method stuff

		if(weekAdapter == null) {
			weekAdapter = new GridCellAdapter(HomePage.this, R.id.calendar_day_gridcell, month, year, day, expWeekListAdapter, dayPicked, point);
			weekCalendarView.setAdapter(weekAdapter);
		}
		else
			setWeekGridCellAdapterToDate(day, month, year);

		if(!view.equals("week")) {
			dayPicked.setText(months[month] + " " + day + ", " + year);
		}

		ArrayList<Event> tempList = new ArrayList<Event>();
		tempList = myEventsDataBaseAdapter.getAllEvents();
		Log.v("EVENTS IN DATABASE: ", tempList.toString());

		String[] temp = dayPicked.getText().toString().split(" ");
		int tempDay = Integer.parseInt(temp[1].substring(0, temp[1].length() - 1));
		int tempMonth = Arrays.asList(months).indexOf(temp[0]);
		int tempYear = Integer.parseInt(temp[2]);

		Log.v("DATE: ", month + " " + day + " " + year);
		getEventsForThatDay(expWeekListAdapter, tempDay, tempMonth, tempYear);

		if(dayOfWeek != 1) {
			if(day - dayOfWeek < 0) {
				month = month - 1;
				day = daysOfMonth[month] + day - dayOfWeek + 1;
			}
			else
				day = day - dayOfWeek + 1;
			dayOfWeek = 1;
		}

		int startDay = 0;
		int endDay = 0;
		int startMonth = 0;
		int endMonth = 0;
		if(day + 7 > daysOfMonth[month]) {
			if(month == 11) {
				startMonth = month;
				endMonth = 0;
			}
			else {
				startMonth = month;
				endMonth = month+1;
			}
			startDay = day;
			endDay = 7 - Math.abs(daysOfMonth[month]-day)-1;
			if(endDay == 0) {
				endMonth = startMonth;
				endDay = daysOfMonth[endMonth];
			}
		}
		else {
			endDay = day+6;
			startDay = day;
			startMonth = month;
			endMonth = month;
		}
		todaysDateTextView=(TextView)findViewById(R.id.viewDateWV);

		if(startMonth == endMonth) {
			todaysDateTextView.setText(months[startMonth].substring(0,3) + "  "
					+ Integer.toString(startDay) + " - " + Integer.toString(endDay));
		} else {
			todaysDateTextView.setText(months[startMonth].substring(0,3) + "  "
					+ Integer.toString(startDay) + " - " + months[endMonth].substring(0,3) + "  "
					+ Integer.toString(endDay));
		}
		viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.weekview)));

		view = "week";
	}

	private void openDayView() {
		// TODO Auto-generated method stub
		view = "day";

		viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.dayview)));
		todaysDateTextView=(TextView)findViewById(R.id.viewDate);
		todaysDateTextView.setText(months[month] + " " + Integer.toString(day) + ", "
				+ Integer.toString(year));

		getEventsForThatDay(expListAdapter, day, month, year);
	}

	public void getEventsForThatDay(ExpandableListAdapter expList, int dd, int mm, int yy) {
		ArrayList<Event> tempList = new ArrayList<Event>();
		tempList = myEventsDataBaseAdapter.getAllEvents();
		Log.v("EVENTS IN DATABASE: ", tempList.toString());
		eventList.clear();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, dd);
		c.set(Calendar.MONTH, mm);
		c.set(Calendar.YEAR, yy);
		int dayOfWeekNum = c.get(Calendar.DAY_OF_WEEK) - 1;

		//only shows events for that day
		for (Event e : tempList) {
			Log.v("EVENT BEING ADDED: ", e.toString());
			//Log.v("EVENT DATE: ", e.getStartDate()[0] + " " + e.getStartDate()[1] + " " + e.getStartDate()[2]);
			if (e.getStartDate()[1] == dd && e.getEndDate()[1] == dd) {
				if (e.getStartDate()[0] == mm && e.getEndDate()[0] == mm) {
					if (e.getStartDate()[2] == yy && e.getEndDate()[2] == yy) {
						if (!e.getRepeatExceptAsArrayList().contains(e.getStartDateAsOneString())) {
							eventList.add(e);
							Log.v("Event added to list: ", e.toString());
						}
					}
				}
			} else if (e.getStartDate()[2] <= yy && e.getEndDate()[2] >= yy) {
				if (e.getStartDate()[0] <= mm && e.getEndDate()[0] >= mm) {
					if (e.getStartDate()[1] <= dd && e.getEndDate()[1] >= dd) {
						Log.v("start date", e.getStartDate()[1] + " " + dd);
						if (e.getStartTimeHour() == -1) {
							//do nothing
						} else if (e.getStartDate()[1] == dd) {
							e.setEndTimeHour(23);
							e.setEndTimeMin(59);
						} else if (e.getEndDate()[1] == dd) {
							e.setStartTimeHour(0);
							e.setStartTimeMin(0);
						} else {
							e.setStartTimeHour(-1);
							e.setStartTimeMin(-1);
							e.setEndTimeHour(-1);
							e.setEndTimeMin(-1);
						}
						if (!e.getRepeatExceptAsArrayList().contains(e.getStartDateAsOneString())) {
							Log.v("Event added to list: ", e.toString());
							eventList.add(e);
						}
					}
				}
			}
			Log.v("repeat length: ", e.getRepeat());
			if (e.getRepeat().length() > 1 && !eventList.contains(e)) {
				//e.setStartDate(mm, dd, yy);
				Calendar cStart = Calendar.getInstance();
				cStart.set(Calendar.DAY_OF_MONTH, e.getStartDate()[1]);
				cStart.set(Calendar.MONTH, e.getStartDate()[0]);
				cStart.set(Calendar.YEAR, e.getStartDate()[2]);
				Calendar cEnd = Calendar.getInstance();
				cEnd.set(Calendar.DAY_OF_MONTH, e.getEndDate()[1]);
				cEnd.set(Calendar.MONTH, e.getEndDate()[0]);
				cEnd.set(Calendar.YEAR, e.getEndDate()[2]);
				long diff = Math.abs(cEnd.getTime().getTime() - cStart.getTime().getTime());
				diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				int diffInt = (int) diff;
				Log.v("Diff: ", diffInt + " ");
				int tempdd, tempmm, tempyy;
				tempmm = mm;
				tempdd = dd;
				tempyy = yy;
				if(e.getRepeat().length() == 9) {
					if(e.getRepeatAsArrayList().get(dayOfWeekNum) == 1 && !eventList.contains(e)) {
						e.setStartDate(mm, dd, yy);
						if (tempdd + diffInt > daysOfMonth[mm]) {
							if (tempmm == 11) {
								tempmm = 0;
								tempyy++;
								tempdd = diffInt - daysOfMonth[11] - tempdd;
							} else {
								tempmm++;
								tempdd = diffInt - daysOfMonth[tempmm - 1] - tempdd;
							}
						} else {
							tempdd = tempdd + diffInt;
						}
						e.setEndDate(tempmm, tempdd, tempyy);
					}
				}
				else if (e.getRepeat().length() != 9) {
					Log.v("repeating monthly: ", dd + " " + diffInt + " " + e.getStartDate()[1]);
					for(int j = 0; j < e.getRepeatAsArrayList().size(); j++) {
						if (dd - diffInt <= e.getRepeatAsArrayList().get(j)) {
							tempdd = e.getRepeatAsArrayList().get(j);
							Log.v("Within the diff", "yay");
							break;
						}
					}
					if (e.getRepeatAsArrayList().contains(tempdd) && !eventList.contains(e)) {
						e.setStartDate(mm, tempdd, yy);
						if (tempdd + diffInt > daysOfMonth[mm]) {
							if (tempmm == 11) {
								tempmm = 0;
								tempyy++;
								tempdd = diffInt - daysOfMonth[11] - tempdd;
							} else {
								tempmm++;
								tempdd = diffInt - daysOfMonth[tempmm - 1] - tempdd;
							}
						} else {
							tempdd = tempdd + diffInt;
						}
						e.setEndDate(tempmm, tempdd, tempyy);
					}
				}
				/*
				else {
					//if (!(e.getRepeatAsArrayList().contains(dd) || e.getRepeatAsArrayList().get(dayOfWeekNum) == 1) && !eventList.contains(e)) {
					for (int j = 0; j < diffInt; j++) {
						Log.v("TEMP DATES START: ", tempmm + "/" + tempdd + "/" + tempyy);
						if (tempdd - 1 <= 0) {
							if (tempmm == 0) {
								tempmm = 11;
								tempyy--;
								tempdd = daysOfMonth[tempmm];
							} else {
								tempmm--;
								tempdd--;
							}
						} else
							tempdd = tempdd - 1;
						Calendar cdar = Calendar.getInstance();
						cdar.set(Calendar.DAY_OF_MONTH, tempdd);
						cdar.set(Calendar.MONTH, tempmm);
						cdar.set(Calendar.YEAR, tempyy);
						int tempDayOfWeekNum = cdar.get(Calendar.DAY_OF_WEEK) - 1;
						Log.v("TEMP DATES MID: ", tempmm + "/" + tempdd + "/" + tempyy + " " + tempDayOfWeekNum);
						if (e.getRepeatAsArrayList().get(tempDayOfWeekNum) == 1 || e.getRepeatAsArrayList().contains(dd)) {
							e.setStartDate(tempmm, tempdd, tempyy);
							if (tempdd + diffInt > daysOfMonth[mm]) {
								if (tempmm == 11) {
									tempmm = 0;
									tempyy++;
									tempdd = diffInt - daysOfMonth[11] - tempdd;
								} else {
									tempmm++;
									tempdd = diffInt - daysOfMonth[tempmm - 1] - tempdd;
								}
							} else {
								tempdd = tempdd + diffInt;
							}
							e.setEndDate(tempmm, tempdd, tempyy);
							Log.v("TEMP DATES END: ", tempmm + "/" + tempdd + "/" + tempyy + " " + tempDayOfWeekNum);
						}
					}
				}
				*/
				Log.v("Getting all dates:", e.getStartDateAsOneString() + " " + e.getEndDateAsOneString() + " " + mm + "/" + dd + "/" + yy + " " + diffInt);
				if (e.getStartDate()[1] == dd && e.getEndDate()[1] == dd) {
					if (e.getStartDate()[0] == mm && e.getEndDate()[0] == mm) {
						if (e.getStartDate()[2] == yy && e.getEndDate()[2] == yy) {
							if (!e.getRepeatExceptAsArrayList().contains(e.getStartDateAsOneString())) {
								eventList.add(e);
								Log.v("Event added to list: ", e.toString());
							}
						}
					}
				} else if (e.getStartDate()[2] <= yy && e.getEndDate()[2] >= yy) {
					if (e.getStartDate()[0] <= mm && e.getEndDate()[0] >= mm) {
						if (e.getStartDate()[1] <= dd && e.getEndDate()[1] >= dd) {
							Log.v("start date", e.getStartDate()[1] + " " + dd);
							if (e.getStartTimeHour() == -1) {
								//do nothing
							} else if (e.getStartDate()[1] == dd) {
								e.setEndTimeHour(23);
								e.setEndTimeMin(59);
							} else if (e.getEndDate()[1] == dd) {
								e.setStartTimeHour(0);
								e.setStartTimeMin(0);
							} else {
								e.setStartTimeHour(-1);
								e.setStartTimeMin(-1);
								e.setEndTimeHour(-1);
								e.setEndTimeMin(-1);
							}
							if (!e.getRepeatExceptAsArrayList().contains(e.getStartDateAsOneString()))
								eventList.add(e);
						}
					}
				}
			}
		}
		Collections.sort(eventList);
		expList.setEventList(eventList);
		expList.notifyDataSetChanged();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case (ActivityTwoRequestCode) : {
				if (resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
					// TODO Extract the data returned from the child Activity.
					Event tempEvent = (Event) data.getExtras().get("newEvent");
					String editChoice = (String) data.getExtras().get("edit");
					Log.v("TEMP EVENT: ", tempEvent.toString());
					if(tempEvent.getID() == -1) {
						long entry_id = myEventsDataBaseAdapter.insertEntry(userName, tempEvent.getEventName(), tempEvent.getLocation(),
								tempEvent.getDescrip(), tempEvent.getStartTimeHour() + "", tempEvent.getStartTimeMin() + "",
								tempEvent.getEndTimeHour() + "", tempEvent.getEndTimeMin() + "", tempEvent.getStartDate()[0] + "",
								tempEvent.getStartDate()[1] + "", tempEvent.getStartDate()[2] + "", tempEvent.getAllDay() + "",
								tempEvent.getRemind() + "", tempEvent.getPriv() + "", tempEvent.getEndDate()[0] + "",
								tempEvent.getEndDate()[1] + "", tempEvent.getEndDate()[2] + "", tempEvent.getCalendar(),
								tempEvent.getColor(), tempEvent.getRepeat(), tempEvent.getRepeatExcept());
						tempEvent.setID(entry_id);
						Toast.makeText(HomePage.this, "Event Successfully Created", Toast.LENGTH_LONG).show();
					}
					else {
						if(editChoice.equals("nochanges")) {
							Toast.makeText(HomePage.this, "No Changes Were Made", Toast.LENGTH_LONG).show();
						}
						else if(editChoice.equals("editall")) {
							myEventsDataBaseAdapter.updateEntry(tempEvent, tempEvent.getID());
							Log.v("UPDATING ALL: ", tempEvent.toString());
							Toast.makeText(HomePage.this, "Event Successfully Updated", Toast.LENGTH_LONG).show();
						}
						else if(editChoice.equals("editselected")) {
							Log.v("UPDATING SELECTED: ", tempEvent.toString());
							long entry_id = myEventsDataBaseAdapter.insertEntry(userName, tempEvent.getEventName(), tempEvent.getLocation(),
									tempEvent.getDescrip(), tempEvent.getStartTimeHour() + "", tempEvent.getStartTimeMin() + "",
									tempEvent.getEndTimeHour() + "", tempEvent.getEndTimeMin() + "", tempEvent.getStartDate()[0] + "",
									tempEvent.getStartDate()[1] + "", tempEvent.getStartDate()[2] + "", tempEvent.getAllDay() + "",
									tempEvent.getRemind() + "", tempEvent.getPriv() + "", tempEvent.getEndDate()[0] + "",
									tempEvent.getEndDate()[1] + "", tempEvent.getEndDate()[2] + "", tempEvent.getCalendar(),
									tempEvent.getColor(), "0", "");
							//tempEvent.setID(entry_id);
							//Event newTempEvent = tempEvent;
							//newTempEvent.setRepeat("1");
							//newTempEvent.setRepeatExcept("");
							//newTempEvent.setID(entry_id);
							//myEventsDataBaseAdapter.updateEntry(tempEvent, newTempEvent.getID());
							tempEvent = myEventsDataBaseAdapter.getSingleEntry_byID(tempEvent.getID());
							int[] startDate = tempEvent.getStartDate();
							String month = Integer.toString(startDate[0]);
							String day = Integer.toString(startDate[1]);
							String year = Integer.toString(startDate[2]);
							if(day.length() < 2)
								day = "0" + day;
							if(month.length() < 2)
								month = "0" + month;
							String repeatExceptThis = month + day + year;
							tempEvent.setRepeatExcept(tempEvent.getRepeat() + repeatExceptThis);
							myEventsDataBaseAdapter.updateEntry(tempEvent, tempEvent.getID());
						}

					}
					//eventList.add(tempEvent);
					//Log.d("OnActivityResult", tempEvent.toString());
					//Toast.makeText(HomePage.this, "Event Added: " + tempEvent.toString(), Toast.LENGTH_LONG).show();

					if(view.equals("day"))
						openDayView();
					else if(view.equals("week"))
						openWeekView();
					else if(view.equals("month"))
						openMonthView();
					else if(view.equals("year"))
						openYearView();
				}
				break;
			}
		}
	}

	private void setGridCellAdapterToDate(int month, int year) {
		adapter.setNewMonthDate(month, year);
		//c.set(year, month-1, c.get(Calendar.DAY_OF_MONTH));
		//adapter.notifyDataSetChanged();
	}

	private void setWeekGridCellAdapterToDate(int day, int month, int year) {
		weekAdapter.setNewDate(day, month, year);
	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		if(cal.isLeapYear(c.get(Calendar.YEAR))) {
			daysOfMonth[1] = 29;
		}
		else
			daysOfMonth[1] = 28;

		if(v.getId() == R.id.toggleLeft) {
			if(month <= 1) {
				if(day == 1) {
					year--;
					month = 11;
					day = 31;
				}
				else {
					day--;
				}
			}
			else if(day == 1) {
				month--;
				day = daysOfMonth[month];
			}
			else {
				day--;
			}
			openDayView();
		}
		else if(v.getId() == R.id.toggleLeftWV) {

			slideOutRight.setDuration(200);
			weekCalendarView.setAnimation(slideOutRight);
			slideOutRight.start();

			if(point.getX()> 0 && point.getX()<width) {
				ObjectAnimator anim = ObjectAnimator.ofFloat(point, "translationX", width + 200);
				anim.setDuration(200);
				anim.start();
			}
			Log.d("Point", point.getX() + "");

			int startDay = 0;
			int endDay = 0;
			int startMonth = 0;
			int endMonth = 0;
			if(day - 7 <= 0) {
				if(month == 0) {
					endMonth = month;
					month = 11;
					startMonth = month;
					year--;
				}
				else {
					endMonth = month;
					month--;
					startMonth = month;
				}
				startDay = daysOfMonth[month] - Math.abs(day-7);
				endDay = day;
			}
			else {
				endDay = day;
				startDay = day-7;
				startMonth = month;
				endMonth = month;
			}
			day = startDay;
			month = startMonth;
			openWeekView();
		}
		else if(v.getId() == R.id.toggleLeftMV) {

			slideOutRight.setDuration(200);
			calendarView.setAnimation(slideOutRight);
			slideOutRight.start();

			if(month <=1 ) {
				month = 11;
				year--;
			}
			else {
				month--;
			}
			todaysDateTextView=(TextView)findViewById(R.id.viewDateMV);
			todaysDateTextView.setText(months[month] + " "
					+ Integer.toString(year));
			openMonthView();
		}
		else if(v.getId() == R.id.toggleLeftYV) {

			slideOutRight.setDuration(200);
			yearViewGridLayout.setAnimation(slideOutRight);
			slideOutRight.start();

			year--;
			todaysDateTextView=(TextView)findViewById(R.id.viewDateYV);
			todaysDateTextView.setText(Integer.toString(year));
			openYearView();
		}
		else if(v.getId() == R.id.toggleRight) {
			if(month == 11) {
				if(day == 31) {
					year++;
					month = 0;
					day = 1;
				}
				else {
					day++;
				}
			}
			else if(day == daysOfMonth[month]) {
				month++;
				day = 1;
			}
			else {
				day++;
			}
			openDayView();
		}
		else if(v.getId() == R.id.toggleRightWV) {

			slideOutLeft.setDuration(200);
			weekCalendarView.setAnimation(slideOutLeft);
			slideOutLeft.start();

			if(point.getX() < width && point.getX() >0) {
				ObjectAnimator anim = ObjectAnimator.ofFloat(point, "translationX", -200);
				anim.setDuration(200);
				anim.start();
			}
			Log.d("Point", point.getX() + "");

			int startDay = 0;
			int endDay = 0;
			int startMonth = 0;
			int endMonth = 0;
			if(day + 14 > daysOfMonth[month]) {
				endDay = 14 - Math.abs(daysOfMonth[month]-day);
				if(month == 11) {
					if(day + 7 > daysOfMonth[month]) {
						startDay = 7 - Math.abs(daysOfMonth[month]-day);
						month = 0;
						startMonth = month;
						endMonth = month;
						year++;
					}
					else {
						startMonth = month;
						month = 0;
						endMonth = month;
						//year++;
						startDay = day + 7;
					}
				}
				else if (day + 7 > daysOfMonth[month]) {
					startDay = 7 - Math.abs(daysOfMonth[month]-day);
					month++;
					startMonth = month;
					endMonth = month;
				}
				else {
					startMonth = month;
					month++;
					endMonth = month;
					startDay = day+7;
				}
			}
			else {
				endDay = day+14;
				startDay = day+7;
				startMonth = month;
				endMonth = month;
			}
			day = startDay;
			month = startMonth;
			openWeekView();
		}
		else if(v.getId() == R.id.toggleRightMV) {

			slideOutLeft.setDuration(200);
			calendarView.setAnimation(slideOutLeft);
			slideOutLeft.start();

			if(month >= 11 ) {
				month = 0;
				year++;
			}
			else {
				month++;
			}
			todaysDateTextView=(TextView)findViewById(R.id.viewDateMV);
			todaysDateTextView.setText(months[month] +  " "
					+ Integer.toString(year));
			openMonthView();
		}
		else if(v.getId() == R.id.toggleRightYV) {
			slideOutLeft.setDuration(200);
			yearViewGridLayout.setAnimation(slideOutLeft);
			slideOutLeft.start();
			year++;
			todaysDateTextView=(TextView)findViewById(R.id.viewDateYV);
			todaysDateTextView.setText(Integer.toString(year));
			openYearView();
		}
	}

	public class GridCellAdapter extends BaseAdapter implements OnClickListener {

		private static final String tag = "GridCellAdapter";
		private final Context _context;
		private final ArrayList<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
		private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		private final int[] permDate = new int[3];
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView dayOfWeek;
		MyEventsDataBaseAdapter myEventsDataBaseAdapter;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		ExpandableListAdapter expLA;
		TextView dayPicked, dayPickedMonth;
		ImageView pointImage;
		Calendar calendar;
		String view;

		public GridCellAdapter(Context context, int textViewResourceId, int month, int year, ExpandableListAdapter exp, TextView dp, String v) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			expLA = exp;
			myEventsDataBaseAdapter = new MyEventsDataBaseAdapter(context);
			myEventsDataBaseAdapter = myEventsDataBaseAdapter.open();
			dayPickedMonth = dp;
			//Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
			calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			view = v;
			//Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			//Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			//Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());
			printMonth(month, year);
		}
		public GridCellAdapter(Context context, int textViewResourceId, int month, int year, int day, ExpandableListAdapter exp, TextView dp, ImageView point) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			myEventsDataBaseAdapter = new MyEventsDataBaseAdapter(context);
			myEventsDataBaseAdapter = myEventsDataBaseAdapter.open();
			expLA = exp;
			dayPicked = dp;
			pointImage = point;
			//Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
			calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			//Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			//Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			//Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());
			printWeek(month, year, day);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		private void printMonth(int mm, int yy) {
			//Log.d(tag, "==> printMOnth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);
			//Log.d(tag, "Current MOnth: " + currentMonthName + " having " + daysInMonth + " days");
			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

			if(currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				//Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}
			else if(currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				//Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}
			else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				//Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			//Log.d(tag, "Week Day: " + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
			//Log.d(tag, "No. Trailing space to add: " + trailingSpaces);
			//Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);
			if(cal.isLeapYear(cal.get(Calendar.YEAR))) {
				if (mm == 2) {
					daysInMonth++;
				}
				else if(mm == 3) {
					daysInPrevMonth++;
				}
			}

			//Trailing Month Days
			int dOW = 0; //Day of week starts on Sunday.
			for (int i = 0; i < trailingSpaces; i++) {
				//Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
				list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear + "-" + weekdays[dOW]);
				dOW++;
			}
			//Current Month Days
			for(int i = 1; i <=daysInMonth; i++) {
				//Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
				if (i == getCurrentDayOfMonth()) {
					if(dOW >= 7)
						list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy + "-NONE");
					else
						list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy + "-" + weekdays[dOW]);
				}
				else {
					if(dOW >= 7)
						list.add(String.valueOf(i) + "-BLACK" + "-" + getMonthAsString(currentMonth) + "-" + yy + "-NONE");
					else
						list.add(String.valueOf(i) + "-BLACK" + "-" + getMonthAsString(currentMonth) + "-" + yy + "-" + weekdays[dOW]);

				}
				dOW++;
			}

			//Leading Month Days
			for(int i = 0; i < list.size() % 7; i++) {
				//Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i+1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear + "-NONE");
			}
		}

		private void printWeek(int mm, int yy, int dd) {
			//Log.d(tag, "==> printWeek: mm: " + mm + " " + "yy: " + yy + " dd: " + dd);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);
			//Log.d(tag, "Current Month: " + currentMonthName + " having " + daysInMonth + " days");
			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, dd);

			if(currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				//Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}
			else if(currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				//Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}
			else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				//Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			//Log.d(tag, "Week Day: " + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
			//Log.d(tag, "No. Trailing space to add: " + trailingSpaces);
			//Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);
			if(cal.isLeapYear(cal.get(Calendar.YEAR))) {
				if (mm == 2) {
					daysInMonth++;
				}
				else if(mm == 3) {
					daysInPrevMonth++;
				}
			}

			//Trailing Month Days
			int tempDD = dd;
			int dOW = 0; //Day of week starts on Sunday.
			int pos = 0;
			for (int i = 0; i < trailingSpaces; i++) {
				//Log.d(tag, "PREV MONTH:= " + dd + " => " + String.valueOf(trailingSpaces) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
				if(tempDD - trailingSpaces + i<= 0) {
					list.add(String.valueOf((daysInPrevMonth - trailingSpaces) + i + DAY_OFFSET) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear + "-" + weekdays[dOW]);
				}
				else {
					list.add(String.valueOf((dd - trailingSpaces) + i) + "-BLACK" + "-" + getMonthAsString(currentMonth) + "-" + yy + "-" + weekdays[dOW]);
				}
				dOW++;
				pos++;
			}
			//Current Month Days
			int overflow = 1;
			//Log.d("Current Perm Date: ", permDate[0] + " " + permDate[1] + " " + permDate[2]);
			for(int i = 0; i <7-trailingSpaces; i++) {
				//Log.d(currentMonthName, String.valueOf(i+dd) + " " + getMonthAsString(currentMonth) + " " + yy);
				if (dd + i == c.get(Calendar.DAY_OF_MONTH)) {
					//Log.d("CURRENT: ", String.valueOf(dd + i) + " ?= " + c.get(Calendar.DAY_OF_MONTH));
					list.add(String.valueOf(dd + i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy + "-" + weekdays[dOW]);
				}
				else if (dd + i <= daysInMonth){
					list.add(String.valueOf(dd + i) + "-BLACK" + "-" + getMonthAsString(currentMonth) + "-" + yy + "-" + weekdays[dOW]);
				}
				else {
					list.add(String.valueOf(overflow) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear + "-" + weekdays[dOW]);
					overflow++;
				}
				dOW++;
				pos++;
			}
		}

		private void setNewDate(int dd, int mm, int yy) {
			list.clear();
			printWeek(mm, yy, dd);
			notifyDataSetChanged();
		}

		private void setNewMonthDate(int mm, int yy) {
			list.clear();
			printMonth(mm, yy);
			notifyDataSetChanged();
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		private void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public ArrayList<String> getDaysInWeek() {
			return list;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}

		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			String date_month_year = (String) v.getTag();
			Log.e("Selected date", date_month_year);

			try {
				Date parsedDate = dateFormatter.parse(date_month_year);
				//Log.d(tag, "Parsed Date: " + parsedDate.toString());
				String[] date = date_month_year.split("-");
				int tempDay = Integer.parseInt(date[0]);
				int tempMonth = Arrays.asList(months).indexOf(date[1]);
				int tempYear = Integer.parseInt(date[2]);
				int position = Integer.parseInt(date[3]);
				int[] posXY = new int[2];
				if (dayPicked != null) {
					//translates pointer in weekview
					int xCurrentPos = pointImage.getLeft();
					v.getLocationOnScreen(posXY);
					int xNextPos = posXY[0];
					Log.e("Translation", "From " + xCurrentPos + " to " + xNextPos);
					ObjectAnimator anim = ObjectAnimator.ofFloat(pointImage, "translationX", xNextPos);
					anim.setDuration(350);
					anim.start();
					dayPicked.setText(date[1] + " " + date[0] + ", " + date[2]);
				} else if (dayPickedMonth != null) {
					//year view click function
					dayPickedMonth.setText(date[1] + " " + date[0] + ", " + date[2]);
					month = tempMonth;
					day = tempDay;
					year = tempYear;
					((HomePage)_context).openMonthView();
					view = "month";
					Log.d("REACHED", "here");
				}
				Log.d(tag, "Parsed Date: " + tempMonth + "/" + tempDay + "/" + tempYear);
				((HomePage)_context).getEventsForThatDay(expLA, tempDay, tempMonth, tempYear);

			}
			catch (ParseException e) {
				e.printStackTrace();
			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}
			Button dayNum = (Button) row.findViewById(R.id.calendar_day_gridcell);
			TextView dayOfWeek = (TextView) row.findViewById(R.id.dayOfWeekLabel);

			if(dayPickedMonth != null && dayPicked == null && view.equals("year")) {
				LinearLayout screenGridCell = (LinearLayout) row.findViewById(R.id.screen_gridcell);
				//params.width = width/2;
				dayOfWeek.setTextSize(7);
				dayNum.setTextSize(10);
				ViewGroup.LayoutParams params = dayNum.getLayoutParams();
				ViewGroup.LayoutParams params2 = dayOfWeek.getLayoutParams();
				params.height = (width/4-params2.height)/(getCount()/7);
				screenGridCell.setPadding(0,0,0,0);
				//dayNum.setEnabled(false);
			}
			else
				dayNum.setEnabled(true);
			//get reference to the day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);
			dayOfWeek = (TextView) row.findViewById(R.id.dayOfWeekLabel);


			//Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			String theDayOfWeek = day_color[4];
			if (dayPicked!= null) {
				String[] parsedDayPicked = dayPicked.getText().toString().split(" ");
				//Log.v("PARSED stuff: ", theday + " - " + themonth + " - " + theyear + " / " + parsedDayPicked[1].substring(0, parsedDayPicked[1].length() - 1) + " - " + parsedDayPicked[0] + " - " + parsedDayPicked[2]);
				if (theday.equals(parsedDayPicked[1].substring(0, parsedDayPicked[1].length() - 1)) && themonth.equals(parsedDayPicked[0]) && theyear.equals(parsedDayPicked[2])) {
					int xNextPos = width / 7 * position;
					//Log.v("SUCCESS???: ", width / 7.0 + " * " + position + " = " + xNextPos);
					if (point.getVisibility() == View.INVISIBLE) {
						point.setX(xNextPos);
						point.setVisibility(View.VISIBLE);
					} else {
						ObjectAnimator anim = ObjectAnimator.ofFloat(point, "translationX", xNextPos);
						anim.setDuration(350);
						anim.start();
					}
				}
			}
			/*
			if((!eventsPerMonthMap.isEmpty() && eventsPerMonthMap != null)) { 
				if(eventsPerMonthMap.containsKey(theday)) { 
					num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());

				}
			}
			 */

			//set the day gridcell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear + "-" + Integer.valueOf(position));
			if(theDayOfWeek.equals("NONE"))
				dayOfWeek.setVisibility(View.GONE);
			else
				dayOfWeek.setText(theDayOfWeek);
			//Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear + theDayOfWeek + "-" + day_color[1]);
			if(day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources().getColor(R.color.lightgray));
			}
			if(day_color[1].equals("BLACK")) {
				gridcell.setTextColor(getResources().getColor(R.color.black));
			}
			if(day_color[1].equals("BLUE")) {
				//Log.d("BLUE: ", Arrays.asList(months).indexOf(themonth) + " =? " + calendar.get(Calendar.MONTH) + " " + year + " =? " + calendar.get(Calendar.YEAR));
				if(year == calendar.get(Calendar.YEAR) && Arrays.asList(months).indexOf(themonth)== calendar.get(Calendar.MONTH))
					gridcell.setTextColor(getResources().getColor(R.color.orange));
				//else
				//	gridcell.setTextColor(getResources().getColor(R.color.black));
			}
			return row;
		}

	};
}
