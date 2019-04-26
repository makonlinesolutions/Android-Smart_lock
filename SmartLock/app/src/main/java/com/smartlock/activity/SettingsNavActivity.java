package com.smartlock.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.smartlock.R;

public class SettingsNavActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout rl_transfer_lock,rl_lockuser,rl_lockgroup,rl_gateway,rl_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_nav);
        toolbar = findViewById(R.id.toolbar);
        rl_transfer_lock=findViewById(R.id.rl_transfer_lock);
        rl_lockuser=findViewById(R.id.rl_lockuser);
        rl_lockgroup=findViewById(R.id.rl_lockgroup);
        rl_gateway=findViewById(R.id.rl_gateway);
        rl_about=findViewById(R.id.rl_about);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rl_transfer_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsNavActivity.this,SettingsTransferLocksActivity.class);
                startActivity(intent);
            }
        });

        rl_lockuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsNavActivity.this,SettingsLockUserActivity.class);
                startActivity(intent);
            }
        });

        rl_gateway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsNavActivity.this,SettingsGatewayActivity.class);
                startActivity(intent);
            }
        });

        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsNavActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

        rl_lockgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsNavActivity.this,SettingsLockGroupActivity.class);
                startActivity(intent);
            }
        });

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
}
