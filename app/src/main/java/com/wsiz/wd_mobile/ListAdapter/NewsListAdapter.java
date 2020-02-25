package com.wsiz.wd_mobile.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsiz.wd_mobile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewsListAdapter extends ArrayAdapter<String> {

    Context mContext;
    private ArrayList<String> dataSet;
    private int lastPosition = -1;

    public NewsListAdapter(ArrayList<String>
                                   data, Context context) {
        super(context, R.layout.list_news, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_news, parent, false);
            viewHolder.tiltedOnList = convertView.findViewById(R.id.tiltedOnList);
            viewHolder.dataOnList = convertView.findViewById(R.id.dataOnList);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        try {
            String[] split = dataModel.split("~~");
            viewHolder.tiltedOnList.setText(split[0]);
            Date date;
            date = new Date();
            date.setTime(Long.valueOf(split[1]));
            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
            String date1 = format1.format(date);
            viewHolder.dataOnList.setText(date1);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tiltedOnList;
        TextView dataOnList;
        TextView standardOnList;
        ImageView imageonList;
    }


}