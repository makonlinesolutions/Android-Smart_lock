package com.nova_smartlock.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nova_smartlock.R;
import com.nova_smartlock.activity.LoginActivity;
import com.nova_smartlock.activity.NearbyLockActivity;
import com.nova_smartlock.activity.SplashScreenActivity;
import com.nova_smartlock.constant.Config;
import com.nova_smartlock.dao.DbService;
import com.nova_smartlock.sp.MyPreference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.nova_smartlock.app.SmartLockApp.mContext;
import static com.nova_smartlock.utils.Const.KEY_VALUE;
import static com.nova_smartlock.utils.Const.USER_KEY_VALUE;

/**
 * dp、sp convert to px
 */
public class DisplayUtil {
    private static int sScreenWidth;
    private static int sScreenHeight;

    /**
     * convert px value to dp
     *
     * @param pxValue
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert dp value to px
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * convert px value to sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * convert sp value to px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * get the width of screen
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
        sScreenWidth = outMetrics.widthPixels;
        return sScreenWidth;
    }

    /**
     * get the height of screen
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
        sScreenHeight = outMetrics.heightPixels;
        return sScreenHeight;
    }

    /**
     * get the height of status bar
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private static long lastClickTime;

    public static void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_unlock_dialog, null, false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        TextView textView = view.findViewById(R.id.tv_unlock);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showMessageDialog(final Context context, final String message, Drawable drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_unlock_dialog, null, false);
        builder.setView(view);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
        final Dialog dialog = builder.create();
        TextView textView = view.findViewById(R.id.tv_unlock);
        TextView tv_message = view.findViewById(R.id.tv_message);
        TextView tv_time = view.findViewById(R.id.tv_time);
        ImageView imageView = view.findViewById(R.id.iv_lock);
        if (message.equals("Door unlocked successfully!")) {
            tv_time.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                MediaPlayerNotification.SoundPlayer(context);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM, hh:mm a");
                LocalDateTime now = LocalDateTime.now();
                tv_time.setText(dtf.format(now));
            } else {
//                tv_time.setText(dtf.forma);
            }
        }
        tv_message.setText(message);
        imageView.setBackground(drawable);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.contains("Disconnected")) {
                    context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else if (message.equals("Please select the key")) {
                    context.startActivity(new Intent(context, NearbyLockActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else if (message.contains("Access Denied") || message.equals("You have already checked-out")) {
                    DbService.deleteAllKey();
//                    Toast.makeText(mContext, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    MyPreference.putStr(mContext, MyPreference.ACCESS_TOKEN, "");
                    MyPreference.putStr(mContext, MyPreference.OPEN_ID, "");
                    SharePreferenceUtility.saveBooleanPreferences(context, Config.IS_ADMIN_LOGIN, false);
                    SharePreferenceUtility.saveObjectPreferences(context, KEY_VALUE, null);
                    SharePreferenceUtility.saveObjectPreferences(context, USER_KEY_VALUE, null);
                    SharePreferenceUtility.saveBooleanPreferences(context, Const.IS_LOGIN, false);
                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }else {
                    dialog.dismiss();
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
