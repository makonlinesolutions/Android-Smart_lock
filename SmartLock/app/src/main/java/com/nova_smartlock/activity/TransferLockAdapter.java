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

public class TransferLockAdapter extends RecyclerView.Adapter<TransferLockAdapter.TransferlockHolder> {
    Activity activity;
    ArrayList<Transferlock_model> transferlock_models;

    public TransferLockAdapter(Activity activity, ArrayList<Transferlock_model> transferlock_models) {
        this.activity = activity;
        this.transferlock_models = transferlock_models;
    }

    @NonNull
    @Override
    public TransferlockHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_transfer_lock,viewGroup,false);
        TransferlockHolder transferlockHolder=new TransferlockHolder(view);
        return transferlockHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransferlockHolder transferlockHolder, int i) {
            transferlockHolder.lock_name.setText(transferlock_models.get(i).getLock_name());
    }

    @Override
    public int getItemCount() {
        return transferlock_models.size();
    }

    public static class TransferlockHolder extends RecyclerView.ViewHolder {
        TextView lock_name;
        public TransferlockHolder(@NonNull View itemView) {
            super(itemView);
            lock_name=itemView.findViewById(R.id.tv_name_lock);
        }
    }
}
