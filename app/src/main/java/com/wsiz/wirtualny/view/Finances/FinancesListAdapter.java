package com.wsiz.wirtualny.view.Finances;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.db.RealmClasses.Finances;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import io.realm.RealmResults;

public class FinancesListAdapter extends ArrayAdapter<Finances> {

    public FinancesListAdapter(RealmResults<Finances> data, Context context) {
        super(context, R.layout.list_finances, data);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Finances finances = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.list_finances, parent, false);
        viewHolder.data = convertView.findViewById(R.id.tf_t1);
        viewHolder.title = convertView.findViewById(R.id.tf_t3);
        viewHolder.price = convertView.findViewById(R.id.tf_aktywnosc);
        convertView.setTag(viewHolder);
        setView(viewHolder,finances);
        return convertView;
    }

    private void setView(ViewHolder viewHolder, Finances finances){
        Date date;
        date = new Date();
        date.setTime(Objects.requireNonNull(finances).getDate());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        String correct_date = format1.format(date);

        viewHolder.data.setText(correct_date);
        viewHolder.title.setText(finances.getDetails());
        viewHolder.price.setText(String.valueOf(finances.getAmount()));
    }

    private static class ViewHolder {
        TextView data;
        TextView title;
        TextView price;

    }
}