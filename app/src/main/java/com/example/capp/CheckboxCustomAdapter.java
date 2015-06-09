package com.example.capp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Christine Tsou on 6/3/2015.
 */
public class CheckboxCustomAdapter extends ArrayAdapter<String> {
    ArrayList<String> calendarNames, calendarsChecked;
    ArrayList<CheckBox> checkBoxes;
    Context context;
    CalendarDataBaseAdapter calendarDataBaseAdapter;
    MyEventsDataBaseAdapter eventsDBAdapt;
    Spinner calendarSpinner;
    Button selectAll;

    public CheckboxCustomAdapter(Context context, ArrayList<String> x, Button selectAll) {
        super(context,R.layout.checkbox_row, x);
        this.context = context;
        this.selectAll = selectAll;
        checkBoxes = new ArrayList<CheckBox>();
        calendarNames = x;
        calendarDataBaseAdapter = new CalendarDataBaseAdapter(context);
        calendarDataBaseAdapter = calendarDataBaseAdapter.open();
        eventsDBAdapt = new MyEventsDataBaseAdapter(context);
        eventsDBAdapt = eventsDBAdapt.open();
        ArrayList<String> eventList = calendarDataBaseAdapter.getAllCalendars();
        calendarsChecked = eventList;
        //addAllCalendarsToChecked();
    }

    public void clearCalendarsChecked() {
        calendarsChecked.clear();
        String calendarViewOpen = ((HomePage) context).getCalendarView();
        if (calendarViewOpen.equals("day"))
            ((HomePage) context).openDayView();
        else if (calendarViewOpen.equals("week"))
            ((HomePage) context).openWeekView();
        else if (calendarViewOpen.equals("month"))
            ((HomePage) context).openMonthView();
        else if (calendarViewOpen.equals("year"))
            ((HomePage) context).openYearView();
        Log.v("Cleared Calendars: ", calendarsChecked.toString());
    }

    public void addAllCalendarsToChecked() {
        ArrayList<String> eventList = calendarDataBaseAdapter.getAllCalendars();
        calendarsChecked.clear();
        calendarsChecked.addAll(eventList);
        String calendarViewOpen = ((HomePage) context).getCalendarView();
        if (calendarViewOpen.equals("day"))
            ((HomePage) context).openDayView();
        else if (calendarViewOpen.equals("week"))
            ((HomePage) context).openWeekView();
        else if (calendarViewOpen.equals("month"))
            ((HomePage) context).openMonthView();
        else if (calendarViewOpen.equals("year"))
            ((HomePage) context).openYearView();
        Log.v("Checked All Calendars: ", calendarsChecked.toString());

    }

    public void setCalendarNames(ArrayList<String> c) {
        Log.v("Setting Calendar: ", c.toString());
        this.calendarNames = c;
        this.notifyDataSetChanged();
    }

    public ArrayList<CheckBox> getCheckBoxes() { return checkBoxes; }

    public ArrayList<String> getCalendarsChecked() { return calendarsChecked; }

    public AlertDialog.Builder createDeleteCalendarDialog(String calendarToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.delete_calendar_dialog, null);

        calendarSpinner = (Spinner) v.findViewById(R.id.calendarSpinner);
        ArrayList<String> existingCalendars = calendarDataBaseAdapter.getAllCalendars();
        existingCalendars.remove(calendarToDelete);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, existingCalendars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calendarSpinner.setAdapter(spinnerAdapter);
        calendarSpinner.setSelection(0);

