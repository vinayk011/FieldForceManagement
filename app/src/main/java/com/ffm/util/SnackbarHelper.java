package com.ffm.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ffm.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

public class SnackbarHelper {

    public static void configSnackbar(Context context, Snackbar snack) {
        //addMargins(snack);
        setToastPosition(snack);
        //setRoundBordersBg(context, snack);
        customizeTextView(context, snack);
        ViewCompat.setElevation(snack.getView(), 6f);
    }

    private static void customizeTextView(Context context, Snackbar snack) {
        TextView textView = snack.getView().findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText));
        textView.setMaxLines(5);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    private static void setToastPosition(Snackbar snack) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snack.getView().getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        snack.getView().setLayoutParams(params);
    }

    private static void addMargins(Snackbar snack) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snack.getView().getLayoutParams();
        params.setMargins(12, 12, 12, 12);
        snack.getView().setLayoutParams(params);
    }

    private static void setRoundBordersBg(Context context, Snackbar snackbar) {
        snackbar.getView().setBackground(context.getDrawable(R.drawable.bg_snackbar));
    }
}