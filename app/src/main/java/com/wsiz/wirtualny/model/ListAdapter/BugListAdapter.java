package com.wsiz.wirtualny.model.ListAdapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wsiz.wirtualny.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BugListAdapter extends ArrayAdapter<String> {

    public BugListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_bug, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_bug, parent, false);
            viewHolder.tf_raport = convertView.findViewById(R.id.tf_raport);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        try {
            assert dataModel != null;

            viewHolder.tf_raport.setText(dataModel);

        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tf_raport;
    }


}