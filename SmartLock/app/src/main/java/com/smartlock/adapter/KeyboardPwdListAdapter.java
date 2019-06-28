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
import com.smartlock.app.SmartLockApp;
import com.smartlock.databinding.ItemKeyboardPwdBinding;
import com.smartlock.enumtype.Operation;
import com.smartlock.model.KeyboardPwd;
import com.smartlock.myInterface.OperateCallback;
import com.smartlock.net.ResponseService;
import com.ttlock.bl.sdk.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KeyboardPwdListAdapter extends RecyclerView.Adapter<KeyboardPwdListAdapter.KeyboardPwdHolder> {

    private Context mContext;
    private ArrayList<KeyboardPwd> keyboardPwds;
    private String lockmac;

    public static class KeyboardPwdHolder extends RecyclerView.ViewHolder {

        public ItemKeyboardPwdBinding binding;

        public KeyboardPwdHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(KeyboardPwd KeyboardPwd) {
            binding.setKeyboardPwd(KeyboardPwd);
        }
    }

    public KeyboardPwdListAdapter(Context context, ArrayList<KeyboardPwd> keyboardPwds, String lockmac) {
        this.mContext = context;
        this.keyboardPwds = keyboardPwds;
        this.lockmac = lockmac;
    }

    @Override
    public KeyboardPwdHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keyboard_pwd, parent, false);
        return new KeyboardPwdHolder(itemView);
    }

    //ToDo confirm messages
    @Override
    public void onBindViewHolder(KeyboardPwdHolder holder, final int position) {
        final KeyboardPwd keyboardPwd = keyboardPwds.get(position);
        holder.bind(keyboardPwd);
        holder.binding.deleteByGateway.setOnClickListener(new View.OnClickListener() {
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
                            int errcode = jsonObject.getInt("errcode");
                            String msg;
                            if(errcode != 0) {
                                msg = "Error in deleting passcode by gateway!";//jsonObject.getString("errmsg");
                                ((BaseActivity)mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                            } else {
                                msg = "Deleted passcode by gateway";
                                keyboardPwds.remove(position);
                                notifyDataSetChanged();
                                ((BaseActivity)mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        ((BaseActivity)mContext).showProgressDialog();
                        return ResponseService.deleteKeyboardPwd(keyboardPwd.getLockId(), keyboardPwd.getKeyboardPwdId(), 2);
                    }
                }.execute();
            }
        });
        holder.binding.deleteByBle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmartLockApp.operateCallback = new OperateCallback() {
                    @Override
                    public void onSuccess() {
                        new AsyncTask<Void, Void, String>() {

                            @SuppressLint("NewApi")
                            @Override
                            protected void onPostExecute(String json) {
                                super.onPostExecute(json);
                                ((BaseActivity)mContext).cancelProgressDialog();
                                LogUtil.d("json:" + json, true);
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    int errcode = jsonObject.getInt("errcode");
                                    String msg;
                                    if(errcode != 0) {
                                        msg = "Error in deleting passcode by server!"; //jsonObject.getString("errmsg");
                                        ((BaseActivity)mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_143_attention_183267));
                                    } else {
                                        msg = "Deleted passcode by server";
                                        keyboardPwds.remove(position);
                                        notifyDataSetChanged();
                                        ((BaseActivity)mContext).showMessageDialog(msg, mContext.getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                ((BaseActivity)mContext).showProgressDialog();
                                return ResponseService.deleteKeyboardPwd(keyboardPwd.getLockId(), keyboardPwd.getKeyboardPwdId(), 1);
                            }
                        }.execute();
                    }

                    @Override
                    public void onFailed() {

                    }
                };

                SmartLockApp.mTTLockAPI.connect(lockmac);
                SmartLockApp.bleSession.setPassword(keyboardPwd.getKeyboardPwd());
                SmartLockApp.bleSession.setOperation(Operation.DELETE_ONE_KEYBOARDPASSWORD);
                SmartLockApp.bleSession.setLockmac(lockmac);

            }
        });
    }

    @Override
    public int getItemCount() {
        return keyboardPwds.size();
    }

}
