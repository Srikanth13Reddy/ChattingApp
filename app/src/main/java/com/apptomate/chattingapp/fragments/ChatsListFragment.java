package com.apptomate.chattingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptomate.chattingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsListFragment extends Fragment {



    public ChatsListFragment() {
        // Required empty public constructor
    }

    public static ChatsListFragment newInstance(String param1, String param2) {
        ChatsListFragment fragment = new ChatsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats_list, container, false);
    }
}