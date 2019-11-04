package com.nova_smartlock.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.nova_smartlock.R;
import com.nova_smartlock.model.TermConditionResponse;
import com.nova_smartlock.model.UnlockKeyNameResponse;
import com.nova_smartlock.retrofit.ApiServiceProvider;
import com.nova_smartlock.retrofit.ApiServices;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nova_smartlock.app.SmartLockApp.mContext;

public class FragmentTermsCondition extends Fragment {
    private WebView webView;
    private ApiServices services;
    private String html_codee;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_termsconditions, container, false);

        webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        services = new ApiServiceProvider(mContext).apiServices;
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        if (NetworkUtils.isNetworkConnected(getContext())) {
            Call<TermConditionResponse> unlockKeyNameResponseCall = services.TERM_CONDITION_RESPONSE_CALL();
            unlockKeyNameResponseCall.enqueue(new Callback<TermConditionResponse>() {
                @Override
                public void onResponse(Call<TermConditionResponse> call, Response<TermConditionResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().response.statusCode==200) {
                            html_codee = response.body().response.messageBody;
                            webView.loadDataWithBaseURL("", html_codee, mimeType, encoding, "");
                        }else {
                            // do something for not success
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        // do something for not success
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<TermConditionResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Fragment_home.getInstance().showMessageDialog("Please check Mobile network connection", getActivity().getDrawable(R.drawable.ic_no_internet));
        }

        //ToDo change link
        return view;
    }
}
