

package com.horizon.base.ui;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.horizon.doodle.worker.lifecycle.LifeEvent;
import com.horizon.doodle.worker.lifecycle.LifeManager;
import com.horizon.doodle.worker.lifecycle.LifeListener;


/**
 * 此类实现了对 Activity onDestroy 的监听，
 * 以确保在 Activity 销毁之前 dismiss。
 */
public class BaseDialog extends Dialog implements LifeListener {
    protected final String TAG = this.getClass().getSimpleName();

    private int hostHash;

    public BaseDialog(@NonNull Activity activity) {
        super(activity);
        hostHash = System.identityHashCode(activity);
        LifeManager.register(hostHash, this);
    }

    public BaseDialog(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
        hostHash = System.identityHashCode(activity);
        LifeManager.register(hostHash, this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        LifeManager.unregister(hostHash, this);
        LifeManager.notify(this, LifeEvent.DESTROY);
    }

    @Override
    public void onEvent(int event) {
        if(event == LifeEvent.DESTROY){
            hostHash = 0;
            if (isShowing()) {
                dismiss();
            }
        }
    }
}
