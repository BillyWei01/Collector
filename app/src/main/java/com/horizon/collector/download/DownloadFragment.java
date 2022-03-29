package com.horizon.collector.download;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;

import com.horizon.base.event.Event;
import com.horizon.base.ui.BaseFragment;
import com.horizon.collector.R;
import com.horizon.collector.setting.model.UserSetting;
import com.horizon.base.event.Observer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadFragment extends BaseFragment implements Observer {
    public static final String TAG = "DownloadFragment";

    private DownloadAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initView() {
        List<String> pathList = loadPhotos();
        mAdapter = new DownloadAdapter(mActivity,pathList, false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.download_rv);
        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(mAdapter);
    }

    private List<String> loadPhotos(){
        List<String> pathList = new ArrayList<>();
        String path = UserSetting.INSTANCE.getCollectPath();
        if(TextUtils.isEmpty(path)){
            return pathList;
        }
        File dir = new File(path);
        if(dir.exists()){
            File[] files = dir.listFiles();
            if(files != null){
                for (File file : files) {
                    pathList.add(file.getAbsolutePath());
                }
            }
        }
        return pathList;
    }

    @Override
    public void onEvent(int event, @NotNull Object... args) {
        mAdapter.setData(loadPhotos());
    }

    @androidx.annotation.NonNull
    @Override
    public int[] listenEvents() {
        return new int[]{
                Event.CHOSE_PATH,
                Event.DOWNLOAD_FINISH
        };
    }
}
