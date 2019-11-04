package com.nova_smartlock.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nova_smartlock.R;
import com.nova_smartlock.activity.FragmentTermsCondition;
import com.nova_smartlock.activity.Fragment_account;
import com.nova_smartlock.activity.Fragment_home;
import com.nova_smartlock.activity.MainActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.nova_smartlock.utils.Constants.AppConst.IS_FIRST_TIME_LOGIN;

public class CommonUtils {
    private static Dialog dialog;

    public static Dialog showProgressDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.app.AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        builder.setCancelable(false);
        /*builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    ((MainActivity)mContext).finish();
                return false;
            }
        });*/
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        LinearLayout ll_yes = dialog.findViewById(R.id.ll_yes);
        LinearLayout ll_no = dialog.findViewById(R.id.ll_no);

        final boolean is_first_time_login = (boolean) SharePreferenceUtility.getPreferences(mContext, IS_FIRST_TIME_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);

        ll_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (!is_first_time_login){
                    SharePreferenceUtility.saveBooleanPreferences(mContext, IS_FIRST_TIME_LOGIN, true);
                }
                //SharePreferenceUtility.saveBooleanPreferences(mContext, IS_FIRST_TIME_LOGIN, false);
            }
        });

        ll_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SharePreferenceUtility.saveBooleanPreferences(mContext, IS_FIRST_TIME_LOGIN, false);
            }
        });

        TextView tv_terms = dialog.findViewById(R.id.tv_label);
        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Fragment fragment = new FragmentTermsCondition();
                FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return dialog;
    }
}
