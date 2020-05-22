package com.example.bebeauty.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static void setHidingMenuOnClick(View view, Activity activity) {
        view.setOnTouchListener((v, event) -> {
            hideKeyboard(activity);
            return false;
        });
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if (imm != null && currentFocus != null)
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
