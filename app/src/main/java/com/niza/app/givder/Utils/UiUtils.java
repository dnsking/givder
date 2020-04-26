package com.niza.app.givder.Utils;

import android.graphics.Color;

import java.util.Random;

public class UiUtils {
    public final static int[] CardColors = new int[]{
            Color.parseColor("#F6D5D6")
            ,Color.parseColor("#DACAE5")
            ,Color.parseColor("#AEC4EB")
            ,Color.parseColor("#FFFDE0")
            ,Color.parseColor("#95CAF6")
    };
    public static int RandomCardColor(){
        Random Dice = new Random();
        int n = Dice.nextInt(CardColors.length);
        return CardColors[n];
    }

    public static int ManipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
