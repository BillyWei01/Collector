package com.horizon.collector.download;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.horizon.base.ui.BaseAdapter;
import com.horizon.base.widget.FlowImageView;
import com.horizon.collector.R;
import com.horizon.collector.common.ExtraKey;
import com.horizon.collector.common.PhotoDetailActivity;
import com.horizon.doodle.Doodle;

import java.util.List;

public class DownloadAdapter extends BaseAdapter<String, DownloadAdapter.DownloadHolder> {

    public DownloadAdapter(Context context, List<String> data, boolean loadMoreFlag) {
        super(context, data, loadMoreFlag);
    }

    @Override
    protected DownloadHolder getItemHolder(ViewGroup parent) {
        return new DownloadHolder(inflate(R.layout.item_download, parent));
    }

    @Override
    protected void bindHolder(String item, int position, DownloadHolder holder) {
        int c;
        if (holder.flowIv.getWidth() > 0) {
            c = holder.flowIv.getWidth();
        } else {
            Resources resources = mContext.getResources();
            int margin = resources.getDimensionPixelSize(R.dimen.flow_item_margin);
            int width = resources.getDisplayMetrics().widthPixels;
            c = (width - 6 * margin) / 3;
        }

        holder.flowIv.setSourceSize(c, c);
        holder.flowIv.requestLayout();

        Doodle.load(item)
                .host(getHost())
                .override(c, c)
                .into(holder.flowIv);
    }

    class DownloadHolder extends RecyclerView.ViewHolder {
        private FlowImageView flowIv;

        DownloadHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPinDetail(getAdapterPosition());
                }
            });
            flowIv = itemView.findViewById(R.id.flow_iv);
        }

        private void toPinDetail(int position) {
            if (position >= 0 && position < mData.size()) {
                Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                intent.putExtra(ExtraKey.DETAIL_URL, mData.get(position));
                mContext.startActivity(intent);
            }
        }
    }
}
