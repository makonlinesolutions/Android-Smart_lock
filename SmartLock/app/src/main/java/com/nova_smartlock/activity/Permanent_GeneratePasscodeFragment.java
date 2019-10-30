package com.nova_smartlock.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nova_smartlock.R;
import com.nova_smartlock.dao.DbService;
import com.nova_smartlock.model.Key;
import com.nova_smartlock.net.ResponseService;
import com.nova_smartlock.utils.DisplayUtil;
import com.nova_smartlock.utils.NetworkUtils;
import com.nova_smartlock.utils.SharePreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dmax.dialog.SpotsDialog;

import static com.nova_smartlock.utils.Const.KEY_VALUE;

public class Permanent_GeneratePasscodeFragment extends Fragment {

    private Button mBtGeneratePasscode;
    private View mParentView;
    private List<Key> arrKey;
    private Key mKey;
    private Context mContext;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_permanent_generatepasscode, container, false);
        mBtGeneratePasscode = mParentView.findViewById(R.id.btn_generate_passcode);
        mContext = getContext();
        alertDialog = new SpotsDialog.Builder().setContext(mContext).setMessage("Loading").build();
        arrKey = DbService.getKeyListKey();
        mKey = (Key) SharePreferenceUtility.getPreferences(getContext(), KEY_VALUE, SharePreferenceUtility.PREFTYPE_OBJECT);
        mBtGeneratePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected(mContext)) {
                    getRequestGeneratePasscode();
                }else {
                    DisplayUtil.showMessageDialog(mContext, "Please check mobile network connection", getActivity().getDrawable(R.drawable.ic_no_internet));
                }
            }
        });
        return mParentView;
    }

    private void getRequestGeneratePasscode() {
        alertDialog.show();
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return ResponseService.getKeyboardPwdPermanent(mKey.getLockId(), 4, 2, System.currentTimeMillis(), Long.parseLong("1923244200000"));
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String json) {
                alertDialog.dismiss();
                String msg = getContext().getString(R.string.words_authorize_successed);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        msg = "Operation failed!";
                        if (jsonObject.getInt("errcode") == 0) {
                            DisplayUtil.showMessageDialog(getContext(), msg, getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                            //Toast.makeText(mContext, "Now you can the credentials", Toast.LENGTH_SHORT).show();
                        } else {
                            DisplayUtil.showMessageDialog(getContext(), msg, getActivity().getDrawable(R.drawable.ic_iconfinder_143_attention_183267)); //ToDo change mesage
                            //Toast.makeText(mContext, "Now you can the credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String keyboardPwd = jsonObject.getString("keyboardPwd");
                        String keyboardPwdId = jsonObject.getString("keyboardPwdId");

                        DisplayUtil.showMessageDialog(getContext(), "Passcode generated successfully. Your Passcode is: " + keyboardPwd, getActivity().getDrawable(R.drawable.ic_iconfinder_ok_2639876));
                        //Toast.makeText(mContext, "E-Key sent successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
