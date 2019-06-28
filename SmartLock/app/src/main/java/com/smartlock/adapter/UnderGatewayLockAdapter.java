package com.smartlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartlock.R;
import com.smartlock.activity.BaseActivity;
import com.smartlock.databinding.ItemUnderGatewayLockBinding;
import com.smartlock.model.UnderGatewayLock;
import com.smartlock.net.ResponseService;
import com.smartlock.utils.DateUitl;
import com.ttlock.bl.sdk.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnderGatewayLockAdapter extends RecyclerView.Adapter<UnderGatewayLockAdapter.GatewayHolder> {

    private Context mContext;
    private ArrayList<UnderGatewayLock> underGatewayLocks;

    public static class GatewayHolder extends RecyclerView.ViewHolder {

        public ItemUnderGatewayLockBinding binding;

        public GatewayHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(UnderGatewayLock underGatewayLock) {
            binding.setUnderGatewayLock(underGatewayLock);
        }
    }

    public UnderGatewayLockAdapter(Context context, ArrayList<UnderGatewayLock> underGatewayLocks) {
        this.mContext = context;
        this.underGatewayLocks = underGatewayLocks;
    }

    @Override
    public GatewayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_under_gateway_lock, parent, false);
        return new GatewayHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GatewayHolder holder, int position) {
        final UnderGatewayLock underGatewayLock = underGatewayLocks.get(position);
        holder.bind(underGatewayLock);
        holder.binding.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {

                    @SuppressLint("NewApi")
                    @Override
                    protected void onPostExecute(String json) {
                        super.onPostExecute(json);
                        ((BaseActivity)mContext).cancelProgressDialog();
                        LogUtil.d("json:" + json, true);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String msg;
                            if(jsonObject.has("errcode")) {
                                msg = "Error in resetting time!"; //jsonObject.getString("errmsg");
                                ((BaseActivity) mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                            } else {
                                long date = jsonObject.getLong("date");
                                msg = "Lock time reset to " + DateUitl.getTime(date, "yyyy-MM-dd HH:mm");
                                ((BaseActivity) mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            }
                            //((BaseActivity)mContext).toast(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        ((BaseActivity)mContext).showProgressDialog();
                        return ResponseService.updateLockDate(underGatewayLock.getLockId());
                    }
                }.execute();
            }
        });
        holder.binding.lockTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {

                    @SuppressLint("NewApi")
                    @Override
                    protected void onPostExecute(String json) {
                        super.onPostExecute(json);
                        ((BaseActivity)mContext).cancelProgressDialog();
                        LogUtil.d("json:" + json, true);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String msg;
                            if(jsonObject.has("errcode")) {
                                msg = "Error in setting lock time!"; //jsonObject.getString("errmsg");
                                ((BaseActivity) mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                            } else {
                                long date = jsonObject.getLong("date");
                                msg = "Lock time set to " + DateUitl.getTime(date, "yyyy-MM-dd HH:mm");
                                ((BaseActivity) mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            }
                            //((BaseActivity)mContext).toast(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        ((BaseActivity)mContext).showProgressDialog();
                        return ResponseService.queryLockDate(underGatewayLock.getLockId());
                    }
                }.execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return underGatewayLocks.size();
    }

}
