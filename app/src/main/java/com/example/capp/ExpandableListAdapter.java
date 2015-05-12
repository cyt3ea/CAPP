package com.example.capp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
	//private Map<String, List<String>> laptopCollections;
	private ArrayList<Event> eventList;
	protected static final int ActivityTwoRequestCode = 0;
	MyEventsDataBaseAdapter eventsDBAdapter;

	public ExpandableListAdapter(Activity context, ArrayList<Event> events) {
		this.context = context;
		this.eventList = events;
		eventsDBAdapter = new MyEventsDataBaseAdapter(context);
		eventsDBAdapter = eventsDBAdapter.open();
	}

	public Event getChild(int groupPosition, int childPosition) {
		return eventList.get(groupPosition);
	}
	public void setEventList(ArrayList<Event> e) {
		eventList = e;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}
	
	
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		String eventDescrip = getChild(groupPosition, childPosition).getDescrip();
		String eventLocation = getChild(groupPosition, childPosition).getLocation();
		if(eventDescrip.equals(""))
			eventDescrip = "No Description";
		if(eventLocation.equals("")) 
			eventLocation = "Location: TBD";
		else 
			eventLocation = "Location: " + eventLocation;
		LayoutInflater inflater = context.getLayoutInflater();
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.eventDescrip);
		TextView item2 = (TextView) convertView.findViewById(R.id.eventLocation);

		item2.setTypeface(null, Typeface.ITALIC);

		item.setText(eventDescrip);
		item2.setText(eventLocation);
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Event getGroup(int groupPosition) {
		return eventList.get(groupPosition);
	}

	public int getGroupCount() {
		return eventList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String eventName = getGroup(groupPosition).getEventName();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.eventName);
		TextView item2 = (TextView) convertView.findViewById(R.id.eventTime);

		ImageView box = (ImageView) convertView.findViewById(R.id.rectimage);
		
		ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
		delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	new AlertDialog.Builder(context)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // continue with delete
                    	eventsDBAdapter.delete_byID(getGroup(groupPosition).getID());
                    	eventList.remove(getGroup(groupPosition));
                    	notifyDataSetChanged();
    					Toast.makeText(context, "Event Successfully Deleted", Toast.LENGTH_LONG).show();
                    }
                 })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // do nothing
                    }
                 })
                .setIcon(android.R.drawable.ic_dialog_alert)
                 .show();
            }
        });
		ImageView edit = (ImageView) convertView.findViewById(R.id.edit);
		edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent editEvent = new Intent(context, CreateEvent.class);
            	editEvent.putExtra("EVENT", getGroup(groupPosition));
				context.startActivityForResult(editEvent, ActivityTwoRequestCode);
                //v.getId() will give you the image id

            }
        });

		//Log.v("EXPANDABLE LIST ADAPTER", "getting color=" + getGroup(groupPosition).getColor());

		box.setBackgroundColor(Color.parseColor(getGroup(groupPosition).getColor()));
		//Log.v("EXPANDABLE LIST ADAPTER", "getting group view=" + eventName);
		
		//item.setTypeface(null, Typeface.BOLD);
		item.setText(eventName);
		
		String time = "";
		if(getGroup(groupPosition).getStartTimeHour() == -1) {
			time = "All Day";
		}
		else {
			SimpleDateFormat df = new SimpleDateFormat("h:mm a");
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, getGroup(groupPosition).getStartTimeHour());
			c.set(Calendar.MINUTE, getGroup(groupPosition).getStartTimeMin());
			String formattedDate = df.format(c.getTime());
			time = formattedDate;
			c.set(Calendar.HOUR_OF_DAY, getGroup(groupPosition).getEndTimeHour());
			c.set(Calendar.MINUTE, getGroup(groupPosition).getEndTimeMin());
			formattedDate = df.format(c.getTime());
			time = time + " - " + formattedDate;
		}
		item2.setText(time);
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}