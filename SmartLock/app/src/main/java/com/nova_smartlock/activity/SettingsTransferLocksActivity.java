package com.nova_smartlock.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nova_smartlock.R;

import java.util.ArrayList;

public class SettingsTransferLocksActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Transferlock_model> transferlock_list_models=new ArrayList<Transferlock_model>();

    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_transfer_locks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recylerview);
        btn_next=findViewById(R.id.btn_next);

        adapter=new TransferLockAdapter(SettingsTransferLocksActivity.this,transferlock_list_models);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        Transferlock_model transferlock_model=new Transferlock_model();
        transferlock_model.setLock_name("Testing");
        transferlock_list_models.add(transferlock_model);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsTransferLocksActivity.this, RecepientActivity.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
