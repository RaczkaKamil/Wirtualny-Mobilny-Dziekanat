package com.wsiz.wd_mobile.ui.Personnel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wsiz.wd_mobile.ListAdapter.PersonelListAdapter;
import com.wsiz.wd_mobile.R;

import java.util.ArrayList;

public class PersonnelFragment extends Fragment {


    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    PersonelListAdapter customAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_personnel, container, false);
        customAdapter = new PersonelListAdapter(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.personel_list);
        online_list.setAdapter(customAdapter);
        online_list.setClickable(false);
        MessageslistOfString.add("Kacper Ruciński" + "~~" + "kacper@onet.pl");
        MessageslistOfString.add("Kacper Ruciński" + "~~" + "kacper@onet.pl");
        //  MessageslistOfString.add("Kacper Ruciński~~kacper@onet.pl");
        //MessageslistOfString.add("Kacper Ruciński~~kacper@onet.pl");
        customAdapter.notifyDataSetChanged();
        return root;
    }
}