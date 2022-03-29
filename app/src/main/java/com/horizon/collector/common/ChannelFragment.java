
package com.horizon.collector.common;


import android.os.Bundle;

import com.horizon.base.ui.BaseFragment;
import com.horizon.collector.common.channel.Channel;
import com.horizon.doodle.MHash;

import java.util.HashSet;
import java.util.Set;

public abstract class ChannelFragment extends BaseFragment {
    protected static final int MODE_REFRESH = 0;
    protected static final int MODE_LOAD_MORE = 1;

    protected static final String ARG_CHANNEL = "channel";

    protected int mLastPage = 1;
    protected int mNextPage = 2;
    protected int mLoadingMode = MODE_REFRESH;

    protected Channel mChannel;
    private long mFragmentID;
    protected Set<String> mIdSet;

    protected static void initFragment(ChannelFragment fragment, Channel channel){
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_CHANNEL, channel);
        fragment.setArguments(arguments);
        fragment.init(channel);
    }

    private void init(Channel channel){
        mChannel = channel;
        TAG = TAG + "_" + channel.id;
        mFragmentID = MHash.hash64(TAG);
        mIdSet = new HashSet<>();
    }

    @Override
    protected void initView() {
        if(mChannel == null && getArguments() != null) {
            Channel channel = getArguments().getParcelable(ARG_CHANNEL);
            if(channel != null){
                init(channel);
            }
        }
    }

    public String getChannelID(){
        return mChannel.id;
    }

    /**
     * 频道的标题
     */
    public String getTitle() {
        return mChannel.name;
    }

    /**
     * 用频道ID作为Fragment的标识
     *
     * @return hash(类型, channel id)
     */
    public long getFragmentID() {
        return mFragmentID;
    }
}
