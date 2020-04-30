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
import com.wsiz.wirtualny.model.ListAdapter.FinancesListAdapter;
import com.wsiz.wirtualny.view.Main.MainActivity;
import com.wsiz.wirtualny.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class FinancesFragment extends Fragment {
    private ArrayList<String> MessageslistOfString = new ArrayList<>();
    private FinancesListAdapter customAdapterr;
    private Boolean isFinansesLoaded = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finances, container, false);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setToolbarVisible(false);

        customAdapterr = new FinancesListAdapter(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.finanse_list);
        online_list.setAdapter(customAdapterr);
        online_list.setClickable(false);
        customAdapterr.notifyDataSetChanged();

        getFinances();
        return root;
    }

    private void getFinances() {
        Thread thread = new Thread(() -> {
            try {
                MainActivity activity = (MainActivity) getActivity();
                assert activity != null;
                while (!activity.isSaved()) {
                    if (!isFinansesLoaded) {
                        getAvailableFinances("readed oldest finances");
                        isFinansesLoaded = true;
                    }
                }
                getAvailableFinances("readed newest finances");
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    private void getAvailableFinances(String message) throws IOException {
        String data;
        FileInputStream fileInputStream;
        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getFinancesFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data).append("\n");
            String splited = stringBuffer.toString();
            String TAG = "Finances fragment";
            Log.d(TAG,message);
            Gson gson = new Gson();
            try{
                JsonFinances[] jsonFinances = gson.fromJson(splited, JsonFinances[].class);
                setJson(jsonFinances);
            }catch (JsonSyntaxException e){
                e.fillInStackTrace();
            }



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

    private void setJson(JsonFinances[] jsonFinances) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            for (JsonFinances jsonFinance : jsonFinances) {
                MessageslistOfString.add(
                        jsonFinance.getDate() + "~~" +
                                jsonFinance.getType() + "~~" +
                                jsonFinance.getDetails() + "~~" +
                                jsonFinance.getAmount() + " zł" + "~~");
                customAdapterr.notifyDataSetChanged();
            }

        });
    }
}