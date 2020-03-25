package com.wsiz.wirtualny.view.More;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.wsiz.wirtualny.view.Login.LoginActivity;
import com.wsiz.wirtualny.view.Main.MainActivity;
import com.wsiz.wirtualny.model.Pocket.FileReader;
import com.wsiz.wirtualny.R;

public class MoreFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_more, container, false);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setToolbarVisible(false);

        TextView u_name = root.findViewById(R.id.u_name);
        TextView u_index = root.findViewById(R.id.u_index);
        TextView u_konto = root.findViewById(R.id.u_konto);
        TextView u_star = root.findViewById(R.id.u_star);
        TextView u_log_out = root.findViewById(R.id.u_log_out);
        u_log_out.setOnClickListener(view -> {
                Intent mySuperIntent = new Intent(getContext(), LoginActivity.class);
                mySuperIntent.putExtra("AutoLogin","false");
                startActivity(mySuperIntent);
        });

        FileReader fileReader = new FileReader();
        fileReader.startReadUserID(getContext());
        String userData = fileReader.getUserData();
        String[] split = userData.split("/");

        u_name.setText(split[2]+" "+split[3]);
        u_index.setText(split[1]);
        if(split[5].contains("true")){
            u_konto.setText("AKTYWNE");
            u_konto.setTextColor(Color.parseColor("#32a852"));
        }else {
            u_konto.setText("NIEAKTYWNE");
            u_konto.setTextColor(Color.RED);
        }

        if(split[6].contains("false")){
            u_star.setText("Uregulowane");
            u_star.setTextColor(Color.parseColor("#32a852"));
        }else{
            u_star.setText("Nieuregulowane");
            u_star.setTextColor(Color.RED);
        }


        ImageView fb_icon = root.findViewById(R.id.fb_icon);
        ImageView discord_icon = root.findViewById(R.id.discord_icon);
        ImageView wsiz_icon = root.findViewById(R.id.wsiz_icon);

        fb_icon.setOnClickListener(view -> startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/WSIZBB/"))));

        discord_icon.setOnClickListener(view -> startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://discordapp.com/channels/555843675293483008/633317533109321738"))));

        wsiz_icon.setOnClickListener(view -> startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.wsi.edu.pl/"))));

        return root;
    }
}

