

package com.horizon.base.ui;


import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.horizon.base.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_EMPTY = 4;
    private static final int TYPE_UNKNOWN = 3;


    public interface OnLoadMoreListener {
        void onLoadMore(boolean forceReload);
    }

    private class DefaultHolder extends ViewHolder {
        public DefaultHolder(View itemView) {
            super(itemView);
        }
    }

    private OnLoadMoreListener mLoadMoreListener;

    private Object mHost;

    protected final Context mContext;
    protected final List<T> mData;
    private final boolean mLoadMoreFlag;

    private FrameLayout mFooterLayout;
    private View mLoadingFooter;
    private View mEndFooter;

    private View mEmptyView;

    protected abstract VH getItemHolder(ViewGroup parent);

    protected abstract void bindHolder(T item, int position, VH holder);

    public BaseAdapter(Context context, List<T> data, boolean loadMoreFlag) {
        mContext = context;
        mData = data == null ? new ArrayList<T>() : data;
        mLoadMoreFlag = loadMoreFlag;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public int getDataSize() {
        return mData.size();
    }

    public void setHost(Object holder) {
        mHost = holder;
    }

    protected final Object getHost() {
        if (mHost != null) {
            return mHost;
        }
        if (mContext instanceof BaseActivity) {
            return mContext;
        }
        return null;
    }


    @Override
    public int getItemCount() {
        if (CollectionUtil.isEmpty(mData)) {
            return mEmptyView == null ? 0 : 1;
        } else {
            return mData.size() + (mLoadMoreFlag ? 1 : 0);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (CollectionUtil.isEmpty(mData)) {
            return mEmptyView == null ? TYPE_UNKNOWN : TYPE_EMPTY;
        }
        if (isFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return new DefaultHolder(mEmptyView);
            case TYPE_FOOTER:
                if (mFooterLayout == null) {
                    mFooterLayout = new FrameLayout(mContext);
                }
                return new DefaultHolder(mFooterLayout);
            case TYPE_UNKNOWN:
                return new DefaultHolder(new View(mContext));
            default:
                return getItemHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ITEM:
                //noinspection unchecked
                bindHolder(mData.get(position), position, (VH) holder);
                break;
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooter(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooter(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

        if (mLoadMoreFlag && mLoadMoreListener != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (mFooterLayout.getChildAt(0) != mEndFooter
                                && findLastVisibleItem(layoutManager) + 1 == getItemCount()) {
                            mLoadMoreListener.onLoadMore(false);
                        }
                    }
                }
            });
        }
    }

    private boolean isFooter(int position) {
        return mLoadMoreFlag && getItemCount() > 1 && position >= getItemCount() - 1;
    }

    private int findLastVisibleItem(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            int max = -1;
            int len = positions.length;
            for (int i = 0; i < len; i++) {
                if (positions[i] > max) {
                    max = positions[i];
                }
            }
            return max;
        }
        return -1;
    }

    public List<T> getData() {
        return mData;
    }

    public void appendData(List<T> data) {
        if (!CollectionUtil.isEmpty(data)) {
            int position = mData.size();
            mData.addAll(data);
            notifyItemInserted(position);
        }
    }

    public void insertFront(List<T> data) {
        if (!CollectionUtil.isEmpty(data)) {
            mData.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    public void setData(List<T> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public T getLastItem() {
        if (!CollectionUtil.isEmpty(mData)) {
            return mData.get(mData.size() - 1);
        }
        return null;
    }

    public void setEmptyView(@LayoutRes int emptyId) {
        mEmptyView = inflate(emptyId);
    }

    public void setLoadingFooter(@LayoutRes int loadingId) {
        mLoadingFooter = inflate(loadingId);
        replaceFooter(mLoadingFooter);
    }

    public void setEndFooter(@LayoutRes int endID) {
        mEndFooter = inflate(endID);
        replaceFooter(mEndFooter);
    }

    public void setFailedFooter(@LayoutRes int failedId) {
        final View failedView = inflate(failedId);
        if (failedView != null) {
            failedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFooter(mLoadingFooter);
                    mLoadMoreListener.onLoadMore(true);
                }
            });
            replaceFooter(failedView);
        }
    }

    private void replaceFooter(View footer) {
        if (footer == null) {
            return;
        }
        if (mFooterLayout == null) {
            mFooterLayout = new FrameLayout(mContext);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.removeAllViews();
        mFooterLayout.addView(footer, params);
    }

    protected final View inflate(@LayoutRes int layoutId) {
        if (layoutId > 0) {
            return LayoutInflater.from(mContext).inflate(layoutId, null);
        }
        return null;
    }

    protected final View inflate(@LayoutRes int layoutId, ViewGroup parent) {
        if (layoutId > 0) {
            return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        }
        return null;
    }
}
