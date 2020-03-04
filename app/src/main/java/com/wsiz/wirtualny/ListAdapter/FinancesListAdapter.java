package com.wsiz.wirtualny.ListAdapter;


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

public class FinancesListAdapter extends ArrayAdapter<String> {

    public FinancesListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_finances, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_finances, parent, false);
            viewHolder.data = convertView.findViewById(R.id.tf_t1);
            viewHolder.title = convertView.findViewById(R.id.tf_t3);
            viewHolder.price = convertView.findViewById(R.id.tf_aktywnosc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        try {
            assert dataModel != null;
            String[] split = dataModel.split("~~");

            Date date;
            date = new Date();
            date.setTime(Long.valueOf(split[0]));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
            String date1 = format1.format(date);


            viewHolder.data.setText(date1);
            viewHolder.title.setText(split[2]);
            viewHolder.price.setText(split[3]);


        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView data;
        TextView title;
        TextView price;

    }


}