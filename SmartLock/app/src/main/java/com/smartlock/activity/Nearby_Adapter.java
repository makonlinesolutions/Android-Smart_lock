package com.smartlock.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smartlock.R;
import com.smartlock.model.Key;
import com.smartlock.utils.SharePreferenceUtility;

import java.util.List;

import static com.smartlock.activity.NearbyLockActivity.curKey;
import static com.smartlock.utils.Const.KEY_VALUE;

public class Nearby_Adapter extends RecyclerView.Adapter<Nearby_Adapter.Nearby_holder> {
    Activity activity;
    List<Key> nearby_models;

    public Nearby_Adapter(Activity activity, List<Key> nearby_models) {
        this.activity = activity;
        this.nearby_models = nearby_models;
    }

    @NonNull
    @Override
    public Nearby_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_nearby_locks, viewGroup, false);
        Nearby_holder nearbt_holder = new Nearby_holder(view);
        return nearbt_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Nearby_holder nearby_holder, int i) {
        nearby_holder.name.setText(nearby_models.get(i).getLockAlias());
    }

    @Override
    public int getItemCount() {
        return nearby_models.size();
    }

    public class Nearby_holder extends RecyclerView.ViewHolder {
        TextView name;

        public Nearby_holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curKey = nearby_models.get(getAdapterPosition());
                    SharePreferenceUtility.saveObjectPreferences(activity.getBaseContext(), KEY_VALUE, nearby_models.get(getAdapterPosition()));
                    if (curKey.isAdmin()) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtra("isAdmin", true);
                        activity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtra("isAdmin", false);
                        activity.startActivity(intent);
                        Toast.makeText(activity, activity.getString(R.string.words_not_admin), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
