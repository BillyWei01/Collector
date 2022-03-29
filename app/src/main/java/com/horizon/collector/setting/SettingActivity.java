package com.horizon.collector.setting;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;

import com.horizon.base.event.Event;
import com.horizon.base.ui.ToolbarActivity;
import com.horizon.collector.R;
import com.horizon.collector.setting.model.SettingItem;
import com.horizon.collector.setting.model.UserSetting;
import com.horizon.collector.setting.path.PickPathActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class SettingActivity extends ToolbarActivity implements SettingAdapter.OnItemClickListener {
    private static final int ID_COLLECT_PATH = 1;

    private static final int RC_READ_WRITE_STORAGE = 1;

    private SettingAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.layout_setting;
    }

    @Override
    protected void initView() {
        setTitle(getStr(R.string.setting));

        List<SettingItem> settingItems = new ArrayList<>();
        settingItems.add(getCollectPathItem());

        mAdapter = new SettingAdapter(mActivity, settingItems, false, this);

        RecyclerView settingRv = findViewById(R.id.settings_rv);
        if (settingRv != null) {
            settingRv.setLayoutManager(new LinearLayoutManager(this));
            settingRv.setAdapter(mAdapter);
        }
    }

    private SettingItem getCollectPathItem() {
        String path = UserSetting.INSTANCE.getCollectPath();
        if (TextUtils.isEmpty(path)) {
            path = getStr(R.string.not_set);
        }
        return new SettingItem(ID_COLLECT_PATH, getStr(R.string.collect_path), path);
    }

    @Override
    public void onItemClick(int id) {
        switch (id) {
            case ID_COLLECT_PATH:
                chooseCollectPath();
                break;
            default:
                break;
        }
    }

    private void chooseCollectPath() {
        pickPath();
    }

    @AfterPermissionGranted(RC_READ_WRITE_STORAGE)
    private void pickPath() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startActivity(PickPathActivity.class);
        } else {
            EasyPermissions.requestPermissions(this,
                    getStr(R.string.storage_permission_request),
                    RC_READ_WRITE_STORAGE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private SettingItem getItem(int id) {
        List<SettingItem> items = mAdapter.getData();
        for (SettingItem item : items) {
            if (item.id == id) {
                return item;
            }
        }
        return new SettingItem(0, "", "");
    }

    @Override
    public void onEvent(int event, @NotNull Object... args) {
        switch (event) {
            case Event.CHOSE_PATH:
                SettingItem item = getItem(ID_COLLECT_PATH);
                item.subtitle = (String) args[0];
                int pos = mAdapter.getData().indexOf(item);
                mAdapter.notifyItemChanged(pos);
                break;
            default:
                break;
        }
    }

    @androidx.annotation.NonNull
    @Override
    public int[] listenEvents() {
        return new int[]{
                Event.CHOSE_PATH
        };
    }

}
