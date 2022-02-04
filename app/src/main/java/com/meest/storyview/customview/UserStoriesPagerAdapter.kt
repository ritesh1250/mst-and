package  com.dbm.meest4bharat.storyview.customview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dbm.meest4bharat.storyview.data.StoryUser
import com.dbm.meest4bharat.storyview.screen.UserStoriesViewFragment

class UserStoriesPagerAdapter constructor(fragmentManager: FragmentManager, private val storyList: ArrayList<StoryUser>, val isUser: Boolean)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = UserStoriesViewFragment.newInstance(position, storyList[position],isUser)

    override fun getCount(): Int {
        return storyList.size
    }

    fun findFragmentByPosition(viewPager: ViewPager, position: Int): Fragment? {
        try {
            val f = instantiateItem(viewPager, position)
            return f as? Fragment
        } finally {
            finishUpdate(viewPager)
        }
    }
}