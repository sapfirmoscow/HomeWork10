package ru.sberbank.homework10.util;

import android.graphics.Color;

import java.util.Random;

public class ColorGenerator {

    private static final int MAX_ALPHA = 255;
    private static final int MIN_ALPHA = 0;
    private static Random sRandom = new Random();


    public static int generateRandomColor() {
        return Color.argb(MAX_ALPHA, sRandom.nextInt(255), sRandom.nextInt(255), sRandom.nextInt(255));
    }

}
