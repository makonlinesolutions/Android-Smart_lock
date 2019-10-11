package com.nova_smartlock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nova_smartlock.R;
import com.nova_smartlock.model.RecordListItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {
    private Context mContext;
    private List<RecordListItems> arrRecordListItems;
    private LayoutInflater mLayoutInflater;

    public RecordListAdapter(Context mContext, List<RecordListItems> arrRecordListItems) {
        this.mContext = mContext;
        this.arrRecordListItems = arrRecordListItems;
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.item_lock_checkin_record, viewGroup, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder recordViewHolder, int position) {

        // Creating date format
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SS");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(arrRecordListItems.get(position).getLockDate());

        // Formatting Date according to the
        // given format
//        System.out.println(simple.format(result));

        recordViewHolder.record.setText("Unlock time: " + String.valueOf(simple.format(result)));
    }

    @Override
    public int getItemCount() {
        return arrRecordListItems.size();
    }


    public class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView record;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record = itemView.findViewById(R.id.tvUnlockTime);
        }
    }
}
