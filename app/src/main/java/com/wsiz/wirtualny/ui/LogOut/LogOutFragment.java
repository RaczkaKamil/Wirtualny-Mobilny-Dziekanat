package com.wsiz.wirtualny.ui.LogOut;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsiz.wirtualny.LoginActivity;
import com.wsiz.wirtualny.R;

public class LogOutFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Intent mySuperIntent = new Intent(getContext(), LoginActivity.class);
        mySuperIntent.putExtra("AutoLogin", "false");
        startActivity(mySuperIntent);
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}
