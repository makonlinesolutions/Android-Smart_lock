package com.nova_smartlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova_smartlock.R;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.model.PasscodeListItem;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.SharePreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.nova_smartlock.utils.Const.KEY_VALUE;

public class PasscodeListAdapter extends RecyclerView.Adapter<PasscodeListAdapter.PassCodeViewHolder> {
    private Context mContext;
    private List<PasscodeListItem> arrPasscodeListItems;
    private LayoutInflater mLayoutInflater;
    private Key mKey;


    public PasscodeListAdapter(Context mContext, List<PasscodeListItem> arrPasscodeListItems) {
        this.mContext = mContext;
        this.arrPasscodeListItems = arrPasscodeListItems;
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mKey = (Key) SharePreferenceUtility.getPreferences(mContext, KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);
    }

    @NonNull
    @Override
    public PassCodeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.item_device, viewGroup, false);
        return new PassCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassCodeViewHolder passCodeViewHolder, final int i) {

        passCodeViewHolder.mIvImageLock.setVisibility(View.GONE);
//        passCodeViewHolder.createDate.setVisibility(View.GONE);
        passCodeViewHolder.passcode_value.setTextColor(Color.parseColor("#000000"));
        passCodeViewHolder.createDate.setTextColor(Color.parseColor("#808080"));
        passCodeViewHolder.createDate.setTextSize(13);
        passCodeViewHolder.passcode_value.setText("Passcode: " + arrPasscodeListItems.get(i).getKeyboardPwd());
        passCodeViewHolder.createDate.setText("Created date: " + getDate(arrPasscodeListItems.get(i).getSendDate()));

        passCodeViewHolder.passcode_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, arrPasscodeListItems.get(i).keyboardPwd.toString(), Toast.LENGTH_SHORT).show();
                getRequestDeletePasscode(arrPasscodeListItems.get(i).keyboardPwdId, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrPasscodeListItems.size();
    }


    public class PassCodeViewHolder extends RecyclerView.ViewHolder {

        TextView passcode_value, createDate, passcode_delete;
        RelativeLayout mIvImageLock;

        public PassCodeViewHolder(@NonNull View itemView) {
            super(itemView);
            passcode_value = itemView.findViewById(R.id.device_name);
            createDate = itemView.findViewById(R.id.device_address);
            mIvImageLock = itemView.findViewById(R.id.rl_img);
            passcode_delete = itemView.findViewById(R.id.passcode_delete_able);
            passcode_delete.setVisibility(View.VISIBLE);


        }
    }

    public String getDate(long milliSeconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM  dd HH:mm:ss z yyyy");
        return sdf.format(new Date(milliSeconds));
    }

    private void getRequestDeletePasscode(final int passkeyboardpasswordId, final int position) {

        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.deleteKeyboardPwd(mKey.getLockId(), passkeyboardpasswordId, 1);
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                String msg = "Operation failed";
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    Log.e("passcode response", json);
                    if (jsonObject.has("errcode")) {
                        msg = "Operation failed!";
                        if (jsonObject.getInt("errcode") == 0) {
                            deleteItemFromList(position);
                            DisplayUtil.showMessageDialog(mContext, "Passcode delete successfully", mContext.getDrawable(R.drawable.ic_iconfinder_ok_2639876)); //ToDo change mesage
                        } else {
                            DisplayUtil.showMessageDialog(mContext, msg, mContext.getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void deleteItemFromList(int position) {
        arrPasscodeListItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrPasscodeListItems.size());
    }
}
