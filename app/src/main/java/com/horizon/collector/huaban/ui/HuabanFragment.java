
package com.horizon.collector.huaban.ui;


import android.text.TextUtils;

import com.horizon.collector.common.ChannelFragment;
import com.horizon.collector.common.PageFragment;
import com.horizon.collector.common.channel.Channel;
import com.horizon.collector.common.channel.ChannelManager;
import com.horizon.base.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HuabanFragment extends PageFragment {
    public static final String TAG = "HuabanFragment";

    private List<HuabanChannelFragment> mFragments = new ArrayList<>();

    public static HuabanFragment newInstance() {
        return new HuabanFragment();
    }

    @Override
    protected List<? extends ChannelFragment> getFragments() {
        if (CollectionUtil.isEmpty(mFragments)) {
            List<Channel> channels = getMyChannels();
            for (Channel channel : channels) {
                mFragments.add(HuabanChannelFragment.newInstance(channel));
            }
        }
        return mFragments;
    }

    @Override
    protected void updateFragments() {
        // 保存渠道配置
        ChannelManager.getHuabanManager().saveChannels();

        // 构造新的渠道列表
        List<Channel> channels = getMyChannels();
        List<HuabanChannelFragment> newFragments = new ArrayList<>(channels.size());
        for (Channel channel : channels) {
            HuabanChannelFragment fragment = pickFragment(channel);
            if(fragment == null){
                fragment = HuabanChannelFragment.newInstance(channel);
            }
            newFragments.add(fragment);
        }

        // 清除旧的渠道列表
        mFragments.clear();

        // 添加新的渠道列表
        mFragments.addAll(newFragments);
    }

    private HuabanChannelFragment pickFragment(Channel channel){
        Iterator<HuabanChannelFragment> iterator = mFragments.iterator();
        while (iterator.hasNext()){
            HuabanChannelFragment fragment = iterator.next();
            if(TextUtils.equals(fragment.getChannelID(), channel.id)){
                iterator.remove();
                return fragment;
            }
        }
        return null;
    }

    @Override
    protected List<Channel> getMyChannels() {
        return ChannelManager.getHuabanManager().getMyChannels();
    }

    @Override
    protected List<Channel> getOtherChannels() {
        return ChannelManager.getHuabanManager().getOtherChannels();
    }

}
