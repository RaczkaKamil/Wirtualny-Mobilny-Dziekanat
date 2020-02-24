package com.wsiz.wd_mobile.ListAdapter;


import android.content.Context;
import android.net.Uri;
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

public class PersonelListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> dataSet;
    Context mContext;

    public PersonelListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_personel, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    private class ViewHolder {
        TextView Name;
        TextView Email;
        ImageView Image;
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
            convertView = inflater.inflate(R.layout.list_personel, parent, false);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.tf_Name);
            viewHolder.Email = (TextView) convertView.findViewById(R.id.tf_Email);
            viewHolder.Image = (ImageView) convertView.findViewById(R.id.img_pictures);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



        try{
            String[] split = dataModel.split("~~");

                viewHolder.Name.setText(split[0]);
                viewHolder.Email.setText(split[1]);
                //viewHolder.Image.setImageURI(Uri.parse(split[2]));






        }catch (ArrayIndexOutOfBoundsException e){
            e.fillInStackTrace();
        }

        return convertView;
    }



}