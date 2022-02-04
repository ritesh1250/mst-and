package com.meest.metme.swipetoreply;

import androidx.recyclerview.widget.RecyclerView;

public interface ISwipeControllerActions {

    void onSwipePerformed(int position, RecyclerView.ViewHolder viewHolder);

}
