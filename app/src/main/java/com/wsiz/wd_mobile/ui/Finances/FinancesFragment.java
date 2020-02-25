package com.wsiz.wd_mobile.ui.Finances;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.wsiz.wd_mobile.JsonAdapter.JsonFinances;
import com.wsiz.wd_mobile.ListAdapter.FinancesListAdapter;
import com.wsiz.wd_mobile.MainActivity;
import com.wsiz.wd_mobile.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class FinancesFragment extends Fragment {
    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    FinancesListAdapter customAdapterr;
    JsonFinances[] jsonFinances;
    Boolean isFinansesLoaded = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finances, container, false);

        customAdapterr = new FinancesListAdapter(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.finanse_list);
        online_list.setAdapter(customAdapterr);
        online_list.setClickable(false);
        customAdapterr.notifyDataSetChanged();

        getFinances();
        return root;
    }

    private void getFinances() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity activity = (MainActivity) getActivity();
                    while (!activity.isSaved()) {
                        if (!isFinansesLoaded) {
                            getAvailableFinances("ODCZYTANO STARE FINANSE");
                            isFinansesLoaded = true;
                        }

                    }

                    getAvailableFinances("ODCZYTANO NOWE FINANSE");

                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void getAvailableFinances(String message) throws IOException {
        String data;
        FileInputStream fileInputStream = null;
        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getFinancesFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data + "\n");
            String splited = stringBuffer.toString();
            System.out.println("----------------------------------" + message + "------------------------");
            System.out.println(splited);
            Gson gson = new Gson();
            jsonFinances = gson.fromJson(splited, JsonFinances[].class);
            setJson(jsonFinances);
        }
    }

    private int getFinancesFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(getContext()).fileList().length; i++) {
            if (getContext().fileList()[i].contains("Finances")) {
                return i;
            }
        }
        return -1;
    }

    public void setJson(JsonFinances[] jsonFinances) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < jsonFinances.length; i++) {
                    MessageslistOfString.add(
                            jsonFinances[i].getDate() + "~~" +
                                    jsonFinances[i].getType() + "~~" +
                                    jsonFinances[i].getDetails() + "~~" +
                                    jsonFinances[i].getAmount() + " zł" + "~~");
                    customAdapterr.notifyDataSetChanged();
                }

            }
        });
    }
}