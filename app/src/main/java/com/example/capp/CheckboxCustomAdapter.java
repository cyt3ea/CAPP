package com.example.capp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Christine Tsou on 6/3/2015.
 */
public class CheckboxCustomAdapter extends ArrayAdapter<String> {
    String[] calendarNames;
    Context context;

    public CheckboxCustomAdapter(Context context, String[] x) {
        super(context,R.layout.checkbox_row, x);
        this.context = context;
        calendarNames = x;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.checkbox_row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.calendarName);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        name.setText(calendarNames[position]);
        cb.setChecked(true);
        return convertView;
    }
}
