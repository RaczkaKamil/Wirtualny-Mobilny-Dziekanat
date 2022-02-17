package com.wsiz.wirtualny.view.Grade;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.Pocket.GradesCalculated;
import com.wsiz.wirtualny.model.db.RealmClasses.Grades;

import java.math.BigDecimal;
import java.util.ArrayList;

public class GradesListAdapter extends ArrayAdapter<GradesCalculated> {

    public GradesListAdapter(ArrayList<GradesCalculated> data, Context context) {
        super(context, R.layout.list_grade, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GradesCalculated dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_grade, parent, false);

            viewHolder.topic = convertView.findViewById(R.id.tf_przedmiot);
            viewHolder.t1 = convertView.findViewById(R.id.tf_t1);
            viewHolder.t2 = convertView.findViewById(R.id.tf_t2);
            viewHolder.t3 = convertView.findViewById(R.id.tf_t3);
            viewHolder.activity = convertView.findViewById(R.id.tf_aktywnosc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }


        try {
            assert dataModel != null;
             viewHolder.topic.setText(dataModel.getNazwaPrzedmiotu());
             viewHolder.activity.setText("");

            String grade = dataModel.getOcenatypid()==0?"":String.valueOf(dataModel.getOcenatypid());
switch ((int) dataModel.getTerminid()){
    case 1:
        viewHolder.t1.setText(grade);
        viewHolder.t2.setText("");
        viewHolder.t3.setText("");
        break;
    case 2:
        viewHolder.t2.setText(grade);
        viewHolder.t1.setText("");
        viewHolder.t3.setText("");
        break;
    case 3:
        viewHolder.t3.setText(grade);
        viewHolder.t1.setText("");
        viewHolder.t2.setText("");
        break;
}




        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        return convertView;
    }

    public double getAvg(){
        double value = 0;
        for (int i = 0; i < getCount(); i++)
            if(getItem(i).getOcenatypid() > 0)
                value = value + getItem(i).getOcenatypid();
double res = 0;

try {
    res = new BigDecimal(value/getFiledLessons()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
}catch (NumberFormatException e){
    e.printStackTrace();
}
        return  res;
    }

    public int getFiledLessons() {
        int count = 0;
        for (int i = 0; i < getCount(); i++)
            if (getItem(i).getOcenatypid() > 0)
                count++;

            return count;
    }

public String getNumberOfLessons(){

        return getFiledLessons()+"/"+getCount();
}
    private class ViewHolder {
        TextView topic;
        TextView t1;
        TextView t2;
        TextView t3;
        TextView activity;

    }


}