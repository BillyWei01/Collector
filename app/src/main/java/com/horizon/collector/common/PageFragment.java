

package com.horizon.collector.common;


import android.content.DialogInterface;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.horizon.base.ui.BaseFragment;
import com.horizon.collector.R;
import com.horizon.collector.common.channel.Channel;
import com.horizon.collector.common.channel.ChannelDialog;

import java.util.List;

public abstract class PageFragment extends BaseFragment {
    protected abstract List<? extends ChannelFragment> getFragments();

    protected abstract void updateFragments();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pages;
    }

    protected abstract List<Channel> getMyChannels();

    protected abstract List<Channel> getOtherChannels();

    @Override
    protected void initView() {
        final PageFragmentAdapter pagerAdapter = new PageFragmentAdapter(getChildFragmentManager());
        pagerAdapter.setData(getFragments());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        ImageView editChannelIv = (ImageView) findViewById(R.id.edit_channel_iv);
        editChannelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelDialog dialog = new ChannelDialog(mActivity, getMyChannels(), getOtherChannels());
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateFragments();
                        pagerAdapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });
    }

    private class PageFragmentAdapter extends FragmentPagerAdapter {
        private List<? extends ChannelFragment> fragments;

        public PageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setData(List<? extends ChannelFragment> fragments) {
            this.fragments = fragments;
        }

        @Override
        public ChannelFragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getFragmentID();
        }

        public int getItemPosition(Object object) {
            if (object instanceof BaseFragment) {
                int pos = fragments.indexOf(object);
                return pos >= 0 ? pos : POSITION_NONE;
            }
            return POSITION_UNCHANGED;
        }
    }

}
