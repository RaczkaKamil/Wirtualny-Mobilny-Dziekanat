package com.wsiz.wirtualny.view.Blank;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.view.Main.MainActivity;
import com.wsiz.wirtualny.view.News.NewsFragment;


public class BlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blank, container, false);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.loadFragment(new NewsFragment(),1);

        Fragment newFragment = new NewsFragment();

        FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
        return root;
    }




}