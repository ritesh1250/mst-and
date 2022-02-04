package com.meest.videomvvmmodule.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.ActivityPreviewBinding;
import com.meest.videomvvmmodule.model.user.UploadImageResponse;
import com.meest.videomvvmmodule.model.videos.AddPostResponse;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.model.videos.UploadVideoThumbnailResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.GlobalApi;
import com.meest.videomvvmmodule.utils.ProgressRequestBody;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.preview.PreviewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PreviewViewModel extends ViewModel {
    public String postDescription = "", hashTag = "";
    public String videoPath = " ", videoThumbnail = "", soundImage = "", soundPath = "", soundId = "";
    public String videoUrl = "";
    public String thumbnailUrl = "";
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<String> onClickUpload = new MutableLiveData<>();
    public ObservableInt position = new ObservableInt(0);
    public SessionManager sessionManager;
    public ArrayList<String> videoPaths = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    public String canComment = "0";
    public int canSave = 1, canDuet = 1;
    public ObservableBoolean postUpload = new ObservableBoolean(false);
    public boolean postSuccess = false;

    public void setOnItemClick(int type) {

        onItemClick.setValue(type);
    }

    public void onDescriptionTextChanged(CharSequence text) {
        postDescription = text.toString();
        position.set(postDescription.length());
    }

    public void fetchVideoUrl() {
        onClickUpload.setValue("hash");

//        video multipart
        MultipartBody.Part body;
        File file = new File(videoPath);
        ProgressRequestBody requestBody = new ProgressRequestBody(file, percentage -> {

        });
        body = MultipartBody.Part.createFormData("video", file.getName(), requestBody);

        MultipartBody.Part body1;
        File file1 = new File(videoThumbnail);
        ProgressRequestBody requestBody1 = new ProgressRequestBody(file1, percentage -> {

        });
        body1 = MultipartBody.Part.createFormData("thumbnail", file1.getName(), requestBody1);

        disposable.add(Global.initRetrofit().getVideoAndThumbnailUrl(Global.apikey, body, body1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((UploadVideoThumbnailResponse updateUser, Throwable throwable) -> {
                    if (updateUser.getStatus() != null) {
                        videoUrl = updateUser.getData().getVideo_url();
                        thumbnailUrl = updateUser.getData().getThumbnail_path();
                        Log.d("video_url", "===============================================" + videoUrl);
//                        fetchImageUrl();
                        uploadPost();
                    }
                }));
    }

    public void getThumbnailUrl() {
        MultipartBody.Part body = null;
        if (videoThumbnail != null && !videoThumbnail.isEmpty()) {
            File file = new File(videoThumbnail);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((uploadImageResponse, throwable) -> {
                        if (uploadImageResponse != null && uploadImageResponse.getStatus()) {
                            thumbnailUrl = uploadImageResponse.getData();
                            Log.e("Profile_Url", "===============================================" + thumbnailUrl);
                        }
                    }));
        }
    }

//    public void fetchImageUrl(){
//        onClickUpload.setValue("hash");
////        thumbnail
//        File file1 = new File(videoThumbnail);
//        ProgressRequestBody requestBody2 = new ProgressRequestBody(file1, percentage -> {
//
//        });
//
//        MultipartBody.Part body1 = MultipartBody.Part.createFormData("file", file1.getName(), requestBody2);
//
//        disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey,body1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
//                .doOnTerminate(() -> isLoading.setValue(false))
//                .subscribe((UploadImageResponse updateUser, Throwable throwable) -> {
//                    if(updateUser.getStatus() != null){
//                        thumbnailUrl = updateUser.getData();
//                        Log.d("thumbnail_url","==============================================="+thumbnailUrl);
//                        uploadPost();
//                    }
//                }));
//    }

    public void uploadPost() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("post_description", postDescription);
        hashMap.put("post_hash_tag", hashTag);
        hashMap.put("can_comment", canComment);
        hashMap.put("can_save", canSave);
        hashMap.put("can_duet", canDuet);
        hashMap.put("user_id", Global.userId);
        hashMap.put("post_video", videoUrl);
        hashMap.put("thumbnail_image", thumbnailUrl);
        hashMap.put("is_trending", "1");
        hashMap.put("is_orignal_sound", soundPath == null || soundPath.isEmpty() ? "0" : "1");
        Log.d("video_url", "===============================================" + videoUrl);
        if (soundId != null && !soundId.isEmpty()) {
            hashMap.put("sound_id", soundId);
        } else {
            hashMap.put("sound_title", "Original sound by " + sessionManager.getUser().getData().getUserName());
            hashMap.put("duration", "1:00");
            hashMap.put("singer", sessionManager.getUser().getData().getUserName());
        }

/*        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("post_description", toRequestBody(postDescription));
        hashMap.put("post_hash_tag", toRequestBody(hashTag));
        hashMap.put("can_comment", toRequestBody(canComment));
        hashMap.put("can_save", toRequestBody(canSave));
        hashMap.put("can_duet", toRequestBody(canDuet));
        hashMap.put("user_id", toRequestBody(Global.userId));
        hashMap.put("post_video", toRequestBody("http://medley.s3.amazonaws.com/uploads/append.mp4"));
        hashMap.put("is_orignal_sound", toRequestBody(soundPath == null || soundPath.isEmpty() ? "0" : "1"));

        if (soundId != null && !soundId.isEmpty()) {
            hashMap.put("sound_id", toRequestBody(soundId));
        } else {
            hashMap.put("sound_title", toRequestBody("Original sound by " + sessionManager.getUser().getData().getUserName()));
            hashMap.put("duration", toRequestBody("1:00"));
            hashMap.put("singer", toRequestBody(sessionManager.getUser().getData().getUserName()));
        }*/
   /*     MultipartBody.Part body;

        File file = new File(videoPath);
        ProgressRequestBody requestBody = new ProgressRequestBody(file, percentage -> {

        });


        body = MultipartBody.Part.createFormData("post_video", file.getName(), requestBody);*/

    /*    File file1 = new File(videoThumbnail);
        ProgressRequestBody requestBody2 = new ProgressRequestBody(file1, percentage -> {

        });

        MultipartBody.Part body1 = MultipartBody.Part.createFormData("post_image", file1.getName(), requestBody2);*/
     /*   MultipartBody.Part body2 = null;
        MultipartBody.Part body3 = null;
        if (soundId == null || soundId.isEmpty()) {

            File file2 = new File(soundPath);
            RequestBody requestFile2 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file2);

            body2 = MultipartBody.Part.createFormData("post_sound", file2.getName(), requestFile2);

            if (soundImage != null) {
                File file3 = new File(soundImage);
                RequestBody requestFile3 =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file3);

                body3 = MultipartBody.Part.createFormData("sound_image", file1.getName(), requestFile3);
            }
        }*/
//        disposable.add(Global.initRetrofit().uploadPost(Global.apikey, Global.accessToken, hashMap, body, body1, body2, body3)
        disposable.add(Global.initRetrofit().uploadPost(Global.apikey, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((AddPostResponse updateUser, Throwable throwable) -> {
                    if (updateUser.getStatus()) {
//                        postSuccess=true;
                        new GlobalApi().rewardUser("3");
                    }
                }));
    }

    public RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

//    public void setIconColor(ActivityPreviewBinding binding, PreviewActivity previewActivity) {
//        binding.videoCutter.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.fastMotion.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.slowMotion.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.videoMixer.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.videoCrop.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.videoMirror.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.videoMute.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.videoRotate.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//        binding.videoReverse.setColorFilter(ContextCompat.getColor(previewActivity, R.color.white));
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
