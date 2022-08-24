package com.example.kpi.socialnetwork.util;

import java.util.Random;

/**
 * Generator of random strings for saving files
 * */
public class RandomStringGenerator {
    private static String alph = "qwertyuiopasdfghjklzxcvbnm";

    public static String generate(int length) {
        var rnd = new Random();
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alph.charAt(rnd.nextInt(alph.length())));
        }
        return sb.toString();
    }
}
