package com.meest.videomvvmmodule.model.videos;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileVideo {
    private List<ProfileVideo.Data> data;
    private String message;
    private String count;
    private boolean status;
    private int post_count;
    @SerializedName("sound_data")
    private ProfileVideo.SoundData soundData;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ProfileVideo.SoundData getSoundData() {
        return soundData;
    }

    public void setSoundData(ProfileVideo.SoundData soundData) {
        this.soundData = soundData;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<ProfileVideo.Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public class Data {

        @SerializedName("post_image")
        private String postImage;
        @SerializedName("singer")
        private String singer;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("sound")
        private String sound;
        @SerializedName("post_video")
        private String postVideo;
        @SerializedName("sound_image")
        private String soundImage;
        @SerializedName("user_profile")
        private String userProfile;
        @SerializedName("post_description")
        private String postDescription;
        @SerializedName("duration")
        private String duration;
        @SerializedName("post_likes_count")
        private String postLikesCount;
        @SerializedName("post_comments_count")
        private int postCommentsCount;
        @SerializedName("video_likes_or_not")
        private int videoIsLiked;
        @SerializedName("full_name")
        private String fullName;
        @SerializedName("post_id")
        private String postId;
        @SerializedName("post_hash_tag")
        private String postHashTag;
        @SerializedName("sound_title")
        private String soundTitle;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("sound_id")
        private String soundId;
        @SerializedName("post_view_count")
        private Integer postViewCount;
        @SerializedName("created_date")
        private String createdDate;
        @SerializedName("status")
        private String status;
        @SerializedName("is_verify")
        private String isVerify;
        @SerializedName("can_comment")
        private String canComment;
        @SerializedName("can_duet")
        private String canDuet;
        @SerializedName("can_save")
        private String canSave;
        @SerializedName("follow_or_not")
        private String followOrNot;
        @SerializedName("profile_category_id")
        private String profileCategoryId;
        @SerializedName("profile_category_name")
        private String profileCategoryName;
        @SerializedName("thumbnail_image")
        private String thumbnail_image;
        private Object player;

        public String getThumbnail_image() {
            return thumbnail_image;
        }

        public void setThumbnail_image(String thumbnail_image) {
            this.thumbnail_image = thumbnail_image;
        }

        public String getProfileCategoryId() {
            return profileCategoryId;
        }

        public void setProfileCategoryId(String profileCategoryId) {
            this.profileCategoryId = profileCategoryId;
        }

        public String getProfileCategoryName() {
            return profileCategoryName;
        }

        public void setProfileCategoryName(String profileCategoryName) {
            this.profileCategoryName = profileCategoryName;
        }

        public String getCanComment() {
            return canComment;
        }

        public void setCanComment(String canComment) {
            this.canComment = canComment;
        }

        public String getCanDuet() {
            return canDuet;
        }

        public void setCanDuet(String canDuet) {
            this.canDuet = canDuet;
        }

        public String getCanSave() {
            return canSave;
        }

        public void setCanSave(String canSave) {
            this.canSave = canSave;
        }

        public boolean getFollowOrNot() {
            return followOrNot.equals("1");
        }

        public void setFollowOrNot(String followOrNot) {
            this.followOrNot = followOrNot;
        }

        public SimpleExoPlayer getPlayer() {
            return (SimpleExoPlayer) player;
        }

        public void setPlayer(SimpleExoPlayer player) {
            this.player = player;
        }

        public boolean isVerified() {
            if(isVerify!=null){
                return isVerify.equals("1");
            }else {
                return  false;
            }
        }

        public void setIsVerify(String isVerify) {
            this.isVerify = isVerify;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public String getSinger() {
            return singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getPostVideo() {
            return postVideo;
        }

        public void setPostVideo(String postVideo) {
            this.postVideo = postVideo;
        }

        public String getSoundImage() {
            return soundImage;
        }

        public void setSoundImage(String soundImage) {
            this.soundImage = soundImage;
        }

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }

        public String getPostDescription() {
            return postDescription;
        }

        public void setPostDescription(String postDescription) {
            this.postDescription = postDescription;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getPostLikesCount() {
            return postLikesCount == null || postLikesCount.isEmpty() ? "0" : postLikesCount;
        }

        public void setPostLikesCount(String postLikesCount) {
            this.postLikesCount = postLikesCount;
        }

        public int getDummyLikeCount() {
            return Integer.parseInt(postLikesCount);
        }

        public void setDummyLikeCount(int likeCount) {
            this.postLikesCount = String.valueOf(likeCount);
        }

        public int getPostCommentsCount() {
            return postCommentsCount;
        }

        public void setPostCommentsCount(int postCommentsCount) {
            this.postCommentsCount = postCommentsCount;
        }

        public boolean getVideoIsLiked() {
            return videoIsLiked == 1;
        }

        public void setVideoIsLiked(int videoIsLiked) {
            this.videoIsLiked = videoIsLiked;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getPostHashTag() {
            return postHashTag;
        }

        public void setPostHashTag(String postHashTag) {
            this.postHashTag = postHashTag;
        }

        public String getSoundTitle() {
            return soundTitle;
        }

        public void setSoundTitle(String soundTitle) {
            this.soundTitle = soundTitle;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSoundId() {
            return soundId;
        }

        public void setSoundId(String soundId) {
            this.soundId = soundId;
        }

        public Integer getPostViewCount() {
//            return Global.prettyCount(Long.parseLong(postViewCount));
            return postViewCount;
        }

        public void setPostViewCount(Integer postViewCount) {
            this.postViewCount = postViewCount;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class SoundData {

        @SerializedName("added_by")
        private String addedBy;
        @Expose
        private String duration;
        @Expose
        private String singer;
        @Expose
        private String sound;
        @SerializedName("sound_category_id")
        private String soundCategoryId;
        @SerializedName("sound_id")
        private String soundId;
        @SerializedName("sound_image")
        private String soundImage;
        @SerializedName("sound_title")
        private String soundTitle;
        @Expose
        private String status;
        @SerializedName("post_video_count")
        private int postVideoCount;


        public int getPostVideoCount() {
            return postVideoCount;
        }

        public void setPostVideoCount(int postVideoCount) {
            this.postVideoCount = postVideoCount;
        }

        public String getAddedBy() {
            return addedBy;
        }

        public void setAddedBy(String addedBy) {
            this.addedBy = addedBy;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getSinger() {
            return singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getSoundCategoryId() {
            return soundCategoryId;
        }

        public void setSoundCategoryId(String soundCategoryId) {
            this.soundCategoryId = soundCategoryId;
        }

        public String getSoundId() {
            return soundId;
        }

        public void setSoundId(String soundId) {
            this.soundId = soundId;
        }

        public String getSoundImage() {
            return soundImage;
        }

        public void setSoundImage(String soundImage) {
            this.soundImage = soundImage;
        }

        public String getSoundTitle() {
            return soundTitle;
        }

        public void setSoundTitle(String soundTitle) {
            this.soundTitle = soundTitle;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
