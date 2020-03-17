package com.wsiz.wirtualny.view.News;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wsiz.wirtualny.model.JsonAdapter.JsonNews;
import com.wsiz.wirtualny.model.ListAdapter.NewsListAdapter;
import com.wsiz.wirtualny.view.Main.MainActivity;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.view.Login.LoginActivity;
import com.wsiz.wirtualny.view.SelectedNews.SelectedActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class NewsFragment extends Fragment {
    private ArrayList<String> MessageslistOfString = new ArrayList<>();
    private ArrayList<String> MessageslistOfString2 = new ArrayList<>();
    private ArrayList<String> MessageslistOfString3 = new ArrayList<>();
    private NewsListAdapter customAdapterr;

    private String TAG = "NEWS FRAGMENT";
    private Boolean isRefreshingProces = false;
    private Boolean isNewest = false;

    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        customAdapterr = new NewsListAdapter(MessageslistOfString3, getContext());
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setToolbarVisible(false);


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
                searchWord(s.toString());
            }
        });


        list_news.setAdapter(customAdapterr);
        list_news.setOnItemClickListener((adapterView, view, i, l) -> {
            int ll = (int) l;
            connectToChosedMessage(ll);
        });


        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d(TAG, "Refreshing...");
            isRefreshingProces = true;
            refreshNews();
        });


        getNews();

        return root;
    }

    private void refreshNews() {
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.downloadComponents();
        getNews();

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
                boolean isNewsLoaded = false;
                assert activity != null;
                while (!activity.isSaved()) {
                    if (!isNewsLoaded) {
                        getAvailableNews("readed oldest news");
                        isNewsLoaded = true;
                    }
                }
                isNewest=true;
                getAvailableNews("readed newest news");


            } catch (IOException | NullPointerException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void getAvailableNews(String message) throws IOException {
        try {
            String data;
            FileInputStream fileInputStream;
            fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getNewsFileNumber()]);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();


            while ((data = bufferedReader.readLine()) != null) {
                stringBuffer.append(data).append("\n");
                String splited = stringBuffer.toString();
                Log.d(TAG, message);
                Gson gson = new Gson();
                JsonNews[] jsonNews = gson.fromJson(splited, JsonNews[].class);
                setJson(jsonNews);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                Toast.makeText(getContext(), "Blad logowania!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            });
            e.fillInStackTrace();
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
                MessageslistOfString.add(
                        jsonNew.getTytyl() + "~~"
                                + jsonNew.getDataut() + "~~"
                                + jsonNew.getTresc() + "~~"
                                + jsonNew.getOgloszenieid() + "~~"
                                + jsonNew.getFilename() + "~~"
                                + jsonNew.getFileuuid());
            }
            MessageslistOfString3.addAll(MessageslistOfString);
            customAdapterr.notifyDataSetChanged();
            if (isRefreshingProces&&isNewest) {

                isRefreshingProces =false;
                isNewest=false;

                Toast.makeText(getContext(), "Odświeżono!", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}