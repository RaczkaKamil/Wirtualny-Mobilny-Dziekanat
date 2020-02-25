package com.wsiz.wd_mobile.ui.News;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wsiz.wd_mobile.JsonAdapter.JsonNews;
import com.wsiz.wd_mobile.ListAdapter.NewsListAdapter;
import com.wsiz.wd_mobile.MainActivity;
import com.wsiz.wd_mobile.R;
import com.wsiz.wd_mobile.ui.SelectedNews.SelectedActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class NewsFragment extends Fragment {
    private JsonNews[] jsonNews;
    private ArrayList<String> MessageslistOfString = new ArrayList<>();
    private ArrayList<String> MessageslistOfString2 = new ArrayList<>();
    private ArrayList<String> MessageslistOfString3 = new ArrayList<>();
    private NewsListAdapter customAdapterr;
    private Snackbar bar;

    private Boolean isNewsLoaded = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        getNews();
        customAdapterr = new NewsListAdapter(MessageslistOfString3, getContext());

        bar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "Pobieranie danych...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) bar.getView();
        snack_view.addView(new ProgressBar(getContext()));
        bar.show();
        final ListView list_news = root.findViewById(R.id.list_news);
        EditText et_search = root.findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Kliknieto: " + s.toString());
                searchWord(s.toString());
            }
        });


        list_news.setAdapter(customAdapterr);
        list_news.setOnItemClickListener((adapterView, view, i, l) -> {
            int ll = (int) l;
            Toast.makeText(getContext(), "Wybrano element: " + ll, Toast.LENGTH_SHORT).show();
            connectToChosedMessage(ll);
        });


        return root;
    }

    private void connectToChosedMessage(int number) {
        Intent intent = new Intent(getActivity(), SelectedActivity.class);
        String[] btmS;
        btmS = MessageslistOfString3.get(number).split("~~");
        intent.putExtra("select", btmS);
        startActivity(intent);
    }

    private void searchWord(String word) {
        MessageslistOfString2.clear();
        MessageslistOfString3.clear();
        for (int i = 0; i < MessageslistOfString.size(); i++) {
            if (MessageslistOfString.get(i).toLowerCase().contains(word)) {
                MessageslistOfString2.add(MessageslistOfString.get(i));
            }
        }
        MessageslistOfString3.addAll(MessageslistOfString2);
        customAdapterr.notifyDataSetChanged();
    }

    private void getNews() {
        Thread thread = new Thread(() -> {
            try {
                MainActivity activity = (MainActivity) getActivity();

                assert activity != null;
                while (!activity.isSaved()) {
                    if (!isNewsLoaded) {
                        getAvailableNews("ODCZYTANO STARE NEWSY");
                        isNewsLoaded = true;
                    }
                }

                getAvailableNews("ODCZYTANO NOWE NEWSY");


            } catch (IOException | NullPointerException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void getAvailableNews(String message) throws IOException {
        String data;
        FileInputStream fileInputStream;
        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getNewsFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data).append("\n");
            String splited = stringBuffer.toString();
            System.out.println("----------------------------------" + message + "------------------------");
            System.out.println(splited);
            Gson gson = new Gson();
            jsonNews = gson.fromJson(splited, JsonNews[].class);
            setJson(jsonNews);
        }

    }


    private int getNewsFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(getContext()).fileList().length; i++) {
            if (getContext().fileList()[i].contains("News")) {
                return i;
            }
        }
        return -1;
    }

    private void setJson(JsonNews[] jsonNews) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            MessageslistOfString3.clear();
            customAdapterr.notifyDataSetChanged();
            for (JsonNews jsonNew : jsonNews) {
                MessageslistOfString.add(jsonNew.getTytyl() + "~~" + jsonNew.getDataut()
                        + "~~" + jsonNew.getTresc() + "~~" + jsonNew.getOgloszenieid() + "~~" + jsonNew.getFilename() + "~~" + jsonNew.getFileuuid());

            }


            MessageslistOfString3.addAll(MessageslistOfString);
            customAdapterr.notifyDataSetChanged();
            bar.dismiss();
        });
    }
}