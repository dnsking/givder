package com.niza.app.givder;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.niza.app.givder.Utils.NetUnits;
import com.niza.app.givder.Utils.UiUtils;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.data.user.Authentication;
import com.niza.app.givder.db.helpers.GivderContentDBHelper;
import com.niza.app.givder.fragments.CameraFragment;
import com.niza.app.givder.fragments.ImagesFragment;
import com.niza.app.givder.fragments.LocationSelectionFragment;
import com.niza.app.givder.fragments.NewPostPlatesDateDescriptionFragment;
import com.niza.app.givder.networking.actions.UserNetworkAction;
import com.niza.app.givder.networking.authentication.GivderAccountHelper;
import com.niza.app.givder.networking.authentication.GivderContentHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.Date;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;

public class NewPostActivity extends AppCompatActivity implements PermissionCallback {

    private StepperLayout mStepperLayout;

    private NewPostPlatesDateDescriptionFragment newPostPlatesDateDescriptionFragment;
    private LocationSelectionFragment locationSelectionFragment;
    private ImagesFragment imagesFragment;
    private CameraFragment cameraFragment;
    private Button grantButton;
    private UserNetworkAction userNetworkAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_new_post);
        grantButton = findViewById(R.id.grantButton);

        grantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nammu.askForPermission(
                        NewPostActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION
                        } ,  NewPostActivity.this);

            }
        });

      if(Nammu.hasPermission(this, new String[]{WRITE_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION })){
          init();
      }
      else{}


    }
    private void init(){


        findViewById(R.id.permissionView).setVisibility(View.GONE);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setListener(new StepperLayout.StepperListener() {
            @Override
            public void onCompleted(View completeButton) {


                userNetworkAction = new UserNetworkAction(

               Utils.GetUserName(NewPostActivity.this)+"_"+ new Date().getTime(),
                        Utils.GetUserName(NewPostActivity.this),
                        Utils.GetAccountType(NewPostActivity.this),
                       Double.toString(locationSelectionFragment.getLat()) ,
                        Double.toString(locationSelectionFragment.getLOn())    ,
                      new Date().getTime()+
                      Long.parseLong( newPostPlatesDateDescriptionFragment.getPostExpirationInHours()) ,
                        newPostPlatesDateDescriptionFragment.getDescription(),
                        newPostPlatesDateDescriptionFragment.getNumberOfPlates(),
                Integer.toString(UiUtils.RandomCardColor()),
                        Utils.GetShortUserName(NewPostActivity.this)
                );

                GivderContentDBHelper givderContentDBHelper = new GivderContentDBHelper(NewPostActivity.this);
                givderContentDBHelper.open();
                givderContentDBHelper.insertEntry(userNetworkAction);
                givderContentDBHelper.close();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GivderContentHelper.AddContent(NewPostActivity.this,userNetworkAction);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Intent intent = new Intent();
                intent.putExtra(App.Content,new Gson().toJson(userNetworkAction));
                setResult(0,intent);
                finish();
            }

            @Override
            public void onError(VerificationError verificationError) {

            }

            @Override
            public void onStepSelected(int newStepPosition) {
/*
                if(newStepPosition==1){
                    phoneNumber =signUpFragment.getPhoneNumber();
                    otpFragment.setPhonberNumber(phoneNumber);
                    Utils.SaveUserName(SetupActivity.this,phoneNumber);
                    sendOtp();
                }
                else if(newStepPosition==2){
                    otp = otpFragment.getOpt();
                    matchOtp();
                }*/
            }

            @Override
            public void onReturn() {

            }
        });
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));
    }

    @Override
    public void permissionGranted() {
        init();
    }

    @Override
    public void permissionRefused() {
finish();
    }


    public  class StepperAdapter extends AbstractFragmentStepAdapter {

        public StepperAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override
        public Step createStep(int position) {
            if(position==0){
                if(newPostPlatesDateDescriptionFragment==null){
                    newPostPlatesDateDescriptionFragment = new NewPostPlatesDateDescriptionFragment();
                }
                return newPostPlatesDateDescriptionFragment;
            }
            else if(position==1){
                if(locationSelectionFragment==null)
                    locationSelectionFragment =new LocationSelectionFragment();
                return locationSelectionFragment;
            }
            else if(position==2){
                if(imagesFragment==null)
                    imagesFragment =new ImagesFragment();
                return imagesFragment;
            }

            else if(position==3){
                if(cameraFragment==null)
                    cameraFragment =new CameraFragment();
                return cameraFragment;
            }

            // return signUpFragment;

            return null;
        }
        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            StepViewModel.Builder builder = new StepViewModel.Builder(context);
            /*switch (position) {
                case 1:
                    builder.setEndButtonLabel("");
                    break;
                case 3:
                    builder.setEndButtonLabel("Grant");
                    break;
                case 10:
                    builder.setEndButtonLabel("");
                    break;
            }*/
            return builder.create();
        }

        @Override
        public int getCount() {
            return 2;
        }


    }

    public void moveToNext(){

        mStepperLayout.setCurrentStepPosition(mStepperLayout.getCurrentStepPosition()+1);
    }

    public void moveToPrevious(){

        mStepperLayout.setCurrentStepPosition(mStepperLayout.getCurrentStepPosition()-1);
    }
}
