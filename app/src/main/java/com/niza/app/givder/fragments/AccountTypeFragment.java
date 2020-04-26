package com.niza.app.givder.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.niza.app.givder.App;
import com.niza.app.givder.R;
import com.niza.app.givder.Utils.GRadioGroup;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountTypeFragment extends Fragment implements Step {


    private MaterialRadioButton selectHelper,selectGiver;
    public AccountTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_type, container, false);
        selectHelper = view.findViewById(R.id.selectHelper);
        selectGiver = view.findViewById(R.id.selectGiver);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // new GRadioGroup(selectHelper,selectGiver);

        selectHelper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectHelper.setChecked(true);
                selectGiver.setChecked(false);
            }
        });

        selectGiver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectGiver.setChecked(true);
                selectHelper.setChecked(false);
            }
        });

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(!selectHelper.isChecked()&&!selectGiver.isChecked()){
            return new VerificationError("Select Account Type");
        }
        return null;
    }
    public String accountType(){
        return selectHelper.isChecked()? App.AccountType_Helper:App.AccountType_Giver;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

        Snackbar.make(getView(),error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

}
