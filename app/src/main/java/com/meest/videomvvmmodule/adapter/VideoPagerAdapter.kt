package com.meest.videomvvmmodule.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.meest.videomvvmmodule.model.videos.Video
import com.meest.videomvvmmodule.view.home.AdsFragment
import com.meest.videomvvmmodule.view.home.PlayerFragment
import java.util.*

class VideoPagerAdapter(
    fragment: Fragment,
    val onCallApi: OnCallApi?,
    val onRecyclerViewItemClick: VideoFullAdapter.OnRecyclerViewItemClick?
) : FragmentStateAdapter(fragment) {

    var dataList: ArrayList<Video.Data?> = ArrayList()
    val pageIds= dataList.map {
        it.hashCode().toLong()
    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        if (dataList[position] == null) {
            return AdsFragment()
        }
        if (position + 3 > dataList.size) {
            onCallApi?.callApi()
        }
        return PlayerFragment.newInstance(dataList[position], onRecyclerViewItemClick,position)
    }

    fun updateData(dataList: java.util.ArrayList<Video.Data?>) {
        this.dataList.clear()
        this.dataList = dataList
//        dataList.add(null)
        notifyDataSetChanged()
    }

    fun loadMore(data: List<Video.Data?>) {
//        val lengthOfNewData = data.size
//        val startIndex = dataList.size
        dataList.addAll(data)
//        dataList.add(null)
        notifyDataSetChanged()
//        notifyItemRangeChanged(startIndex, lengthOfNewData)
    }

   /* fun removeFragment(position: Int) {
        dataList.removeAt(position)
        notifyItemRangeChanged(position, dataList.size)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return dataList[position].hashCode().toLong() // make sure notifyDataSetChanged() works
    }

    override fun containsItem(itemId: Long): Boolean {
        return pageIds.contains(itemId)
    }*/

    interface OnCallApi {
        fun callApi()
    }

}