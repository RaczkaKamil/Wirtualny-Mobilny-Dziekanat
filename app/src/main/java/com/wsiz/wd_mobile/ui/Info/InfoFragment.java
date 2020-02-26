package com.wsiz.wd_mobile.ui.Info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wsiz.wd_mobile.R;

public class InfoFragment extends Fragment {

    ImageView fb_icon;
    ImageView discord_icon;
    ImageView wsiz_icon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);


        fb_icon=root.findViewById(R.id.fb_icon);
        discord_icon=root.findViewById(R.id.discord_icon);
        wsiz_icon=root.findViewById(R.id.wsiz_icon);

        fb_icon.setOnClickListener(view -> {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/WSIZBB/")));
        });

        discord_icon.setOnClickListener(view -> {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://discordapp.com/channels/555843675293483008/633317533109321738")));
        });

        wsiz_icon.setOnClickListener(view -> {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.wsi.edu.pl/")));
        });
        return root;
    }
}