package com.meest.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.meest.BuildConfig
import com.meest.Meeast

import com.meest.Paramaters.TextPostUploadParam
import com.meest.responses.UploadBulkMediaResponse
import com.meest.mediapikcer.utils.PictureFacer
import com.meest.meestbhart.utilss.ApiUtils
import com.meest.meestbhart.utilss.SharedPrefreances
import com.meest.meestbhart.utilss.WebApi
import com.meest.model.MultipleStoryDataModel
import com.meest.videomvvmmodule.utils.Global
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.set

class UploadFilesToServer(selectList: MutableList<PictureFacer>, context: Context, uploadedData: MediaUploadInterface, requestCode: Int, isImage: Boolean) {
    var selectList: MutableList<PictureFacer>
    var context: Context? = null
    var uploadedData: MediaUploadInterface? = null
    var requestCode: Int = 0
    var isImage: Boolean = true

    init {
        this.selectList = selectList
        this.context = context
        this.uploadedData = uploadedData
        this.requestCode = requestCode
        this.isImage = isImage
        Log.e("UploadFilesToServer", selectList.toString())
    }

    val uiScope = CoroutineScope(Dispatchers.Main)

    fun startMediaUpload() {
        uiScope.launch {
            uploadMediaToServer(selectList)
        }
    }

    private fun uploadMediaToServer(mList: List<PictureFacer>) {
//        if (BuildConfig.DEBUG) {
//            baseUrl = SharedPrefreances.getSharedPreferenceString(Meeast.context(), SharedPrefreances.BASE_URL_DUMMY)
////            Log.e("BaseURL",baseUrl);
//        }
        try {
            val client = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(600, TimeUnit.SECONDS)
                    .writeTimeout(600, TimeUnit.SECONDS).build()
            val retrofit = Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
            val webApi = retrofit.create(WebApi::class.java)
            val map: MutableMap<String, String> = HashMap()
            map["x-token"] = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN)
            var requests = ArrayList<Observable<*>>()
            var uploadedMediaList = ArrayList<String>()
            var surveyImagesParts = mutableListOf<MultipartBody.Part>()
            var count: Int = 0
            if (isImage) {
                for (i in 0 until mList.size) {
                    val imageFile = File(mList[i].picturePath)
                    val requestFile: RequestBody
                    val body: MultipartBody.Part
                    requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                    body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                    surveyImagesParts.add(body)
                }
            } else {
                for (i in 0 until mList.size) {
                    val imageFile = File(mList[i].picturePath)
                    Log.e("UploadFilesToServer: ", mList[i].picturePath)
                    val requestFile: RequestBody
                    val body: MultipartBody.Part
                    requestFile = RequestBody.create("video/*".toMediaTypeOrNull(), imageFile)
                    body = MultipartBody.Part.createFormData("video", imageFile.name, requestFile)
                    surveyImagesParts.add(body)
                }
            }

            requests.add(webApi.uploadMedia(map, surveyImagesParts))
            Observable
                    .zip(requests) {
                        println(it)
                        var res = it[0] as UploadBulkMediaResponse

                        for (i in 0 until res.data.size) {
                            Log.e("Server Response", res.data[i].url)
                            uploadedMediaList.add(res.data[i].url)
                        }

                        if (requestCode == 100) {
                            uploadData(uploadedMediaList)
                        } else {
                            uploadedData!!.getUploadedMedia(requestCode, uploadedMediaList, isImage, mList)
//                            context!!.runOnUiThread(Runnable {
//
//                                uploadedData!!.getUploadedMedia(requestCode, uploadedMediaList, isImage, mList)
//                            })
                        }
                    }.observeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        println(it)

                    }) {
                        println(it)
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("CheckResult")
    fun uploadData(mList: List<String>) {
        try {
            var baseUrl = ApiUtils.BASE_URL
            if (BuildConfig.DEBUG) {
                baseUrl = SharedPrefreances.getSharedPreferenceString(Meeast.context(), SharedPrefreances.BASE_URL_DUMMY)
            }
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                .certificatePinner(ApiUtils.certPinnerMeest)
                .build()
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()

            val backendApi = retrofit.create(WebApi::class.java)
            val map: MutableMap<String, String> = HashMap()
            map["x-token"] = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN)
            val webApi = ApiUtils.getClient().create(WebApi::class.java)
            val requests = ArrayList<Observable<*>>()
            val hashTagList: List<String> = java.util.ArrayList()

            val storyDataModel = MultipleStoryDataModel()
            storyDataModel.caption = ""
            val loc = TextPostUploadParam.Location()
            storyDataModel.location = loc
            loc.lat = 0.0
            loc.long = 0.0
            storyDataModel.hashTags = hashTagList
            storyDataModel.story = mList
            storyDataModel.status = true
            storyDataModel.image = false



            requests.add(backendApi.insertUserStory(map, storyDataModel))

            Observable.zip(requests) {
                uploadedData!!.getUploadedMedia()
//                context!!.runOnUiThread(Runnable {
//                    uploadedData!!.getUploadedMedia()
//                })
            }.subscribe({// Will be triggered if all requests will end successfully (4xx and 5xx also are successful requests too)
                //Do something on successful completion of all requests
            }) {
                uploadedData!!.getUploadedMedia()
//                context!!.runOnUiThread(Runnable {
//                    uploadedData!!.getUploadedMedia()
//                })
                //Do something on error completion of requests
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}