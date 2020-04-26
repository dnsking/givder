package com.niza.app.givder.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.niza.app.givder.R;
import com.niza.app.givder.SetupActivity;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class OtpFragment extends Fragment implements Step {


    private String phonberNumber;
    private String opt;
    private TextView fourDigitCodeTxtView;
    private OtpView otp_view;
    private ExtendedFloatingActionButton redo;

    public OtpFragment() {
        // Required empty public constructor
    }

    public static OtpFragment newInstance() {
        OtpFragment fragment = new OtpFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {
                OtpFragment.this. opt = otp;
                ((SetupActivity) getActivity()).moveToNext();
            }
        });
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_otp, container, false);
        otp_view = view.findViewById(R.id.otp_view);

        fourDigitCodeTxtView = view.findViewById(R.id.fourDigitCodeTxtView);
        redo = view.findViewById(R.id.redo);
        return view;
    }

    public String getPhonberNumber() {
        return phonberNumber;
    }

    public void setPhonberNumber(String phonberNumber) {
        this.phonberNumber = phonberNumber;
        fourDigitCodeTxtView.setText("A verification code was sent to "+phonberNumber);
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(opt==null)
            return new VerificationError("Enter Four Digit Code");
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

        Snackbar.make(otp_view,error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }
}
