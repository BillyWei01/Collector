package com.horizon.collector.setting.path;


import android.os.Environment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.horizon.base.event.Event;
import com.horizon.base.ui.ToolbarActivity;
import com.horizon.base.util.ToastUtil;
import com.horizon.collector.R;
import com.horizon.collector.setting.model.UserSetting;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class PickPathActivity extends ToolbarActivity {
    private TextView mPathTv;
    private RecyclerView mPathRv;
    private PathAdapter mPathAdapter;

    private File mCurrentPath;
    private Stack<List<File>> mPathStack;
    private Stack<File> mFileStack;
    private PathFilter mPathFiler;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_toolbar_relative;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.layout_pick_path;
    }

    @Override
    protected void initView() {
        setTitle(getStr(R.string.select_path));


        File root = Environment.getExternalStorageDirectory();
        mCurrentPath = root;
        mFileStack = new Stack<>();
        mPathStack = new Stack<>();

        mPathFiler = new PathFilter();
        mPathFiler.setShowHidden(UserSetting.INSTANCE.getShowHidden());

        mPathTv = findViewById(R.id.path_tv);
        mPathTv.setText(root.getPath());

        List<File> data = getFiles(root, mPathFiler);
        mPathAdapter = new PathAdapter(mActivity, data, true, mPathFiler);
        mPathRv = findViewById(R.id.path_rv);
        if (mPathRv != null) {
            mPathRv.setLayoutManager(new LinearLayoutManager(this));
            mPathRv.setAdapter(mPathAdapter);
        }
        mPathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file) {
                List<File> subFiles = getFiles(file, mPathFiler);
                mFileStack.push(mCurrentPath);
                mPathStack.push(new ArrayList<>(mPathAdapter.getData()));
                mPathTv.setText(file.getPath());
                mPathAdapter.setData(subFiles);
                mCurrentPath = file;
            }
        });

        TextView backTv = findViewById(R.id.back_tv);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFileStack.isEmpty()) {
                    File file = mFileStack.pop();
                    List<File> subFiles = mPathStack.pop();
                    mPathTv.setText(file.getPath());
                    mPathAdapter.setData(subFiles);
                    mPathRv.scrollToPosition(0);
                    mCurrentPath = file;
                }
            }
        });

        Button selectBtn = findViewById(R.id.select_btn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File checkFile = mPathAdapter.getCheckFile();
                if (checkFile != null) {
                    UserSetting.INSTANCE.setCollectPath(checkFile.getPath());
                    com.horizon.base.event.EventManager.notify(Event.CHOSE_PATH, checkFile.getPath());
                    finish();
                } else {
                    ToastUtil.showTips(R.string.check_tips);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pick_path, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemShowHidden = menu.findItem(R.id.action_show_hidden);

        if (!UserSetting.INSTANCE.getShowHidden()) {
            itemShowHidden.setTitle(R.string.show_hidden_files);
        } else {
            itemShowHidden.setTitle(R.string.hidden_files);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_show_hidden:
                toggleShowHidden();
                return true;
            case R.id.add_folder:
                ToastUtil.showTips("该功能还未实现-_-");
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleShowHidden() {
        boolean showHidden = UserSetting.INSTANCE.getShowHidden();
        UserSetting.INSTANCE.setShowHidden(!showHidden);
        mPathFiler.setShowHidden(!showHidden);
        List<File> subFiles = getFiles(mCurrentPath, mPathFiler);
        mPathAdapter.setData(subFiles);
        mPathRv.scrollToPosition(0);
        mPathRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        }, 100L);
    }

    private List<File> getFiles(File root, FileFilter filter) {
        List<File> files = new ArrayList<>();
        File[] fileArray = root.listFiles(filter);
        if (fileArray != null && fileArray.length > 0) {
            files.addAll(Arrays.asList(fileArray));
        }
        Collections.sort(files, new PathComparator());
        return files;
    }

}
