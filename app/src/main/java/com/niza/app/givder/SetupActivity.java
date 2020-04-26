package com.niza.app.givder;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.niza.app.givder.Utils.NetUnits;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.data.user.Authentication;
import com.niza.app.givder.fragments.AccountTypeFragment;
import com.niza.app.givder.fragments.OtpFragment;
import com.niza.app.givder.fragments.OtpVerificationFragment;
import com.niza.app.givder.fragments.SignUpFragment;
import com.niza.app.givder.networking.authentication.GivderAccountHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.io.IOException;

public class SetupActivity extends AppCompatActivity {

    private StepperLayout mStepperLayout;
    private SignUpFragment signUpFragment;
    private OtpFragment otpFragment;
    private AccountTypeFragment accountTypeFragment;


    private String phoneNumber;
    private String otp;
    private AccountManager accountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        accountManager = AccountManager.get(getBaseContext());

        setContentView(R.layout.activity_setup);


        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setListener(new StepperLayout.StepperListener() {
            @Override
            public void onCompleted(View completeButton) {
                finishSetup();
            }

            @Override
            public void onError(VerificationError verificationError) {

            }

            @Override
            public void onStepSelected(int newStepPosition) {

                if(newStepPosition==1){
                    phoneNumber =signUpFragment.getPhoneNumber();
                    otpFragment.setPhonberNumber(phoneNumber);
                    Utils.SaveUserName(SetupActivity.this,phoneNumber);
                    Utils.SaveShortUserName(SetupActivity.this,
                            signUpFragment.getUserName());
                    sendOtp();
                }
                else if(newStepPosition==2){
                    otp = otpFragment.getOpt();
                    matchOtp();
                }
            }

            @Override
            public void onReturn() {

            }
        });
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));

    }


    public void sendOtp(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    GivderAccountHelper.SignInSignUpToken(phoneNumber);
                    App.Log("logIn succesful");
                } catch (IOException e) {
                    e.printStackTrace();
                    App.Log("logIn failed "+e.getMessage());
                }
            }
        }).start();
    }

    public  class StepperAdapter extends AbstractFragmentStepAdapter {

        public StepperAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override
        public Step createStep(int position) {
            if(position==0){
                if(signUpFragment==null){
                    signUpFragment = new SignUpFragment();
                }
                return signUpFragment;
            }
                else if(position==1){
                    if(otpFragment==null)
                        otpFragment =new OtpFragment();
                    return otpFragment;
                }
                else if(position==2){
                return new OtpVerificationFragment();
            }else if(position==3){
                if(accountTypeFragment==null)
                    accountTypeFragment =new AccountTypeFragment();
                return accountTypeFragment;
            }
               // return signUpFragment;

            return null;
        }
        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            StepViewModel.Builder builder = new StepViewModel.Builder(context);
            switch (position) {
                case 1:
                    builder.setEndButtonLabel("");
                    break;
            }
            return builder.create();
        }

        @Override
        public int getCount() {
            return 4;
        }


    }
private void finishSetup(){
    Utils.IsFirstRun(SetupActivity.this,false);
        Utils.SaveAccountType(this,accountTypeFragment.accountType());
    Intent intent = new Intent(SetupActivity.this, MainActivity.class);
    startActivity(intent);
    overridePendingTransition(R.anim.slide_out,
            R.anim.slide_in);
    finish();
}
    public void moveToNext(){

        mStepperLayout.setCurrentStepPosition(mStepperLayout.getCurrentStepPosition()+1);
    }

    public void moveToPrevious(){

        mStepperLayout.setCurrentStepPosition(mStepperLayout.getCurrentStepPosition()-1);
    }
    public void matchOtp(){


        if(NetUnits.IsNetworkAvailable(this)){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        final Authentication authentication = GivderAccountHelper.MatchKey(SetupActivity.this,phoneNumber,phoneNumber,otp);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if(authentication.getAccessToken()!=null){
                                    String authToken = new Gson().toJson(authentication);


                                    final Account account = new Account(phoneNumber, App.AccountType);

                                    String tokenType = App.TokenType;
                                    App.Log("Save authToken "+authToken);

                                    accountManager.addAccountExplicitly(account, phoneNumber, null);
                                    accountManager.setAuthToken(account, tokenType, authToken);

                                    moveToNext();

                                }
                                else{
                                    moveToPrevious();

                                    Snackbar.make(mStepperLayout,"Code Does Not Match",Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else{
            Snackbar.make(mStepperLayout,"No Internet Connection Available",Snackbar.LENGTH_SHORT).show();
            moveToPrevious();
        }


    }
}
