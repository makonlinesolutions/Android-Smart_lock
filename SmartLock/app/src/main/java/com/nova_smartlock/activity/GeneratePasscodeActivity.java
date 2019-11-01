package com.nova_smartlock.activity;

import android.app.Dialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nova_smartlock.R;

public class GeneratePasscodeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_passcode);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = new Dialog(GeneratePasscodeActivity.this);

        viewPager=findViewById(R.id.pager);
        tabLayout=findViewById(R.id.tablayout);

        Pager_GeneratePasscode adapter=new Pager_GeneratePasscode(getSupportFragmentManager(),3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (viewPager != null) {
            viewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
