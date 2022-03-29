
package com.horizon.base.ui;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.horizon.base.config.GlobalConfig;
import com.horizon.doodle.Doodle;
import com.horizon.base.event.EventManager;
import com.horizon.base.event.Observer;

import org.jetbrains.annotations.NotNull;
import com.horizon.doodle.worker.lifecycle.LifeEvent;

public abstract class BaseFragment extends Fragment implements Observer {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    private static final int[] EMPTY_EVENTS = new int[0];

    protected String TAG = this.getClass().getSimpleName();

    protected BaseActivity mActivity;
    private View mRootView;
    private boolean isViewRecycled = false;

    private boolean isVisible;
    private boolean isActivityCreated;
    protected boolean isDataLoaded;

    protected abstract int getLayoutResource();

    protected abstract void initView();

    protected void loadData() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    protected final View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.register(this);

        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();

            if (GlobalConfig.DEBUG) {
                Log.d(TAG, "restore instance state");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());

        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onSaveInstanceState");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onCreateView " + (mRootView == null));
        }
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutResource(), container, false);
            isViewRecycled = false;
        } else {
            isViewRecycled = true;
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onActivityCreated");
        }

        if (isViewRecycled) {
            return;
        }

        initView();

        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "fragment decor root 3: "  + System.identityHashCode(mRootView.getRootView()));
        }

        isActivityCreated = true;
        checkLoadData();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "setUserVisibleHint " + isVisibleToUser);
        }

        Doodle.notifyEvent(this, isVisibleToUser ? LifeEvent.SHOW : LifeEvent.HIDE);

        isVisible = isVisibleToUser;
        checkLoadData();
    }

    private void checkLoadData() {
        if (isVisible && isActivityCreated && !isDataLoaded) {
            if (GlobalConfig.DEBUG) {
                Log.d(TAG, "loadData");
            }
            loadData();
            isDataLoaded = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onPause");
        }
        Doodle.notifyEvent(this, LifeEvent.HIDE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onResume");
        }
        Doodle.notifyEvent(this, LifeEvent.SHOW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);

        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "onDestroy");
        }
        Doodle.notifyEvent(this, LifeEvent.DESTROY);
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
