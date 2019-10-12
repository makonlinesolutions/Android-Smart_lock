package com.nova_smartlock.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.nova_smartlock.R;

public class FragmentTermsCondition extends Fragment {
    private WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_termsconditions, container, false);

        webView = view.findViewById(R.id.webview);

        //ToDo change link
        webView.loadUrl("https://www.google.com");
        return view;
    }
}
