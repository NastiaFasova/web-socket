package com.example.kpi.socialnetwork.util;

import java.util.Random;

public class RandomStringGenerator {
    private static String _alph = "qwertyuiopasdfghjklzxcvbnm";

    public static String generate(int length)
    {
        var rnd = new Random();
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            sb.append(_alph.charAt(rnd.nextInt(_alph.length())));
        }
        return sb.toString();
    }
}
