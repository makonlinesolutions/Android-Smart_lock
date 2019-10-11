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

public class Records_Adapter extends RecyclerView.Adapter<Records_Adapter.Records_holder>{
    Activity activity;
    ArrayList<Records_list_model> records_list_models;
    public Records_Adapter(Activity activity, ArrayList<Records_list_model> records_list_models) {
        this.activity = activity;
        this.records_list_models = records_list_models;
    }

    @NonNull
    @Override
    public Records_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_records,viewGroup,false);
        Records_holder records_holder=new Records_holder(view);
        return records_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Records_holder records_holder, int i) {
        records_holder.tv_time.setText(records_list_models.get(i).getTime());
        records_holder.tv_date.setText(records_list_models.get(i).getDate());
        records_holder.tv_status.setText(records_list_models.get(i).getStaus());
        records_holder.tv_username.setText(records_list_models.get(i).getUser_name());

    }

    @Override
    public int getItemCount() {
        return records_list_models.size();
    }

    public static class Records_holder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_time,tv_username,tv_status;

       public Records_holder(@NonNull View itemView) {
           super(itemView);
           tv_date=itemView.findViewById(R.id.tv_date);
           tv_time=itemView.findViewById(R.id.tv_time);
           tv_username=itemView.findViewById(R.id.tv_user_name);
           tv_status=itemView.findViewById(R.id.tv_status);
       }
   }
}
