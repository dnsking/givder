package com.niza.app.givder.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niza.app.givder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyContributionsFragment extends Fragment {


    public MyContributionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_contributions, container, false);
    }

}
