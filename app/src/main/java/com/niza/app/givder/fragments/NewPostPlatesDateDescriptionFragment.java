package com.niza.app.givder.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.niza.app.givder.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostPlatesDateDescriptionFragment extends Fragment implements Step {


    private TextInputEditText numberOfPlatesEditText,
            postExpirationEditText,descriptionEditText;
    public NewPostPlatesDateDescriptionFragment() {
        // Required empty public constructor
    }

    public String getNumberOfPlates(){
        return numberOfPlatesEditText.getText().toString();
    }
    public String getPostExpirationInHours(){
        return postExpirationEditText.getText().toString();
    }
    public String getDescription(){
        return descriptionEditText.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_new_post_plates_date_description, container, false);
        numberOfPlatesEditText = view.findViewById(R.id.numberOfPlatesEditText);
        postExpirationEditText = view.findViewById(R.id.postExpirationEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        if(numberOfPlatesEditText.getText()==null)
        return new VerificationError("Enter Number Of Plates");
        else if(postExpirationEditText.getText()==null)
            return new VerificationError("Enter Expiration Time");
        else if(descriptionEditText.getText()==null)
            return new VerificationError("Enter Description");


        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

        Snackbar.make(getView(),error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();

    }
}
