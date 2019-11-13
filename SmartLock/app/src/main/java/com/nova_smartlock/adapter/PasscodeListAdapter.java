package com.nova_smartlock.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.nova_smartlock.interfaces.ShowCustomDialog;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.model.PasscodeListItem;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.NetworkUtils;
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

        if (!compareTwoDate(arrPasscodeListItems.get(i).getStartDate(), arrPasscodeListItems.get(i).getEndDate())) {
            getRequestDeletePasscode(arrPasscodeListItems.get(i).keyboardPwdId, i, 0);
        }
        passCodeViewHolder.mIvImageLock.setVisibility(View.GONE);
//        passCodeViewHolder.createDate.setVisibility(View.GONE);
        passCodeViewHolder.passcode_value.setTextColor(Color.parseColor("#000000"));
        passCodeViewHolder.createDate.setTextColor(Color.parseColor("#808080"));
        passCodeViewHolder.createDate.setTextSize(13);
        passCodeViewHolder.passcode_value.setText(mContext.getString(R.string.passcode) + " " + arrPasscodeListItems.get(i).getKeyboardPwd());
        passCodeViewHolder.start_date.setText(mContext.getString(R.string.passcode_start_date) + " " + getDate(arrPasscodeListItems.get(i).getStartDate()));
        passCodeViewHolder.end_date.setText(mContext.getString(R.string.passcode_expire_date) + " " + getDate(arrPasscodeListItems.get(i).getEndDate()));
        passCodeViewHolder.passcode_code_type.setText(mContext.getString(R.string.passcode_type) + " " + getPasscodeVersion(arrPasscodeListItems.get(i).keyboardPwdType));

        passCodeViewHolder.passcode_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, arrPasscodeListItems.get(i).keyboardPwd.toString(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder confirm_delete = new AlertDialog.Builder(mContext);
                confirm_delete.setCancelable(false);
                confirm_delete.setTitle("Confirm Delete");
                confirm_delete.setMessage("Are you sure you want to delete this passcode ?");
                confirm_delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(NetworkUtils.isNetworkConnected(mContext)){
                            getRequestDeletePasscode(arrPasscodeListItems.get(i).keyboardPwdId, i, 1);
                        }else {
                            DisplayUtil.showMessageDialog(mContext, "Please check Mobile network connection",
                                    mContext.getResources().getDrawable(R.drawable.ic_no_internet));
                        }
                    }
                });
                confirm_delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = confirm_delete.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrPasscodeListItems.size();
    }


    public class PassCodeViewHolder extends RecyclerView.ViewHolder {

        TextView passcode_value, createDate, passcode_delete, passcode_code_type, start_date, end_date;
        RelativeLayout mIvImageLock;

        public PassCodeViewHolder(@NonNull View itemView) {
            super(itemView);
            passcode_value = itemView.findViewById(R.id.device_name);
            start_date = itemView.findViewById(R.id.passcode_start_date);
            end_date = itemView.findViewById(R.id.passcode_end_date);
            passcode_code_type = itemView.findViewById(R.id.passcode_type);
            createDate = itemView.findViewById(R.id.device_address);
            mIvImageLock = itemView.findViewById(R.id.rl_img);
            passcode_delete = itemView.findViewById(R.id.passcode_delete_able);
            passcode_delete.setVisibility(View.VISIBLE);
            passcode_code_type.setVisibility(View.VISIBLE);
            start_date.setVisibility(View.VISIBLE);
            end_date.setVisibility(View.VISIBLE);
            createDate.setVisibility(View.GONE);


        }
    }

    public String getDate(long milliSeconds) {
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM  dd HH:MM:SS z yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd MMM, yyyy");
        Log.d("time :", sdf.format(new Date(milliSeconds)));
        return sdf.format(new Date(milliSeconds));
    }

    private void getRequestDeletePasscode(final int passkeyboardpasswordId, final int position, final int from_where) {

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
                            if (from_where == 1) {
                                DisplayUtil.showMessageDialog(mContext, "Passcode delete successfully", mContext.getDrawable(R.drawable.ic_iconfinder_ok_2639876)); //ToDo change mesage
                            }
                        } else {
                            if (from_where == 1) {
                                DisplayUtil.showMessageDialog(mContext, msg, mContext.getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                            }
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
        try {
            arrPasscodeListItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, arrPasscodeListItems.size());
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private String getPasscodeVersion(int keyboardPwdType) {
        switch (keyboardPwdType) {
            case 1:
                return "One-time";
            case 2:
                return "Permanent";
            case 3:
                return "Timed";
            case 4:
                return "Delete";
            case 5:
                return "Weekend Cyclic";
            case 6:
                return "Daily Cyclic";
            case 7:
                return "Wordday Cyclic";
            case 8:
                return "Monday Cyclic";
            case 9:
                return "Tuesday Cyclic";
            case 10:
                return "Wednesday Cyclic";
            case 11:
                return "Thursday Cyclic";
            case 12:
                return "Friday Cyclic";
            case 13:
                return "Saturday Cyclic";
            case 14:
                return "Sunday Cyclic";
            default:
                return "null";
        }
    }

    private boolean compareTwoDate(long start_date, long end_date) {
        if (end_date - System.currentTimeMillis() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
