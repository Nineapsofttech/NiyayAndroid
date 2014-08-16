package com.niyay.reader;

import android.widget.AbsListView;

/**
 * Created by APPLE on 1/10/14.
 */
public abstract class LoadMoreScrollListener implements AbsListView.OnScrollListener {

    private boolean loading = false;
    private Integer page = 1;

    public LoadMoreScrollListener() {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading && (totalItemCount == firstVisibleItem+visibleItemCount)){
            loading = false;
            onLoadMore(page++);
        }
    }

    public void ready(){
        loading = true;
    }

    public void reset(){
        page = 1;
        loading = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public abstract void onLoadMore(Integer page);
}