package com.example.capp;

import java.util.ArrayList;

public class Day {
	ArrayList<Event> eventList = new ArrayList<Event>();
	String date = "";
	
	public void addEvent(Event newEvent) {
		eventList.add(newEvent);
	}
	public void removeEvent(Event e) {
		eventList.remove(e);
	}
	public ArrayList<Event> getEvents() {
		return eventList;
	}
	public String getDate() {
		return date;
	}	
}
