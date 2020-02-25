package com.wsiz.wd_mobile.ListAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsiz.wd_mobile.R;

import java.util.ArrayList;

public class PersonelListAdapter extends ArrayAdapter<String> {

    Context mContext;
    private ArrayList<String> dataSet;
    private int lastPosition = -1;

    public PersonelListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_personel, data);
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
            convertView = inflater.inflate(R.layout.list_personel, parent, false);
            viewHolder.Name = convertView.findViewById(R.id.tf_Name);
            viewHolder.Email = convertView.findViewById(R.id.tf_Email);
            viewHolder.Image = convertView.findViewById(R.id.img_pictures);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        try {
            String[] split = dataModel.split("~~");

            viewHolder.Name.setText(split[0]);
            viewHolder.Email.setText(split[1]);
            //viewHolder.Image.setImageURI(Uri.parse(split[2]));


        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView Name;
        TextView Email;
        ImageView Image;
    }


}