package  com.dbm.meest4bharat.storyview.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryUser(val username: String, val profilePicUrl: String, val stories: ArrayList<Story>) : Parcelable