package com.wsiz.wirtualny.view.News;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.db.RealmClasses.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmResults;


public class NewsListAdapter extends ArrayAdapter<News> {

    public NewsListAdapter(RealmResults<News>  data, Context context) {
        super(context, R.layout.list_news, data);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_news, parent, false);
            viewHolder.tiltedOnList = convertView.findViewById(R.id.tiltedOnList);
            viewHolder.dataOnList = convertView.findViewById(R.id.dataOnList);
            convertView.setTag(viewHolder);



        try {



            Date date;
            date = new Date();
            date.setTime(news.getDataut());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
            String date1 = format1.format(date);


            viewHolder.tiltedOnList.setText(news.getTytul());
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