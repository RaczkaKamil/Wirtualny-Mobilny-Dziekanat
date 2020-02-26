package com.wsiz.wd_mobile.ListAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wsiz.wd_mobile.R;

import java.util.ArrayList;

public class GradesListAdapter extends ArrayAdapter<String> {

    public GradesListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_grade, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_grade, parent, false);

            viewHolder.przedmiot = convertView.findViewById(R.id.tf_przedmiot);
            viewHolder.t1 = convertView.findViewById(R.id.tf_t1);
            viewHolder.t2 = convertView.findViewById(R.id.tf_t2);
            viewHolder.t3 = convertView.findViewById(R.id.tf_t3);
            viewHolder.aktywnosc = convertView.findViewById(R.id.tf_aktywnosc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }


        try {
            assert dataModel != null;
            String[] split = dataModel.split("~~");
            viewHolder.przedmiot.setText(split[0]);
            if (!split[1].contains("0.0")) {
                viewHolder.t1.setText(split[1]);
            } else {
                viewHolder.t1.setText("");
            }
            if (!split[2].contains("0.0")) {
                viewHolder.t2.setText(split[2]);
            } else {
                viewHolder.t2.setText("");
            }
            if (!split[3].contains("0.0")) {
                viewHolder.t3.setText(split[3]);
            } else {
                viewHolder.t3.setText("");
            }
            if (!split[4].contains("0.0")) {
                viewHolder.aktywnosc.setText(split[4]);
            } else {
                viewHolder.aktywnosc.setText("");
            }


        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView przedmiot;
        TextView t1;
        TextView t2;
        TextView t3;
        TextView aktywnosc;

    }


}