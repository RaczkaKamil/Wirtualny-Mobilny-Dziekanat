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


public class NewsListAdapter extends ArrayAdapter<String> {

    public NewsListAdapter(ArrayList<String>
                                   data, Context context) {
        super(context, R.layout.list_news, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String dataModel = getItem(position);
        ViewHolder viewHolder;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_news, parent, false);
            viewHolder.tiltedOnList = convertView.findViewById(R.id.tiltedOnList);
            viewHolder.dataOnList = convertView.findViewById(R.id.dataOnList);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        try {

            assert dataModel != null;
            String[] split = dataModel.split("~~");

            Date date;
            date = new Date();
            date.setTime(Long.valueOf(split[1]));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
            String date1 = format1.format(date);


            viewHolder.tiltedOnList.setText(split[0]);
            viewHolder.dataOnList.setText(date1);

        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tiltedOnList;
        TextView dataOnList;
    }


}