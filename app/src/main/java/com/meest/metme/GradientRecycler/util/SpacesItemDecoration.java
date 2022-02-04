package com.meest.metme.GradientRecycler.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        this.space = outRect.left;
        this.space = outRect.right;
        this.space = outRect.bottom;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = this.space;
        }

    }

    public SpacesItemDecoration(int space) {
        this.space = space;
    }
}
