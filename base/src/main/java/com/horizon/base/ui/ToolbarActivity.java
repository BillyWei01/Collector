package com.horizon.base.ui;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.horizon.base.R;

public abstract class ToolbarActivity extends BaseActivity {
    protected ViewGroup mContentView;
    private Toolbar mToolbar;

    protected abstract int getContentLayout();

    protected abstract void initView();

    protected int getActivityLayout(){
        return R.layout.activity_toolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        mContentView = (ViewGroup) inflater.inflate(getActivityLayout(), null);
        inflater.inflate(getContentLayout(), mContentView, true);

        mToolbar = mContentView.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(mContentView);

        initView();
    }

    protected final void setTitle(String title){
        mToolbar.setTitle(title);
    }

    protected final void hideTitle(){
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


}
