package com.niza.app.givder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.niza.app.givder.networking.authentication.GivderAccountAuthenticator;

public class GivderAccountService extends Service {
    public GivderAccountService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        GivderAccountAuthenticator authenticator = new GivderAccountAuthenticator(this);
        return authenticator.getIBinder();}
}
