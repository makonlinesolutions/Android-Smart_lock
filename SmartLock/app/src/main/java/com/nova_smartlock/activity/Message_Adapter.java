package com.nova_smartlock.activity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nova_smartlock.R;

import java.util.ArrayList;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.MessageHolder>{
    Activity activity;
    ArrayList<Message_list_model> message_list_models;

    public Message_Adapter(Activity activity, ArrayList<Message_list_model> message_list_models) {
        this.activity = activity;
        this.message_list_models = message_list_models;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_message,viewGroup,false);
        MessageHolder messageHolder=new MessageHolder(view);
        return messageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
        messageHolder.message_title.setText(message_list_models.get(i).getMessage_title());
        messageHolder.message.setText(message_list_models.get(i).getMessage());

    }

    @Override
    public int getItemCount() {
        return message_list_models.size();
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {
        TextView message,message_title;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.tv_message);
            message_title=itemView.findViewById(R.id.tv_message_title);
        }
    }
}
