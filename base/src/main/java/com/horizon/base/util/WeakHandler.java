package com.horizon.base.util;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {
    private final WeakReference<MessageTarget> mTargetReference;

    public WeakHandler(MessageTarget target, Looper looper) {
        super(looper);
        this.mTargetReference = new WeakReference<>(target);
    }

    @Override
    public void handleMessage(Message msg) {
        final MessageTarget callback = mTargetReference.get();
        if (callback != null) {
            callback.handleMessage(msg);
        }
    }

    public interface MessageTarget{
        void handleMessage(Message msg);
    }
}

