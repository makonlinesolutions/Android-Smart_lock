package com.nova_smartlock.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.nova_smartlock.R;
import com.nova_smartlock.model.AdminDataGetDetailsResponse;
import com.nova_smartlock.retrofit.ApiServiceProvider;
import com.nova_smartlock.retrofit.ApiServices;
import com.nova_smartlock.utils.Const;
import com.nova_smartlock.utils.SharePreferenceUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.nova_smartlock.app.SmartLockApp.mContext;
import static com.nova_smartlock.utils.Const.ADMIN_PASSWORD;
import static com.nova_smartlock.utils.Const.APP_ID;
import static com.nova_smartlock.utils.Const.SCRETE_KEY;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    private boolean ISLOGIN;
    Thread splashTread;
    private ApiServices services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        view = findViewById(android.R.id.content);
        services = new ApiServiceProvider(mContext).apiServices;
        ISLOGIN = (boolean) SharePreferenceUtility.getPreferences(this, Const.IS_LOGIN, SharePreferenceUtility.PREFTYPE_BOOLEAN);
        if (checkPermission()) {
            if (ISLOGIN) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }, 1000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }, 1000);
                getAdminDetails();
            }
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        if (ISLOGIN) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();
                                }
                            }, 1000);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();
                                }
                            }, 1000);
                        }
                    } else {
                        Snackbar.make(view, "Permission Denied, You cannot access location permission .", Snackbar.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                                showMessageOKCancel("You need to allow access the location permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashScreenActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }
    /**
     * <b>getAdminDetails(String myName) </b>
     * <p>This methos is used for get the admin details from server</p>
     * Developed by: Abhay Dasondhi
     * Date: 02:11:2019
     */
    private void getAdminDetails() {
        Call<AdminDataGetDetailsResponse> adminDataGetDetailsResponseCall = services.ADMIN_DATA_GET_DETAILS_RESPONSE_CALL();
        adminDataGetDetailsResponseCall.enqueue(new Callback<AdminDataGetDetailsResponse>() {
            @Override
            public void onResponse(Call<AdminDataGetDetailsResponse> call, Response<AdminDataGetDetailsResponse> response) {
                if (response.isSuccessful()) {
                    Log.i("admin_login",response.message());
                    if (response.body().response.statusCode == 200) {
                        SharePreferenceUtility.saveStringPreferences(mContext, Const.ADMIN_LOGIN, response.body().response.details.userName);
                        SharePreferenceUtility.saveStringPreferences(mContext, Const.ADMIN_PASSWORD, response.body().response.details.password);
                        SharePreferenceUtility.saveStringPreferences(mContext, Const.APP_ID, response.body().response.details.lockId);
                        SharePreferenceUtility.saveStringPreferences(mContext, Const.SCRETE_KEY, response.body().response.details.secretKey);
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<AdminDataGetDetailsResponse> call, Throwable t) {
                Log.d("Splash Screen", t.getMessage());
            }
        });
    }
}
