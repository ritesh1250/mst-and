package  com.dbm.meest4bharat.storyview.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(val url: String, val storyDate: String, val id: String) : Parcelable {

    fun isVideo() =  url.contains(".mp4")
}