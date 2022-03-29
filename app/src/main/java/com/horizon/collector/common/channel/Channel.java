
package com.horizon.collector.common.channel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 频道实体类
 * Created by YoKeyword on 15/12/29.
 */
public class Channel implements Parcelable {
    public final String id;
    public final String name;

    public Channel (String id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    protected Channel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel source) {
            return new Channel(source);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
