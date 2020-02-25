package com.wsiz.wd_mobile.ListAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wsiz.wd_mobile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FinancesListAdapter extends ArrayAdapter<String> {

    Context mContext;
    private ArrayList<String> dataSet;
    private int lastPosition = -1;

    public FinancesListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_finances, data);
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
            convertView = inflater.inflate(R.layout.list_finances, parent, false);
            viewHolder.data = convertView.findViewById(R.id.tf_t1);
            // viewHolder.rodzaj = (TextView) convertView.findViewById(R.id.tf_t2);
            viewHolder.tytuł = convertView.findViewById(R.id.tf_t3);
            viewHolder.kwota = convertView.findViewById(R.id.tf_aktywnosc);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        try {
            String[] split = dataModel.split("~~");

            Date date;
            date = new Date();
            date.setTime(Long.valueOf(split[0]));
            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
            String date1 = format1.format(date);


            viewHolder.data.setText(date1);

            //viewHolder.rodzaj.setText(split[1]);

            viewHolder.tytuł.setText(split[2]);

            viewHolder.kwota.setText(split[3]);


        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView data;
        TextView rodzaj;
        TextView tytuł;
        TextView kwota;

    }


}