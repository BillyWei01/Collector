package com.horizon.collector.setting;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.horizon.base.ui.BaseAdapter;
import com.horizon.collector.R;
import com.horizon.collector.setting.model.SettingItem;

import java.util.List;

public class SettingAdapter extends BaseAdapter<SettingItem, SettingAdapter.SettingHolder> {
    interface OnItemClickListener {
        void onItemClick(int id);
    }

    private OnItemClickListener mListener;

    SettingAdapter(Context context, List<SettingItem> data, boolean loadMoreFlag,
                   OnItemClickListener listener) {
        super(context, data, loadMoreFlag);
        mListener = listener;
    }

    @Override
    protected SettingHolder getItemHolder(ViewGroup parent) {
        return new SettingHolder(inflate(R.layout.item_setting, parent));
    }

    @Override
    protected void bindHolder(SettingItem item, int position, SettingHolder holder) {
        holder.titleTv.setText(item.title);
        if (!TextUtils.isEmpty(item.subtitle)) {
            holder.subtitleTv.setText(item.subtitle);
            holder.subtitleTv.setVisibility(View.VISIBLE);
        } else {
            holder.subtitleTv.setVisibility(View.GONE);
        }
    }

    class SettingHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private TextView subtitleTv;

        SettingHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.title_tv);
            subtitleTv = itemView.findViewById(R.id.subtitle_tv);
            if (mListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position >= 0 && position < mData.size()) {
                            mListener.onItemClick(mData.get(position).id);
                        }
                    }
                });
            }
        }
    }


}
