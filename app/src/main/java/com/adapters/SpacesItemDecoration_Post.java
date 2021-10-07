package com.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class SpacesItemDecoration_Post extends ItemDecoration {
    private int space;

    public SpacesItemDecoration_Post(int space) {
        this.space = space;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        outRect.left = this.space / 2;
        outRect.right = this.space / 2;
        outRect.bottom = this.space;
        outRect.top = this.space;
    }
}
