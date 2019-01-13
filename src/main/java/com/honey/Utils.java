package com.honey;

public class Utils {
    private Utils(){}

    public static <T> T checkNonNull(T var0, String var1) {
        if (var0 == null) {
            error(var1);
        }

        return var0;
    }


    public static void error(String message) {
        throw new AssertionError(message);
    }
}
