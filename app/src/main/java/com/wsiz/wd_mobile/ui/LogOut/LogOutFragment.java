package com.wsiz.wd_mobile.ui.LogOut;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsiz.wd_mobile.LoginActivity;
import com.wsiz.wd_mobile.R;

public class LogOutFragment extends Fragment {


    public static LogOutFragment newInstance() {
        return new LogOutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Intent mySuperIntent = new Intent(getContext(), LoginActivity.class);
        mySuperIntent.putExtra("AutoLogin", "false");
        startActivity(mySuperIntent);
        return inflater.inflate(R.layout.log_out_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}
