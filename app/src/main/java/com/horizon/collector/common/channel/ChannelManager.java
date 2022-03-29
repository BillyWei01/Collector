package com.horizon.collector.common.channel;


import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;


import com.horizon.collector.setting.model.UserSetting;

import java.util.ArrayList;
import java.util.List;

public class ChannelManager {
    private final List<Channel> myChannels;
    private final List<Channel> otherChannels;

    private static final int HUABAN = 0 ;

    private static final SparseArray<ChannelManager> INSTANCES = new SparseArray<>(2);

    private ChannelManager(int key) {
        String str= UserSetting.INSTANCE.getHuabanChannels();
        if (TextUtils.isEmpty(str)) {
            List<List<Channel>> channels = getDefaultChannels(key);
            myChannels = channels.get(0);
            otherChannels = channels.get(1);
            saveChannels();
        } else {
            String[] result = str.split("&");
            myChannels = decodeChannels(result[0]);
            if(result.length > 1){
                otherChannels =  decodeChannels(result[1]);
            }else {
                otherChannels =  new ArrayList<>();
            }
        }
    }

    public static ChannelManager getHuabanManager() {
        return getInstance(HUABAN);
    }

    private synchronized static ChannelManager getInstance(int key) {
        ChannelManager manager = INSTANCES.get(key);
        if (manager == null) {
            manager = new ChannelManager(key);
            INSTANCES.put(key, manager);
        }
        return manager;
    }

    public List<Channel> getMyChannels() {
        return myChannels;
    }

    public List<Channel> getOtherChannels() {
        return otherChannels;
    }

    public void saveChannels() {
        StringBuilder builder = new StringBuilder();
        encodeChannels(myChannels, builder);
        builder.setCharAt(builder.length() - 1, '&');
        encodeChannels(otherChannels, builder);
        builder.setLength(builder.length() - 1);
        String channels = builder.toString();
        UserSetting.INSTANCE.setHuabanChannels(channels);
    }

    private void encodeChannels(@NonNull List<Channel> channels, StringBuilder builder) {
        for (Channel channel : channels) {
            builder.append(channel.id).append(':').append(channel.name).append(',');
        }
    }

    private List<Channel> decodeChannels(String result) {
        List<Channel> channels = new ArrayList<>();
        if (TextUtils.isEmpty(result)) {
            return channels;
        }
        String[] cs = result.split(",");
        for (String c : cs) {
            int index = c.indexOf(':');
            String id = c.substring(0, index);
            String name = c.substring(index + 1);
            channels.add(new Channel(id, name));
        }
        return channels;
    }

    private List<List<Channel>> getDefaultChannels(int key) {
        List<Channel> myChannels = new ArrayList<>();
        List<Channel> otherChannels = new ArrayList<>();

        if (key == HUABAN) {
            myChannels.add(new Channel("anime", "动漫"));
            myChannels.add(new Channel("beauty", "妹纸"));
            myChannels.add(new Channel("travel_places", "旅行"));

            otherChannels.add(new Channel("pets", "宠物"));
            otherChannels.add(new Channel("photography", "摄影"));
            otherChannels.add(new Channel("apparel", "服装"));
        } else {
            myChannels.add(new Channel("nature", "nature"));
            myChannels.add(new Channel("landscape", "landscape"));
            myChannels.add(new Channel("flowers", "flowers"));
            myChannels.add(new Channel("wedding", "wedding"));
            myChannels.add(new Channel("pets", "pets"));

            otherChannels.add(new Channel("love", "love"));
            otherChannels.add(new Channel("food", "food"));
            otherChannels.add(new Channel("space", "space"));
            otherChannels.add(new Channel("spring", "spring"));
            otherChannels.add(new Channel("summer", "summer"));
            otherChannels.add(new Channel("autumn", "autumn"));
            otherChannels.add(new Channel("winter", "winter"));
        }

        List<List<Channel>> channels = new ArrayList<>(2);
        channels.add(myChannels);
        channels.add(otherChannels);
        return channels;
    }

}
