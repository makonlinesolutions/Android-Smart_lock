package com.nova_smartlock.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nova_smartlock.R;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {
    Toolbar toolbar;
    ArrayList<Message_list_model> list_model=new ArrayList<Message_list_model>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        toolbar = findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recylerview);
        adapter=new Message_Adapter(MessagesActivity.this,list_model);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        Message_list_model message_list_model=new Message_list_model();
        message_list_model.setMessage("Your Account has been used to Login from new device");
        message_list_model.setMessage_title("System Message");
        list_model.add(message_list_model);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

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
