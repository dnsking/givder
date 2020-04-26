package com.niza.app.givder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asdev.phoneedittext.PhoneEditText;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.niza.app.givder.App;
import com.niza.app.givder.R;
import com.niza.app.givder.Utils.NetUnits;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class SignUpFragment extends Fragment implements Step {

    private PhoneEditText phoneNumberEditText;
    private PhoneNumberUtil mPhoneUtil = PhoneNumberUtil.getInstance();
    private String phoneNumber;
    private TextInputEditText userNameEditText;

    private Phonenumber.PhoneNumber parsePhoneNumber(String number) throws NumberParseException {

        TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        App.Log("number "+number);
        return mPhoneUtil.parseAndKeepRawInput(number, countryCodeValue);
    }
    public String getUserName(){
        return userNameEditText.getText().toString();
    }
    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(userNameEditText.getText()==null){

            return new VerificationError("Set User Name");
        }
        else {

            try{

                String number = phoneNumberEditText.getText().toString().replaceAll(" ","");

                if(!number.startsWith("+")&&!number.startsWith(phoneNumberEditText.getDIAL_CODE()))
                    number = phoneNumberEditText.getDIAL_CODE()+phoneNumberEditText.getText().toString();
                number = number.replaceAll(" ","");
                phoneNumber = number;
                try {
                    if(mPhoneUtil.isValidNumber(parsePhoneNumber(number))&& NetUnits.IsNetworkAvailable(getActivity())){

                        return null;
                    }
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }

            /*if(NetUnits.IsNetworkAvailable(getActivity())){
                TelephonyManager tm = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                String countryCodeValue = tm.getNetworkCountryIso();
                if(countryCodeValue==null||countryCodeValue.length()==0)
                    return null;
            }*/
                if(NetUnits.IsNetworkAvailable(getActivity()))
                    return null;
                    //  return new VerificationError("Phone Number Not Valid");
                else
                    return new VerificationError("No Internet Connection Available");
            }
            catch (Exception ex){

                return new VerificationError("Enter Phone Number");
            }
        }
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
        userNameEditText = view.findViewById(R.id.userNameEditText);
        return view;
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Snackbar.make(phoneNumberEditText,error.getErrorMessage(),Snackbar.LENGTH_SHORT).show();

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
