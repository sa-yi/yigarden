package com.sayi.vdim.utils;

public class PasswordStrengthChecker {
    public static int checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0; // 极弱
        }

        int strengthPoints = 0;

        // Check for length
        if (password.length() >= 8) {
            strengthPoints++;
        }

        // Check for lower case characters
        if (isLowerCasePresent(password)) {
            strengthPoints++;
        }

        // Check for upper case characters
        if (isUpperCasePresent(password)) {
            strengthPoints++;
        }

        // Check for digits
        if (isDigitPresent(password)) {
            strengthPoints++;
        }

        // Check for special characters
        if (isSpecialCharacterPresent(password)) {
            strengthPoints++;
        }

        // Determine the strength based on the points
        return strengthPoints;
    }

    private static boolean isLowerCasePresent(String password) {
        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUpperCasePresent(String password) {
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDigitPresent(String password) {
        for (char ch : password.toCharArray()) {
            if (Character.isDigit(ch)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSpecialCharacterPresent(String password) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";
        for (char ch : password.toCharArray()) {
            if (specialChars.indexOf(ch) != -1) {
                return true;
            }
        }
        return false;
    }
}
