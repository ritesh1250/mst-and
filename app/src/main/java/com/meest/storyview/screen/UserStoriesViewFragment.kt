package  com.dbm.meest4bharat.storyview.screen


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dbm.meest4bharat.storyview.data.Story
import com.dbm.meest4bharat.storyview.data.StoryUser
import com.dbm.meest4bharat.storyview.utils.OnSwipeTouchListener
import com.dbm.meest4bharat.storyview.utils.hide
import com.dbm.meest4bharat.storyview.utils.show
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.DefaultRenderersFactory.ExtensionRendererMode
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.meest.Fragments.HomeFragments.userOwnStoryResponse
import com.meest.Meeast
import com.meest.Paramaters.UserDeleteStoryParameter
import com.meest.R
import com.meest.responses.DeleteStoryResponse
import com.meest.meestbhart.utilss.ApiUtils
import com.meest.meestbhart.utilss.SharedPrefreances
import com.meest.meestbhart.utilss.WebApi
import com.meest.storyview.customview.CustomProgressiveStoryView
import com.meest.storyview.screen.StoryActivity
import kotlinx.android.synthetic.main.fragment_story_display.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserStoriesViewFragment : Fragment(),CustomProgressiveStoryView.StoriesListener {


    private val position: Int by
    lazy { arguments?.getInt(EXTRA_POSITION) ?: 0 }

    private val isUSer: Boolean by
    lazy { arguments?.getBoolean(IS_USER) ?: false }

    private val storyUser: StoryUser by
    lazy {
        (arguments?.getParcelable<StoryUser>(
                EXTRA_STORY_USER
        ) as StoryUser)
    }

    private val stories: ArrayList<Story> by
    lazy { storyUser.stories }

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var userStoryViewControls: UserStoryViewControls? = null
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L
    private var onResumeCalled = false
    private var onVideoPrepared = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_display, container, false)
    }

    var circularProgressDrawable: CircularProgressDrawable? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyDisplayVideo.useController = false
        circularProgressDrawable = CircularProgressDrawable(requireActivity())
        circularProgressDrawable!!.strokeWidth = 5f
        circularProgressDrawable!!.centerRadius = 30f
        circularProgressDrawable!!.start()
        updateStory()
        setUpUi()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.userStoryViewControls = context as UserStoryViewControls
    }

    override fun onStart() {
        super.onStart()
        counter = restorePosition()
    }

    override fun onResume() {
        super.onResume()
        onResumeCalled = true
        try {
            if (stories[counter].isVideo() && !onVideoPrepared) {
                simpleExoPlayer?.playWhenReady = false
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        simpleExoPlayer?.seekTo(5)
        simpleExoPlayer?.playWhenReady = true
        if (counter == 0) {
            storiesProgressView?.startStories()
        } else {
            // restart animation
            counter = StoryActivity.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
            storiesProgressView?.startStories(counter)
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
        storiesProgressView?.abandon()
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        userStoryViewControls?.nextPageView()
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        --counter
        savePosition(counter)
        updateStory()
    }



    override fun onNext() {
        if (stories.size <= counter + 1) {
            return
        }
        ++counter
        savePosition(counter)
        updateStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleExoPlayer?.release()
    }

    private fun updateStory() {
        simpleExoPlayer?.stop()
        if (stories[counter].isVideo()) {
            storyDisplayVideo.show()
            storyDisplayImage.hide()
            storyDisplayVideoProgress.show()
            initializePlayer()
        } else {
            storyDisplayVideo.hide()
            storyDisplayVideoProgress.hide()
            storyDisplayImage.show()
            Handler().post({
                pressTime = System.currentTimeMillis()
                pauseCurrentStory()
            })
            Glide.with(this).load(stories[counter].url)
                    .placeholder(circularProgressDrawable)
                    .listener(object : RequestListener<Drawable?> {


                        override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                            Handler().post({
                                showStoryOverlay()
                                resumeCurrentStory()
                            })
                            return false; }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                            Handler().post({
                                showStoryOverlay()
                                resumeCurrentStory()
                            })
                            return false; }
                    })
                    .into(storyDisplayImage)
        }

//        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
//            timeInMillis = stories[counter].storyDate
//        }
//        storyDisplayTime.text = DateFormat.format("MM-dd-yyyy HH:mm:ss", stories[counter].storyDate).toString()
        storyDisplayTime.text = stories[counter].storyDate
    }



    private fun initializePlayer() {
        // playing video
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory(context?.let { Util.getUserAgent(it, Util.getUserAgent(requireContext(), getString(R.string.app_name))) })
        val loadControlnew: LoadControl = DefaultLoadControl.Builder().setBufferDurationsMs(25000, 50000, 1500, 2000).createDefaultLoadControl()
        @ExtensionRendererMode val extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
        val renderersFactorynew: RenderersFactory = DefaultRenderersFactory(requireContext()).setExtensionRendererMode(extensionRendererMode)

        mediaDataSourceFactory = CacheDataSourceFactory(Meeast.simpleCache, dataSourceFactory)
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(stories[counter].url))

        if (simpleExoPlayer == null) {
            simpleExoPlayer = SimpleExoPlayer.Builder(requireContext(), renderersFactorynew).setLoadControl(loadControlnew).build()
        } else {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
            simpleExoPlayer = SimpleExoPlayer.Builder(requireContext(), renderersFactorynew).setLoadControl(loadControlnew).build()
        }
        simpleExoPlayer?.prepare(mediaSource, false, false)
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
        }

        storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        storyDisplayVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {

                storyDisplayVideoProgress.hide()
                if (counter == stories.size.minus(1)) {
                    userStoryViewControls?.nextPageView()
                } else {
                    storiesProgressView?.skip()
                }
            }

            override fun onLoadingChanged(isLoading: Boolean) {

                if (isLoading) {
                    storyDisplayVideoProgress.show()
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                } else {
                    storyDisplayVideoProgress.hide()
                    storiesProgressView?.getProgressWithIndex(counter)
                            ?.setDuration(simpleExoPlayer?.duration ?: 8000L)
                    onVideoPrepared = true
                    resumeCurrentStory()
                }
            }
        })
    }

    var userMenuDialog: AlertDialog? = null
    var sendFeedbackConfirmationAlertDialog: AlertDialog? = null
    private fun showMenuDialog() {
        try {
            pauseCurrentStory()
            val builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)
            val viewGroup: ViewGroup = requireActivity().findViewById<ViewGroup>(android.R.id.content)
            val dialogView = LayoutInflater.from(activity).inflate(R.layout.custom_story_dialog, viewGroup, false)
            val headerText = dialogView.findViewById<TextView>(R.id.headerText)
            headerText.setOnClickListener {
                showDeleteConfirmationDialog()
            }
            builder.setView(dialogView)
            userMenuDialog = builder.create()
            userMenuDialog!!.setCancelable(true)
            userMenuDialog!!.show()


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun showDeleteConfirmationDialog() {
        try {
            val builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)
            val viewGroup: ViewGroup = requireActivity().findViewById<ViewGroup>(android.R.id.content)
            val dialogView = LayoutInflater.from(activity).inflate(R.layout.custom_dialog_delete_confirm, viewGroup, false)
            val okBtn = dialogView.findViewById<Button>(R.id.okBtn)
            val cancelBtn = dialogView.findViewById<Button>(R.id.cancelBtn)
            okBtn.setOnClickListener({
                deleteUserStory(storyUser.stories[counter].id)
            })
            cancelBtn.setOnClickListener({
                resumeCurrentStory()
                userMenuDialog!!.dismiss()
                sendFeedbackConfirmationAlertDialog!!.dismiss()
            })
            builder.setView(dialogView)
            sendFeedbackConfirmationAlertDialog = builder.create()
            sendFeedbackConfirmationAlertDialog!!.setCancelable(false)
            sendFeedbackConfirmationAlertDialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteUserStory(storyID: String) {
        try {
            val webApi = ApiUtils.getClient().create(WebApi::class.java)
            val header: MutableMap<String, String> = HashMap()
            header["x-token"] = SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN)
            //            header.put("x-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjNmZmI0MmQ5LWUxYjQtNGU0ZC04NzdjLTYwODRjMGZhMzYwNSIsImVtYWlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMC0xMC0xMlQxNDowMzoyMC41MTRaIn0sImlhdCI6MTYwMjUxMTQwMH0.zVS3c5B24b-r2PU6hYPANWwavs409IuLXPNPbcMLWdM");
            val paramter = UserDeleteStoryParameter(storyID)
            val call = webApi.deleteUserStory(header, paramter)
            call.enqueue(object : Callback<DeleteStoryResponse> {
                override fun onFailure(call: Call<DeleteStoryResponse>, t: Throwable) {

                }

                override fun onResponse(call: Call<DeleteStoryResponse>, response: Response<DeleteStoryResponse>) {
                    if (userOwnStoryResponse.data.rows.get(0).allStories.size > 0)
                        userOwnStoryResponse.data.rows.get(0).allStories.removeAt(counter)
                    Toast.makeText(activity!!, activity!!.resources.getString(R.string.story_deleted_success), Toast.LENGTH_SHORT).show()
                    activity!!.setResult(Activity.RESULT_OK)
                    activity!!.finish()
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }



    private fun setUpUi() {
        if (isUSer) {
            moreIV.visibility = View.VISIBLE
        } else {
            moreIV.visibility = View.GONE
        }
        moreIV.setOnClickListener({
            showMenuDialog()
        })
        val touchListener = object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
//                Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                try {
//                    Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
                    activity!!.finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onClick(view: View) {
                when (view) {
                    next -> {
                        if (counter == stories.size - 1) {
                            userStoryViewControls?.nextPageView()
                        } else {
                            storiesProgressView?.skip()
                        }
                    }
                    previous -> {
                        if (counter == 0) {
                            userStoryViewControls?.backPageView()
                        } else {
                            storiesProgressView?.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                hideStoryOverlay()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        showStoryOverlay()
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }
        previous.setOnTouchListener(touchListener)
        next.setOnTouchListener(touchListener)

        storiesProgressView?.setStoriesCountDebug(
                stories.size, position = arguments?.getInt(EXTRA_POSITION) ?: -1
        )
        storiesProgressView?.setAllStoryDuration(4000L)
        storiesProgressView?.setStoriesListener(this)

        Glide.with(this).load(storyUser.profilePicUrl).circleCrop().into(storyDisplayProfilePicture)
        storyDisplayNick.text = storyUser.username
    }


    private fun playPauseStory(play: Boolean) {
        if (play) {
            pressTime = System.currentTimeMillis()
            pauseCurrentStory()
        } else {
            showStoryOverlay()
            resumeCurrentStory()
        }

    }

    private fun showStoryOverlay() {
        if (storyOverlay == null || storyOverlay.alpha != 0F) return

        storyOverlay.animate()
                .setDuration(100)
                .alpha(1F)
                .start()
    }

    private fun hideStoryOverlay() {
        if (storyOverlay == null || storyOverlay.alpha != 1F) return

        storyOverlay.animate()
                .setDuration(200)
                .alpha(0F)
                .start()
    }

    private fun savePosition(pos: Int) {
        StoryActivity.progressState.put(position, pos)
    }

    private fun restorePosition(): Int {
        return StoryActivity.progressState.get(position)
    }

    fun pauseCurrentStory() {
        simpleExoPlayer?.playWhenReady = false
        storiesProgressView?.pause()
    }

    fun resumeCurrentStory() {
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
            showStoryOverlay()
            storiesProgressView?.resume()
        }
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
        private const val IS_USER = "IS_USER"
        fun newInstance(position: Int, story: StoryUser, isUser: Boolean): UserStoriesViewFragment {
            return UserStoriesViewFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_USER, isUser)
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_STORY_USER, story)
                }
            }
        }
    }
}