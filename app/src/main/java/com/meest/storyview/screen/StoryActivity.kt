package  com.meest.storyview.screen

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.bumptech.glide.Glide
import com.dbm.meest4bharat.storyview.customview.UserStoriesPagerAdapter
import com.dbm.meest4bharat.storyview.data.Story
import com.dbm.meest4bharat.storyview.data.StoryUser
import com.dbm.meest4bharat.storyview.screen.StoriesPageChangeListener
import com.dbm.meest4bharat.storyview.screen.UserStoriesViewFragment
import com.dbm.meest4bharat.storyview.screen.UserStoryViewControls
import com.dbm.meest4bharat.storyview.utils.CubeOutTransformer
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheUtil
import com.google.android.exoplayer2.util.Util
import com.meest.Fragments.HomeFragments.BUNDLE
import com.meest.Meeast
import com.meest.R
import com.meest.responses.UserFollowerStoryResponse
import kotlinx.android.synthetic.main.activity_notification_social_feed.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.mutableListOf

class StoryActivity : AppCompatActivity(), UserStoryViewControls {

    private lateinit var pagerAdapter: UserStoriesPagerAdapter
    private var currentPage: Int = 0
    var userStoryListResponse: UserFollowerStoryResponse? = null
    var position: Int = 0
    var isUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_)
        progressState.clear()
        val intent: Intent = intent
        val args: Bundle? = intent.getBundleExtra(BUNDLE)
        userStoryListResponse = args!!.getSerializable("StoryResponse") as UserFollowerStoryResponse
        position = intent.getIntExtra("position", 0)
        isUser = intent.hasExtra("iSUser")
        setUpPager()
    }

    /*   private fun updateOwnStory() {
        try {
            val webApi = ApiUtils.getClient().create(WebApi::class.java)
            val header: MutableMap<String, String> = HashMap()
            header["x-token"] = SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN)
            //            header.put("x-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjNmZmI0MmQ5LWUxYjQtNGU0ZC04NzdjLTYwODRjMGZhMzYwNSIsImVtYWlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMC0xMC0xMlQxNDowMzoyMC41MTRaIn0sImlhdCI6MTYwMjUxMTQwMH0.zVS3c5B24b-r2PU6hYPANWwavs409IuLXPNPbcMLWdM");
            val paramter = UserStoryParameter(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID))
            val call = webApi.getOwnStories(header, paramter)
            call.enqueue(object : Callback<UserFollowerStoryResponse> {
                override fun onFailure(call: Call<UserFollowerStoryResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<UserFollowerStoryResponse>, response: Response<UserFollowerStoryResponse>) {
                    userStoryListResponse = response.body().Data()
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }*/
    override fun backPageView() {
        if (viewPager.currentItem > 0) {
            try {
                fakeDrag(false)
            } catch (e: Exception) {
                //NO OP
            }
        }
    }

    override fun nextPageView() {
        if (viewPager.currentItem + 1 < viewPager.adapter?.count ?: 0) {
            try {
                fakeDrag(true)
            } catch (e: Exception) {
                //NO OP
            }
        } else {
//            Toast.makeText(this, "All stories displayed.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun createStoryList(): ArrayList<StoryUser> {
        val storyUserList = ArrayList<StoryUser>()
        try {
            var userRows = userStoryListResponse!!.data.rows
            for (i in 0 until userRows.size) {
                val stories = ArrayList<Story>()
                for (j in 0 until userRows[i].allStories.size) {
                    stories.add(
                        Story(
                            userRows[i].allStories[j].story,
                            userRows[i].allStories[j].createdAt,
                            userRows[i].allStories[j].id
                        )
                    )
                }

                storyUserList.add(
                    StoryUser(
                        userRows[i].user.username,
                        userRows[i].user.displayPicture,
                        stories
                    )
                )
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return storyUserList
    }

    private fun setUpPager() {
        val storyUserList = createStoryList()
        if (storyUserList.size == 0) {

            return;
        }
        preLoadStories(storyUserList)

        pagerAdapter = UserStoriesPagerAdapter(
            supportFragmentManager,
            storyUserList,
            isUser
        )
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = position
        viewPager.setPageTransformer(
            true,
            CubeOutTransformer()
        )
        viewPager.addOnPageChangeListener(object : StoriesPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }

            override fun onPageScrollCanceled() {
                currentFragment()?.resumeCurrentStory()
            }
        })
    }

    private fun preLoadStories(storyUserList: ArrayList<StoryUser>) {
        val imageList = mutableListOf<String>()
        val videoList = mutableListOf<String>()

        storyUserList.forEach { storyUser ->
            storyUser.stories.forEach { story ->
                if (story.isVideo()) {
                    videoList.add(story.url)
                } else {
                    imageList.add(story.url)
                }
            }
        }
        preLoadVideos(videoList)
        preLoadImages(imageList)
    }

    private fun preLoadVideos(videoList: MutableList<String>) {
        videoList.map { data ->
            GlobalScope.async {
                val dataUri = Uri.parse(data)
                val dataSpec = DataSpec(dataUri, 0, 500 * 1024, null)
                val dataSource: DataSource =
                    DefaultDataSourceFactory(
                        applicationContext,
                        Util.getUserAgent(applicationContext, getString(R.string.app_name))
                    ).createDataSource()

                val listener =
                    CacheUtil.ProgressListener { requestLength: Long, bytesCached: Long, _: Long ->
                        val downloadPercentage = (bytesCached * 100.0
                                / requestLength)
                        Log.d("preLoadVideos", "downloadPercentage: $downloadPercentage")
                    }

                try {
                    CacheUtil.cache(
                        dataSpec,
                        Meeast.simpleCache,
                        CacheUtil.DEFAULT_CACHE_KEY_FACTORY,
                        dataSource,
                        listener,
                        null
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun preLoadImages(imageList: MutableList<String>) {
        imageList.forEach { imageStory ->
            Glide.with(this).load(imageStory).preload()
        }
    }

    private fun currentFragment(): UserStoriesViewFragment? {
        return pagerAdapter.findFragmentByPosition(
            viewPager,
            currentPage
        ) as UserStoriesViewFragment
    }

//    private fun currentFragment(): CustomStoryFragment? {
//        return pagerAdapter.findFragmentByPosition(viewPager, currentPage) as CustomStoryFragment
//    }

    /**
     * Change ViewPage sliding programmatically(not using reflection).
     * https://tech.dely.jp/entry/2018/12/13/110000
     * What for?
     * setCurrentItem(int, boolean) changes too fast. And it cannot set animation duration.
     */
    private var prevDragPosition = 0

    private fun fakeDrag(forward: Boolean) {
        if (prevDragPosition == 0 && viewPager.beginFakeDrag()) {
            ValueAnimator.ofInt(0, viewPager.width).apply {
                duration = 400L
                interpolator = FastOutSlowInInterpolator()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        removeAllUpdateListeners()
                        if (viewPager.isFakeDragging) {
                            viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        removeAllUpdateListeners()
                        if (viewPager.isFakeDragging) {
                            viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationStart(p0: Animator?) {}
                })
                addUpdateListener {
                    if (!viewPager.isFakeDragging) return@addUpdateListener
                    val dragPosition: Int = it.animatedValue as Int
                    val dragOffset: Float =
                        ((dragPosition - prevDragPosition) * if (forward) -1 else 1).toFloat()
                    prevDragPosition = dragPosition
                    viewPager.fakeDragBy(dragOffset)
                }
            }.start()
        }
    }

    companion object {
        val progressState = SparseIntArray()
    }
}
