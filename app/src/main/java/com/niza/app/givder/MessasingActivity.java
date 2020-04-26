package com.niza.app.givder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.niza.app.givder.networking.actions.GiverMessageNetwork;

public class MessasingActivity extends AppCompatActivity {

    private GiverMessageNetwork[] giverMessageNetworkTexts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messasing);
    }
}
