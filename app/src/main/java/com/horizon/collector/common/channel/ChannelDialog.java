package com.horizon.collector.common.channel;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.Window;

import com.horizon.base.ui.BaseActivity;
import com.horizon.base.ui.BaseDialog;
import com.horizon.collector.R;

import java.util.List;

public class ChannelDialog extends BaseDialog implements ChannelAdapter.FinishEditListener {

    private List<Channel> mMyChannels;
    private List<Channel> mOtherChannel;

    public ChannelDialog(@NonNull BaseActivity activity,
                         List<Channel> myChannels, List<Channel> otherChannels) {
        super(activity, R.style.ChannelDialog);
        mMyChannels = myChannels;
        mOtherChannel = otherChannels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_channel);

        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.SlideInOutAnim);
        }

        RecyclerView channelRv = findViewById(R.id.channel_rv);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        channelRv.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(channelRv);

        final ChannelAdapter adapter =
                new ChannelAdapter(getContext(), helper, mMyChannels, mOtherChannel, this);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return (viewType == ChannelAdapter.TYPE_MY
                        || viewType == ChannelAdapter.TYPE_OTHER) ? 1 : 4;
            }
        });
        channelRv.setAdapter(adapter);
    }

    @Override
    public void onFinishEdit() {
        dismiss();
    }
}
