package com.wsiz.wirtualny.model.ListAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsiz.wirtualny.R;

import java.util.ArrayList;

public class PersonelListAdapter extends ArrayAdapter<String> {

    public PersonelListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_personel, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_personel, parent, false);
            viewHolder.Name = convertView.findViewById(R.id.tf_Name);
            viewHolder.Email = convertView.findViewById(R.id.tf_Email);
            viewHolder.Image = convertView.findViewById(R.id.img_pictures);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        try {
            assert dataModel != null;
            String[] split = dataModel.split("~~");

            viewHolder.Name.setText(split[0]);
            viewHolder.Email.setText(split[1]);

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