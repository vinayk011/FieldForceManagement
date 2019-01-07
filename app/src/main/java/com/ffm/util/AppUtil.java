package com.ffm.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import com.ffm.MainActivity;
import com.ffm.activity.HomeActivity;
import com.ffm.preference.AppPreference;

import java.util.ArrayList;
import java.util.List;


public class AppUtil {

    public static void dashboard(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void signOut(Context context) {
        cleanAppData();
        dashboard(context);
    }

    private static void cleanAppData() {
        //DataHandler.getInstance().clearData();
        AppPreference.getInstance().clear();
        //PaperDB.getInstance().destroy();
    }
}
