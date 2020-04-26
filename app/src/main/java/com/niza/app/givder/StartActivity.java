package com.niza.app.givder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.niza.app.givder.Utils.Utils;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.Activity = this;
       // setContentView(R.layout.activity_start);
        if(Utils.IsFirstRun(this)){

            Intent intent = new Intent(this,SetupActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
