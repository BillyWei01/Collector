package com.horizon.collector.setting.path;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.horizon.base.ui.BaseAdapter;
import com.horizon.base.util.ResUtil;
import com.horizon.collector.R;

import java.io.File;
import java.util.List;

public class PathAdapter extends BaseAdapter<File, PathAdapter.FolderHolder> {
    public interface OnItemClickListener{
        void onItemClick(File file);
    }

    private OnItemClickListener mOnItemClickListener;

    private PathFilter mPathFiler;
    private int mCheckPosition = -1;

    PathAdapter(Context context, List<File> data, boolean loadMoreFlag, PathFilter pathFilter) {
        super(context, data, loadMoreFlag);
        mPathFiler = pathFilter;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    File getCheckFile(){
        return mCheckPosition == -1 ? null : mData.get(mCheckPosition);
    }

    @Override
    public void setData(List<File> data) {
        super.setData(data);
        mCheckPosition = -1;
    }

    @Override
    protected FolderHolder getItemHolder(ViewGroup parent) {
        return new FolderHolder(inflate(R.layout.item_path, parent));
    }

    @Override
    protected void bindHolder(File file, int position, FolderHolder holder) {
        holder.nameTv.setText(file.getName());
        File[] files = file.listFiles(mPathFiler);
        int filesLen = (files == null) ? 0 : files.length;
        holder.detailTv.setText(ResUtil.getStr(R.string.item, Integer.toString(filesLen)));
        holder.pathCb.setChecked(mCheckPosition == position);
    }

    class FolderHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private TextView detailTv;
        private CheckBox pathCb;

        FolderHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            detailTv = itemView.findViewById(R.id.detail_tv);
            pathCb = itemView.findViewById(R.id.path_cb);

            pathCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (isChecked) {
                        int lastPos = mCheckPosition;
                        mCheckPosition = position;
                        if (lastPos >= 0 && lastPos != position) {
                            notifyItemChanged(lastPos);
                        }
                    } else if (mCheckPosition == position) {
                        mCheckPosition = -1;
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(mData.get(position));
                    }
                }
            });
        }
    }
}
