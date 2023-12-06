package com.esprow.interview.sklroman.stockmarket.tool;

public class Flags {

    private static boolean dev = false;

    public static boolean isDev() {
        return dev;
    }

    public static void setDevTrue() {
        Flags.dev = true;
    }
}
