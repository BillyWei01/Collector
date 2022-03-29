
package com.horizon.base.ui;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.horizon.base.config.GlobalConfig;
import com.horizon.base.util.ResUtil;
import com.horizon.base.event.EventManager;
import com.horizon.base.event.Observer;

import org.jetbrains.annotations.NotNull;

public abstract class BaseActivity extends AppCompatActivity implements Observer {
    private static final int[] EMPTY_EVENTS = new int[0];

    protected final String TAG = this.getClass().getSimpleName();

    protected final BaseActivity mActivity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.register(this);
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);
    }

    public void startActivity(Class<?> activityClazz) {
        Intent intent = new Intent(this, activityClazz);
        startActivity(intent);
    }

    protected static String getStr(int resId) {
        return ResUtil.getStr(resId);
    }

    @Override
    public void onEvent(int event, @NotNull Object... args) {

    }

    @androidx.annotation.NonNull
    @Override
    public int[] listenEvents() {
        return EMPTY_EVENTS;
    }
}
