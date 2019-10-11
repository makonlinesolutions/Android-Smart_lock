package com.nova_smartlock.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.nova_smartlock.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CommonUtils {
    private static Dialog dialog;

    public static Dialog showProgressDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.app.AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }
}
