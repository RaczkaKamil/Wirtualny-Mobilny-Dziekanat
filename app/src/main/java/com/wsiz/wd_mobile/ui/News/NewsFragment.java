package com.wsiz.wd_mobile.ui.News;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
        JsonNews jsonNews[];
    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    ArrayList<String> MessageslistOfString2 = new ArrayList<String>();
    ArrayList<String> MessageslistOfString3 = new ArrayList<String>();
    ArrayList<String> MessageslistOfString4 = new ArrayList<String>();
    EditText et_search;
    NewsListAdapter customAdapterr;
    Snackbar bar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        getNews(root);
        customAdapterr = new NewsListAdapter(MessageslistOfString3, getContext());

        bar= Snackbar.make(getActivity().findViewById(android.R.id.content), "Pobieranie danych...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) bar.getView();
        snack_view.addView(new ProgressBar(getContext()));
        bar.show();
        final ListView list_news = root.findViewById(R.id.list_news);
        et_search= root.findViewById(R.id.et_search);
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
        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int ll = (int) l;
                Toast.makeText(getContext(), "Wybrano element: "+ll, Toast.LENGTH_SHORT).show();
                connectToChosedMessage(ll);
            }
        });


        return root;
    }

    private void connectToChosedMessage(int number){
        Intent intent = new Intent(getActivity(), SelectedActivity.class);
        String btmS[] = new String[6];
        btmS=MessageslistOfString3.get(number).split("~~");
        intent.putExtra("select", btmS);
        startActivity(intent);
    }

    public void searchWord(String word) {
        MessageslistOfString2.clear();
        MessageslistOfString3.clear();
        MessageslistOfString4.clear();
        for (int i = 0; i < MessageslistOfString.size(); i++) {
            if(MessageslistOfString.get(i).toLowerCase().contains(word)){
                MessageslistOfString2.add(MessageslistOfString.get(i));
            }
        }
        MessageslistOfString3.addAll(MessageslistOfString2);
        customAdapterr.notifyDataSetChanged();
    }

    private void getNews(View view){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    MainActivity activity = (MainActivity) getActivity();
                    while (!activity.isSaved()) {
                        String data;
                        FileInputStream fileInputStream = null;
                        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()    [getNewsFileNumber()]);
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuffer stringBuffer = new StringBuffer();


                        while ((data = bufferedReader.readLine()) != null) {
                            stringBuffer.append(data + "\n");
                            String splited = stringBuffer.toString();
                            System.out.println("----------------------------------ODCZYTANO STARE NEWSY------------------------");
                            System.out.println(splited);
                            Gson gson = new Gson();
                            jsonNews = gson.fromJson(splited, JsonNews[].class);
                            setJson(jsonNews);
                        }

                    }

                        if (activity.isSaved()) {
                            String data;
                            FileInputStream fileInputStream = null;
                            fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()    [getNewsFileNumber()]);
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            StringBuffer stringBuffer = new StringBuffer();


                            while ((data = bufferedReader.readLine()) != null) {
                                stringBuffer.append(data + "\n");
                                String splited = stringBuffer.toString();
                                System.out.println("----------------------------------ODCZYTANO NEWSY------------------------");
                                System.out.println(splited);
                                Gson gson = new Gson();
                                jsonNews = gson.fromJson(splited, JsonNews[].class);
                                setJson(jsonNews);
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                    e.printStackTrace();

                }
            }
        });

        thread.start();
    }


    private int getNewsFileNumber(){
        for (int i = 0; i < Objects.requireNonNull(getContext()).fileList().length; i++) {
            if(getContext().fileList()[i].contains("News")){
                return i;
            }
        }
        return -1;
        }

    public void setJson(JsonNews[] jsonNews) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageslistOfString3.clear();
                customAdapterr.notifyDataSetChanged();
        for(
            int i = 0;
            i<jsonNews.length;i++)

            {
                MessageslistOfString.add(jsonNews[i].getTytyl() + "~~" + jsonNews[i].getDataut()
                        + "~~" + jsonNews[i].getTresc() + "~~" + jsonNews[i].getOgloszenieid() + "~~" + jsonNews[i].getFilename() + "~~" + jsonNews[i].getFileuuid());

            }


        MessageslistOfString3.addAll(MessageslistOfString);
        customAdapterr.notifyDataSetChanged();
        bar.dismiss();
            }
        });
    }
}