        builder.setView(v)
                // Add action buttons
                .setTitle("Delete " + calendarToDelete + " Calendar")
                        .setMessage("Select new calendar for all existing events in selected calendar to delete.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //LoginDialogFragment.this.getDialog().cancel();
                            }
                        });
        return builder;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.checkbox_row, parent, false);
        final TextView name = (TextView) convertView.findViewById(R.id.calendarName);
        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBoxes.add(cb);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked())
                    calendarsChecked.add(name.getText().toString());
                else
                    calendarsChecked.remove(calendarsChecked.indexOf(name.getText().toString()));
                if(getCalendarsChecked().size() == 0)
                    selectAll.setText("Select All");
                else if(getCalendarsChecked().size() == calendarNames.size())
                    selectAll.setText("Deselect All");
                Log.v("Calendars Checked: ", calendarsChecked.toString());
                String calendarViewOpen = ((HomePage) context).getCalendarView();
                if (calendarViewOpen.equals("day"))
                    ((HomePage) context).openDayView();
                else if (calendarViewOpen.equals("week"))
                    ((HomePage) context).openWeekView();
                else if (calendarViewOpen.equals("month"))
                    ((HomePage) context).openMonthView();
                else if (calendarViewOpen.equals("year"))
                    ((HomePage) context).openYearView();
            }
        });
        name.setText(calendarNames.get(position));
        final ImageView deleteRow = (ImageView) convertView.findViewById(R.id.deleteRow);
        ImageView editRow = (ImageView) convertView.findViewById(R.id.editRow);
        if(position == 0) {
            deleteRow.setVisibility(View.INVISIBLE);
        }
        else {
            deleteRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder deleteDialog = createDeleteCalendarDialog(name.getText().toString());
                    deleteDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            calendarDataBaseAdapter.deleteEntry(name.getText().toString());
                            calendarNames.remove(name.getText().toString());
                            ArrayList<Event> eventList = eventsDBAdapt.getAllEvents();
                            for (Event e : eventList) {
                                if (e.getCalendar().equals(name.getText().toString())) {
                                    e.setCalendar(calendarSpinner.getSelectedItem().toString());
                                    e.setColor(calendarDataBaseAdapter.getSingleEntry(calendarSpinner.getSelectedItem().toString()));
                                    eventsDBAdapt.updateEntry(e, e.getID());
                                }
                            }
                            notifyDataSetChanged();
                            String calendarViewOpen = ((HomePage) context).getCalendarView();
                            if (calendarViewOpen.equals("day"))
                                ((HomePage) context).openDayView();
                            else if (calendarViewOpen.equals("week"))
                                ((HomePage) context).openWeekView();
                            else if (calendarViewOpen.equals("month"))
                                ((HomePage) context).openMonthView();
                            else if (calendarViewOpen.equals("year"))
                                ((HomePage) context).openYearView();
                        }
                    });
                    Dialog deleteCalDialog = deleteDialog.create();
                    deleteCalDialog.show();
                }
            });
        }
        editRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof HomePage) {
                    final LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                    AlertDialog.Builder editCalendarBuilder = ((HomePage) context).onCreateDialog();
                    View editView = inflater.inflate(R.layout.dialog_create_calendar, null);
                    final EditText calendarName = (EditText) editView.findViewById(R.id.calendarNameInput);
                    final Spinner colorSpin = (Spinner) editView.findViewById(R.id.colorC);
                    final ArrayList<String> colorList = new ArrayList<String>(Arrays.asList("Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Black"));
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, colorList) {
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
                                    tv.setTextColor(getContext().getResources().getColor(R.color.reds));
                                    break;
                                case 1:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.oranges));
                                    break;
                                case 2:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.yellows));
                                    break;
                                case 3:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.greens));
                                    break;
                                case 4:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.blues));
                                    break;
                                case 5:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.purples));
                                    break;
                                case 6:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.pinks));
                                    break;
                                case 7:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.blacks));
                                    break;
                            }
                            return v;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
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
                                    tv.setTextColor(getContext().getResources().getColor(R.color.reds));
                                    break;
                                case 1:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.oranges));
                                    break;
                                case 2:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.yellows));
                                    break;
                                case 3:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.greens));
                                    break;
                                case 4:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.blues));
                                    break;
                                case 5:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.purples));
                                    break;
                                case 6:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.pinks));
                                    break;
                                case 7:
                                    tv.setTextColor(getContext().getResources().getColor(R.color.blacks));
                                    break;
                            }
                            return v;
                        }
                    };
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    colorSpin.setAdapter(spinnerAdapter);
                    final String nameText = name.getText().toString();
                    final String color = calendarDataBaseAdapter.getSingleEntry(name.getText().toString());
                    Log.v("Calendar Color: ", color);
                    if (color.equals("Red"))
                        colorSpin.setSelection(0);
                    else if (color.equals("Orange"))
                        colorSpin.setSelection(1);
                    else if (color.equals("Yellow"))
                        colorSpin.setSelection(2);
                    else if (color.equals("Green"))
                        colorSpin.setSelection(3);
                    else if (color.equals("Blue"))
                        colorSpin.setSelection(4);
                    else if (color.equals("Purple"))
                        colorSpin.setSelection(5);
                    else if (color.equals("Pink"))
                        colorSpin.setSelection(6);
                    else if (color.equals("Black"))
                        colorSpin.setSelection(7);
                    calendarName.setText(nameText);
                    editCalendarBuilder.setView(editView);
                    editCalendarBuilder.setPositiveButton("Edit Calendar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectAll.setText("Deselect All");
                            calendarDataBaseAdapter.updateEntry(nameText, calendarName.getText().toString(), colorSpin.getSelectedItem().toString());
                            calendarNames.set(position, calendarName.getText().toString());
                            ArrayList<Event> eventList = new ArrayList<Event>();
                            eventList = eventsDBAdapt.getAllEvents();
                            for (Event e : eventList) {
                                if (e.getCalendar().equals(nameText)) {
                                    e.setCalendar(calendarName.getText().toString());
                                    e.setColor(colorSpin.getSelectedItem().toString());
                                    eventsDBAdapt.updateEntry(e, e.getID());
                                }
                            }
                            notifyDataSetChanged();
                            String calendarViewOpen = ((HomePage) context).getCalendarView();
                            if (calendarViewOpen.equals("day"))
                                ((HomePage) context).openDayView();
                            else if (calendarViewOpen.equals("week"))
                                ((HomePage) context).openWeekView();
                            else if (calendarViewOpen.equals("month"))
                                ((HomePage) context).openMonthView();
                            else if (calendarViewOpen.equals("year"))
                                ((HomePage) context).openYearView();
                        }
                    });
                    Dialog editCalendar = editCalendarBuilder.create();
                    editCalendar.show();
                }

            }
        });
        cb.setChecked(true);
        return convertView;
    }
}
