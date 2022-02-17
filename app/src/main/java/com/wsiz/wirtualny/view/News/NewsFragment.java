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
import com.wsiz.wirtualny.model.db.RealmClasses.News;
import com.wsiz.wirtualny.presenter.News.NewsContract;
import com.wsiz.wirtualny.presenter.News.NewsPresenter;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.view.Activity.Login.LoginActivity;
import com.wsiz.wirtualny.view.Activity.SelectedNews.SelectedActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.RealmResults;

public class NewsFragment extends Fragment {
    private NewsContract.Presenter presenter;
    private ArrayList<String> MessageslistOfString = new ArrayList<>();
    private ArrayList<String> MessageslistOfString2 = new ArrayList<>();
    private ArrayList<String> MessageslistOfString3 = new ArrayList<>();
    private NewsListAdapter customAdapterr;

    private String TAG = "NEWS FRAGMENT";
    private Boolean isRefreshingProces = false;
    private Boolean isNewest = false;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView list_news;
    private EditText et_search;
    private RealmResults<News> newsRealmResults;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        MainActivity activity = (MainActivity) getActivity();
        Objects.requireNonNull(activity).setToolbarVisible(false);
        presenter = new NewsPresenter();
        presenter.downlaodData();
        initView(root);

        return root;
    }


    private void initView(View root){
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        list_news = root.findViewById(R.id.list_news);
        et_search = root.findViewById(R.id.et_search);
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
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d(TAG, "Refreshing...");
            isRefreshingProces = true;
            refreshList();
        });
        initList();
    }

    private void initList(){
        newsRealmResults = presenter.getNews();
        customAdapterr = new NewsListAdapter(newsRealmResults, getContext());
        list_news.setAdapter(customAdapterr);
        list_news.setOnItemClickListener((adapterView, view, i, l) -> {
            int ll = (int) l;
            connectToChosedMessage(ll);
        });
    }



    private void refreshList(){
        newsRealmResults = presenter.getNews();
        customAdapterr.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    private void connectToChosedMessage(int number) {
        Intent intent = new Intent(getActivity(), SelectedActivity.class);
        System.out.println("CHOSING: " + newsRealmResults.get(number).getId());
        intent.putExtra("select", (int) newsRealmResults.get(number).getId());
        startActivity(intent);
    }

    private void searchWord(String word) {
        System.out.println("SEARCH " +word);
         newsRealmResults = presenter.getNewsByName(word);
        customAdapterr = new NewsListAdapter(newsRealmResults, getContext());
        list_news.setAdapter(customAdapterr);
    }

}