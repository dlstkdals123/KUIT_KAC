package org.example.kuit_kac.global.util;

public class Validator {
    public static boolean isStringNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isStringEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
