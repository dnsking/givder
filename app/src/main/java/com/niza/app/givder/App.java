package com.niza.app.givder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;


import java.io.IOException;

import okhttp3.Response;
import pl.tajchert.nammu.Nammu;


public class App extends Application {



    public static  final String PhoneNumber = "PhoneNumber";
    public static  final String UserName = "UserName";
    public static  final String AccountType = "com.niza.app.givder";
    public static  final String TokenType = "givdertoken";


    public static  final String NewUser = "NewUser";


    public static  final String IncomingCallReceived = "IncomingCallReceived";
    public static  final String IncomingCallAnswered = "IncomingCallAnswered";
    public static  final String IncomingCallEnded = "IncomingCallEnded";
    public static  final String OutgoingCallStarted = "OutgoingCallStarted";
    public static  final String OutgoingCallEnded = "OutgoingCallEnded";


    public static  final String FileUpload = "FileUpload";
    public static  final String CompleteSetup = "CompleteSetup";


    public static  final String SignInUrl = "https://erqhbjxqwg.execute-api.us-east-1.amazonaws.com/givderstage/signin";



    public static  final String Prefs = "com.app.givder.prefs";


    public static boolean IsDebug = true;
    private static final String TAG = "Givder Client";
    public static final String Content = "Content";
    public static final String ContentSecondary = "ContentSecondary";
    public static final String Default = "allOtherUsers";
    public static final String DefaultAudio = "allOtherUsersAudio";
    public static final String Audio = "Audio";
    public static void Log(String msg){
        if(App.IsDebug){
            int maxLogSize = 1000;
            if(msg.length()>maxLogSize)
                for(int i = 0; i <= msg.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i+1) * maxLogSize;
                    end = end > msg.length() ? msg.length() : end;
                    Log.i(TAG, msg.substring(start, end));
                }
            else
                Log.i(TAG, msg);
        }
    }

    public static  final String User = "user";
    public static  final String Password = "Password";
    public static  final String FirstRun = "FirstRun";
    public class Urls{
        public static final String Content = "https://erqhbjxqwg.execute-api.us-east-1.amazonaws.com/givderstage/content";
    }

    public static  Activity Activity;
    public static String AccountType_Helper ="AccountType_Helper";
    public static String AccountType_Giver ="AccountType_Giver";

    public static String MessageType_Request = "MessageType_Request";
    public static String MessageType_Text = "MessageType_Text";

    @Override
    public void onCreate() {
        super.onCreate();

        Nammu.init(this);

    }

}
