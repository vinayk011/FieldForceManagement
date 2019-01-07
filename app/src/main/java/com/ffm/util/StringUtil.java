package com.ffm.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.Nullable;


/**
 * Created by Raviteja on 19-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class StringUtil {
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static int getLength(String s) {
        return isEmpty(s) ? 0 : s.length();
    }

    public static boolean checkLength(String s, int l) {
        return !isEmpty(s) && s.length() == l;
    }

    public static boolean checkLengthGreater(String s, int l) {
        return !isEmpty(s) && s.length() > l;
    }

    public static boolean isNull(String contactName) {
        return contactName == null || contactName.isEmpty() || "null".equals(contactName);
    }

    public static boolean validateForSummary(String s) {
        if (isEmpty(s)) {
            return false;
        } else if ("-".equals(s)) {
            return false;
        }
        return true;
    }

    public static String fixString(String s) {
        if (isEmpty(s)) {
            return "";
        }
        return s.trim();
    }

    public static boolean isOnlyNumbers(String s) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static String mobileNumberValidator(String s) {
        if (isEmpty(s)) {
            return fixString(s);
        }
        if (s.length() > 3) {
            return s.substring(0, 4).replaceFirst("\\+", "").replaceFirst("9", "").replaceFirst("1", "");
        }
        return s;
    }

    public static boolean validateEmail(String s) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(fixString(s)).matches();
    }

    public static boolean isEquals(String s1, String s2, boolean ignoreCase) {
        return ignoreCase ? fixString(s1).equalsIgnoreCase(fixString(s2)) : fixString(s1).equals(fixString(s2));
    }

    public static long convertToLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception ignored) {
        }
        return 0;
    }

    public static String multilineString(String... lines) {
        StringBuilder sb = new StringBuilder();
        for (String s : lines) {
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    public static int getValueAsInt(String value, int defaultValue) {
        int iValue = defaultValue;
        try {
            iValue = Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
        }
        return iValue;
    }

    public static int getValueAsInt(String value) {
        int iValue = 0;
        try {
            iValue = Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
        }
        return iValue;
    }

    public static String getFromSplit(String value, String splitWith, int position) {
        String[] str = value.split(splitWith);
        if (str.length > position) {
            return str[position];
        }
        return null;
    }

    public static String validString(String s) {
        if (s == null || "null".equals(s)) {
            return "";
        }
        return s;
    }

    public static String fixName(String s) {
        if (validString(s).toLowerCase().contains("hug")) {
            return s;
        }
        return "Hug Fit";
    }

    public static boolean startsWith(String s, String s1) {
        int s1Length = validString(s1).length();
        if (validString(s).length() >= s1Length) {
            String sSub = s.substring(0, s1Length);
            return sSub.toLowerCase().equals(validString(s1).toLowerCase());
        }
        return false;
    }

    public static String capitalizeFirstLetter(String text) {
        StringBuilder str = new StringBuilder();
        String[] tokens = text.split("\\s");// Can be space,comma or hyphen
        for (String token : tokens) {
            str.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1)).append(" ");
        }
        return str.toString().trim();
    }

    @Nullable
    public static List<Integer> splitToIntList(@Nullable String input) {
        if (input == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, ",");
        while (tokenizer.hasMoreElements()) {
            final String item = tokenizer.nextToken();
            try {
                result.add(Integer.parseInt(item));
            } catch (NumberFormatException ex) {
                Log.e("ROOM", "Malformed integer list", ex);
            }
        }
        return result;
    }

    @Nullable
    public static String joinIntoString(@Nullable List<Integer> input) {
        if (input == null) {
            return null;
        }

        final int size = input.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(Integer.toString(input.get(i)));
            if (i < size - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}