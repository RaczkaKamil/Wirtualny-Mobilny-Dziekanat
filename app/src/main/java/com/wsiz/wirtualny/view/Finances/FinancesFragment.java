package com.wsiz.wirtualny.view.Finances;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wsiz.wirtualny.model.JsonAdapter.JsonFinances;
import com.wsiz.wirtualny.model.db.RealmClasses.Finances;
import com.wsiz.wirtualny.presenter.Finances.FinancesContract;
import com.wsiz.wirtualny.presenter.Finances.FinancesPresenter;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;
import com.wsiz.wirtualny.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.RealmResults;

public class FinancesFragment extends Fragment {
    private FinancesContract.Presenter presenter;
    private FinancesListAdapter customAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finances, container, false);
        presenter = new FinancesPresenter();
        presenter.downlaodData();
        initView(root);
        return root;
    }

    private void initView(View root) {
        ListView online_list = root.findViewById(R.id.finanse_list);
        customAdapter = new FinancesListAdapter(presenter.getFinances(), getContext());
        online_list.setAdapter(customAdapter);
        online_list.setClickable(false);
    }
}