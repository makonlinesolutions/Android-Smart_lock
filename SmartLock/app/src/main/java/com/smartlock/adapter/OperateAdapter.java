package com.smartlock.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartlock.R;
import com.smartlock.constant.Operate;
import com.smartlock.model.Key;
import com.smartlock.utils.ViewHolder;


public class OperateAdapter extends BaseAdapter {

    private Context mContext;

    private Key mKey;

    String[] operates;

    public OperateAdapter(Context context, Key key, String[] operates) {
        mContext = context;
        mKey = key;
        this.operates = operates;
    }

    @Override
    public int getCount() {
        return operates.length;
    }

    @Override
    public Object getItem(int position) {
        return operates[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, R.layout.item_operate);
        ((TextView)viewHolder.getView(R.id.operate)).setText(operates[position]);
        TextView valueView = viewHolder.getView(R.id.value);
        switch (position) {
            case Operate.SET_ADMIN_CODE:
                valueView.setText(mKey.getNoKeyPwd());
                break;
            case Operate.SET_DELETE_CODE:
                valueView.setText(mKey.getDeletePwd());
                break;
        }
        return viewHolder.getConvertView();
    }
}
