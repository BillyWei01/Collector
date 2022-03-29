package com.horizon.collector.setting.path;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.horizon.base.ui.BaseActivity;
import com.horizon.base.ui.BaseDialog;
import com.horizon.collector.R;


public class AddFolderDialog extends BaseDialog  {
    public AddFolderDialog(@NonNull BaseActivity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_channel);
    }
}
