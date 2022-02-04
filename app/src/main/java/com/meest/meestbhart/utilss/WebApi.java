package com.meest.meestbhart.utilss;

import com.google.gson.JsonObject;
import com.meest.Paramaters.AboutRequestParameter;
import com.meest.Paramaters.BlockUserParameters;
import com.meest.Paramaters.CommentDisLikeParam;
import com.meest.Paramaters.CommentLikeParam;
import com.meest.Paramaters.FAQParametr;
import com.meest.Paramaters.FollowPeoplesParam;
import com.meest.Paramaters.InsertLikeParameters;
import com.meest.Paramaters.InsightGraphParameter;
import com.meest.Paramaters.InsightsParameter;
import com.meest.Paramaters.RemoveBlockedUserParameter;
import com.meest.Paramaters.SearchHomeParam;
import com.meest.Paramaters.TextPostUploadParam;
import com.meest.Paramaters.UnFollowPeoplesParam;
import com.meest.Paramaters.UpdateChatSettingParam;
import com.meest.Paramaters.UpdatePasswordParams;
import com.meest.Paramaters.UpdateTopicsParams;
import com.meest.Paramaters.UserDeleteStoryParameter;
import com.meest.Paramaters.UserFeedbackParameter;
import com.meest.Paramaters.UserSettingParams;
import com.meest.Paramaters.UserStoryParameter;
import com.meest.elasticsearch.ElasticSearchRespone;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.login.model.ForgotPasswordOtpVerify;
import com.meest.meestbhart.login.model.ForogtPasswordMobile;
import com.meest.meestbhart.login.model.LoginEmailCheckResponse;
import com.meest.meestbhart.login.model.LoginMobileCheck;
import com.meest.meestbhart.login.model.LoginParam;
import com.meest.meestbhart.login.model.LoginResponse;
import com.meest.meestbhart.login.model.ResetEmailPasswordParam;
import com.meest.meestbhart.login.model.ResetPasswordParam;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.meestbhart.register.DefaultAppResponse;
import com.meest.meestbhart.register.DefaultParam;
import com.meest.meestbhart.register.fragment.choosephotovideo.model.ChooseVideoPhotoResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.ChooseTopicResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicParam;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.UpdateSelectedTopicsResponse;
import com.meest.meestbhart.register.fragment.otp.model.EmailOtpVerificationParam;
import com.meest.meestbhart.register.fragment.otp.model.MobileOtpVerificationParam;
import com.meest.meestbhart.register.fragment.otp.model.OtpVerificartionResponse;
import com.meest.meestbhart.register.fragment.otp.model.RegisteP;
import com.meest.meestbhart.register.fragment.otp.model.RegisterNewResponse;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyEmailAndMobileParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyMobileParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifyEmailParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifyEmailResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.register.fragment.username.model.VerifyUserNameResponse;
import com.meest.meestbhart.register.fragment.username.model.VerifyUsernameParam;
import com.meest.metme.model.ChatUnreadCountResponse;
import com.meest.metme.model.ChatUserContactModel;
import com.meest.metme.model.FontResponse;
import com.meest.metme.model.ForwardMessageResponse;
import com.meest.metme.model.GetForwardListResponse;
import com.meest.metme.model.GradientColor;
import com.meest.metme.model.SaveTextAppearanceModel;
import com.meest.metme.model.SolidColor;
import com.meest.metme.model.WallpaperModel;
import com.meest.metme.model.WallpaperNewModel;
import com.meest.metme.model.chat.CreateRoomRequestModel;
import com.meest.metme.model.chat.CreateRoomResponseModel;
import com.meest.model.DeleteVideoParam;
import com.meest.model.MultipleStoryDataModel;
import com.meest.model.StoryDataModel;
import com.meest.responses.AcceptRejectResponse;
import com.meest.responses.AddLikeResponse;
import com.meest.responses.AudioCategoryResponse;
import com.meest.responses.AudioResponse;
import com.meest.responses.BulkChatDeleteResponse;
import com.meest.responses.BulkDeleteChatHeadResponse;
import com.meest.responses.BulkDeleteRequestResponse;
import com.meest.responses.CampCompletedResponse;
import com.meest.responses.CampaignAccountOverviewResponse;
import com.meest.responses.CampaignDeleteResponse;
import com.meest.responses.CampaignDraftedResponse;
import com.meest.responses.CampaignDuplicateResponse;
import com.meest.responses.CampaignPaymentResponse;
import com.meest.responses.CampaignPreviewResponse;
import com.meest.responses.CampaignSignUpLeadsResponse;
import com.meest.responses.CampaignSignUpResponse;
import com.meest.responses.CampaignUpdateResponse;
import com.meest.responses.CampaignViewResponse;
import com.meest.responses.ChatSettingResponse;
import com.meest.responses.CollectionResponse;
import com.meest.responses.ColorsResponseNew;
import com.meest.responses.CommentDislikeResponse;
import com.meest.responses.CommentLikeResponse;
import com.meest.responses.CreateNewCampaignResponse;
import com.meest.responses.DeleteStoryResponse;
import com.meest.responses.DisLikeResponse;
import com.meest.responses.FeedFollowResponse;
import com.meest.responses.FeedResponse;
import com.meest.responses.FeedSubCommentResponse;
import com.meest.responses.FeelingResponse;
import com.meest.responses.FollowRequestResponse;
import com.meest.responses.FollowResponse;
import com.meest.responses.FollowerListResponse;
import com.meest.responses.FollowsPeoplesResponse;
import com.meest.responses.FriendsListResponse;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.responses.GetPendingUserResponse;
import com.meest.responses.GetProfilebyUserResponse;
import com.meest.responses.GetReportResponse;
import com.meest.responses.GetVideo;
import com.meest.responses.HashtagProfileResponse;
import com.meest.responses.HashtagSearchResponse;
import com.meest.responses.InsertPostLikeResponse;
import com.meest.responses.InsertRecord;
import com.meest.responses.LanguagesResponse;
import com.meest.responses.LikeDetailsResponse;
import com.meest.responses.OnBoardingResponse;
import com.meest.responses.PostByIdResponse;
import com.meest.responses.PostHashTagResponse;
import com.meest.responses.PostTextResponse;
import com.meest.responses.ProfilePhotoUploadResponse;
import com.meest.responses.SavedDataResponse;
import com.meest.responses.SearchHomeResponse;
import com.meest.responses.ShowAdvtResponse;
import com.meest.responses.ShowFeedAdOne;
import com.meest.responses.SinglePostInsightResponse;
import com.meest.responses.SubCommentResponse;
import com.meest.responses.SvsSubCommentResponse;
import com.meest.responses.TextPostImageBackgroundResponse;
import com.meest.responses.ThumbnailResponse;
import com.meest.responses.TrendHashtagResponse;
import com.meest.responses.TrendPeopleResponse;
import com.meest.responses.UploadBulkMediaResponse;
import com.meest.responses.UrlResponse;
import com.meest.responses.UserActivityResponse;
import com.meest.responses.UserBlockListResponse;
import com.meest.responses.UserFAQCategoryResponse;
import com.meest.responses.UserFAQResponse;
import com.meest.responses.UserFeedbackResponse;
import com.meest.responses.UserFollowerStoryResponse;
import com.meest.responses.UserInsightGraphResponse;
import com.meest.responses.UserInsightsResponse;
import com.meest.responses.UserPrivacyResponse;
import com.meest.responses.UserSelectedTopics;
import com.meest.responses.UserSettingUpdateResponse;
import com.meest.responses.UserSettingsResponse;
import com.meest.responses.UserUpdatedSelectedTopics2;
import com.meest.responses.VideoAddCommentResponse;
import com.meest.responses.VideoCommentLikeResponse;
import com.meest.responses.VideoCommentResponse;
import com.meest.responses.VideoPostResponse;
import com.meest.responses.VideoSearchResponse;
import com.meest.responses.VideoUploadResponse;
import com.meest.responses.VideosResponse;
import com.meest.social.socialViewModel.model.CheckChatHeadNFollowResponse1;
import com.meest.social.socialViewModel.model.CreateGroupResponse;
import com.meest.social.socialViewModel.model.MediaPostResponse1;
import com.meest.social.socialViewModel.model.MessageResponse;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;
import com.meest.social.socialViewModel.model.RemoveBlockedUserResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.social.socialViewModel.model.SuggestionResponse;
import com.meest.social.socialViewModel.model.TopResponse;
import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.social.socialViewModel.model.UpdatePasswordResponse1;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.svs.models.AudioUploadResponse;
import com.meest.svs.models.FavMusicAudioResponse;
import com.meest.svs.models.FollowListResponse;
import com.meest.svs.models.HashtagVideoResponse;
import com.meest.svs.models.MusicVideoResponse;
import com.meest.svs.models.SVSProfileModel;
import com.meest.svs.models.SearchResponse;
import com.meest.svs.models.VideoByCategoryResponse;
import com.meest.svs.models.VideoByPostIdResponse;
import com.meest.svs.models.VideoCategoryResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

//import com.meest.svs.models.ReportTypeResponse;

public interface WebApi {

    public static final String USER_IMG_BASE_URL = "http://tunemist.com/vadmin/assets/uplods/user_profile/";

    @GET("/pub/languages")
    Call<LanguagesResponse> languages();

    @POST("/pub/verifyEmail")
    Call<VerifyEmailResponse> verifyEmail(@Body VerifyEmailParam verifyEmailParam);

    @POST("/pub/verifyOtpAll")
    Call<OtpVerificartionResponse> otpVerification(@Body MobileOtpVerificationParam verifyParam);

    @POST("/pub/verifyEmailOTP")
    Call<OtpVerificartionResponse> verificationEmailOtp(@Body EmailOtpVerificationParam verificationParam);

    @POST("/pub/sendOTPONEmail")
    Call<ApiResponse> sendEmailOtp(@Body VerifyEmailParam verifyEmailParam);

//    @POST("/pub/verifyMobile")
//    Call<VerifyEmailResponse> verifyAll(@Body VerifiyEmailAndMobileParam verifyParam);//VerifiyEmailAndMobileParam

    @POST("/pub/verifyAll")
    Call<VerifyEmailResponse> verifyAll(@Body VerifiyEmailAndMobileParam verifyParam);

    @POST("/pub/forgotPassword")
    Call<ForogtPasswordMobile> forgotPassword(@Body VerifiyMobileParam verifyParam);

    @POST("/pub/verifyUsername")
    Call<VerifyUserNameResponse> verifyUsername(@Body VerifyUsernameParam verifyParam);

    @POST("/stories/insert")
    Call<ApiResponse> insertStory(@HeaderMap Map<String, String> headers, @Body StoryDataModel storyDataModel);

    @POST("/pub/loginUser")
    Call<LoginResponse> login(@Body LoginParam loginParam);

    @POST("user/setDefaultApp")
    Call<DefaultAppResponse> defaultApp(@HeaderMap Map<String, String> headers, @Body DefaultParam defaultParam);


    @POST("/pub/verifyOTP")
    Call<VerifyEmailResponse> forgotPasswordverifyOTP(@Body ForgotPasswordOtpVerify forgotPasswordOtpVerify);

    @POST("/pub/resetPassword")
    Call<LoginResponse> resetPassword(@Body ResetPasswordParam resetPasswordParam);

    @POST("/pub/resetPassword/ByEmail")
    Call<LoginResponse> resetEmailPassword(@Body ResetEmailPasswordParam resetEmailPasswordParam);

    @POST("/pub/registerUser")
    Call<com.meest.responses.RegisterResponse> register(@Body RegisteP resetPasswordParam);


    @POST("/pub/register")
    Call<RegisterNewResponse> register_new(@Body RegisteP resetPasswordParam);

    @POST("/pub/onBoard")
    Call<OnBoardingResponse> afterSignup();

    @POST("/topics/getAll")
    Call<TopicsResponse> getAll_topic(@HeaderMap Map<String, String> headers, @Body TopicParam topicParam);

    //delete story
    @POST("/stories/delete")
    Call<DeleteStoryResponse> deleteUserStory(@HeaderMap Map<String, String> headers, @Body UserDeleteStoryParameter parameter);

    //own stories
    @POST("/stories/getAllStoriesByUserId")
    Call<UserFollowerStoryResponse> getOwnStories(@HeaderMap Map<String, String> headers, @Body UserStoryParameter parameter);

    //get user stories all
    @POST("/stories/all")
    Call<UserFollowerStoryResponse> getUserStories(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @Multipart
    @POST("/media/insert")
        //Call<DocumentResponse> document(@Part("document_file\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("userId") String user_id,@Part("document_type") String document_type,@Part("document_number") String document_number);
    Call<ProfilePhotoUploadResponse> insert(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part file);
    @Multipart
    @POST("/media/insert/imageAndThumbnail")
    Call<ThumbnailResponse> getThumbnail(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part originalFile, @Part MultipartBody.Part thumbnailFile);

    @POST("/user/update")
    Call<ApiResponse> update_profile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/user/update")
    Call<ApiResponse> updateUserProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("pub/update")
    Call<ApiResponse> updateAuthToken(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/userTopics/insert")
    Call<UpdateSelectedTopicsResponse> userTopics_insert(@HeaderMap Map<String, String> headers, @Body ChooseTopicResponse chooseTopicResponse);

    @POST("/pub/afterSIgnup")
    Call<ChooseVideoPhotoResponse> pub_afterSIgnup(@HeaderMap Map<String, String> headers);

    @POST("/post/getAllWithAdvt")
    Call<ShowAdvtResponse> showAdvtFeed(@HeaderMap Map<String, String> headers);

    @POST("/post/getAll")
    Call<FeedResponse> getAllFeed(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/user/get/userMedia")
    Call<MediaPostResponse1> getMediaFeed(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

//    @POST("trending/get/trendingPosts")
//    Call<MediaPostResponse> getTrendingMediaFeed(@HeaderMap Map<String, String> headers);
    @POST("trending/get/topPosts")
    Call<MediaPostResponse1> getTrendingMediaFeed(@HeaderMap Map<String, String> headers);

    @POST("/follows/remove/follower")
    Call<FollowRequestResponse> rejectFollow(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/follows/acceptedStatus")
    Call<FollowRequestResponse> acceptFollow(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

//    @POST("/status/get/colors/for/story")
//    Call<StoryGradientResponse> getGradientColor(@HeaderMap Map<String, String> headers);
    @POST("/post/insertComment")
    Call<ApiResponse> insertComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/insert/comment")
    Call<ApiResponse> insertAdComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/insertSubComment")
    Call<ApiResponse> insertSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/insert/sub/comment")
    Call<ApiResponse> insertAdSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/getSubComment")
    Call<FeedSubCommentResponse> getFeedSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/get/sub/comments")
    Call<FeedSubCommentResponse> getAdFeedSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/get/sub/comments")
    Call<SvsSubCommentResponse> getSvsSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/insertLike")
    Call<InsertPostLikeResponse> insertLike(@HeaderMap Map<String, String> headers, @Body InsertLikeParameters insertLikeParameters);

    @POST("/advertise/insert/like")
    Call<InsertPostLikeResponse> insertLikeAd(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);
    
    @POST("/post/insertDislike")
    Call<DisLikeResponse> insertDislike(@HeaderMap Map<String, String> headers, @Body InsertLikeParameters insertLikeParameters);

    @POST("/post/getComments")
    Call<VideoCommentResponse> getComments(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/get/comments")
    Call<VideoCommentResponse> getAdComments(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);
    
    @POST("/pub/mobileVerify")
    Call<LoginMobileCheck> mobileVerify(@Body VerifiyMobileParam commentGetParam);

    @POST("/pub/checkEmail")
    Call<LoginEmailCheckResponse> emailVerify(@Body VerifyEmailParam emailGetParam);

    @POST("/post/commentLike")
    Call<CommentLikeResponse> commentLike(@HeaderMap Map<String, String> headers, @Body CommentLikeParam commentLikeParam);

    @POST("/post/removeCommentLike")
    Call<CommentDislikeResponse> removeCommentLike(@HeaderMap Map<String, String> headers, @Body CommentDisLikeParam commentDisLikeParam);
    
    @FormUrlEncoded
    @POST("getVideo")
    Call<GetVideo> getVideo(@Field("page") String page, @Field("id") String id,
                            @Field("contest_id") String contest_id, @Field("uploaded_by") String uploaded_by,
                            @Field("ws_key") String ws_key, @Field("userId") String user_id,
                            @Field("language_id") String language_id,
                            @Field("type") String type);
    
//    @POST("/admin/userProfile")
//    Call<UserProfileFetchResponse> userProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);
    
    @POST("/user/get/userProfileById")
    Call<UserProfileRespone1> userProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/user/get/userProfileById")
    Call<ChatUserContactModel> userMetMeProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @GET("/follows/suggestUser")
    Call<SuggestionResponse> suggestUser(@HeaderMap Map<String, String> headers);

    @POST("/follows/following")
    Call<ApiResponse> followingRequest(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/follows/followingStatus")
    Call<UnfollowResponse> followingStatus(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @FormUrlEncoded
    @POST("/follows/followsData")
    Call<FollowerListResponse> followsData(@HeaderMap Map<String, String> headers, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("/follows/followsData")
    Call<FollowerListResponse> followsData(@HeaderMap Map<String, String> headers, @Field("story") String story,
                                           @Field("caption") String caption,
                                           @Field("status") boolean status,
                                           @Field("image") boolean image
    );

    @GET("/follows/getBackgroundImage")
    Call<TextPostImageBackgroundResponse> getBackgroundImage(@HeaderMap Map<String, String> headers);


    @POST("/user/get/userProfileById")
    Call<UserProfileRespone1> getUserProfileData(@HeaderMap Map<String, String> headers);

    @Multipart
    @POST("/video/add")
    Call<VideoUploadResponse> uploadVideo(@HeaderMap Map<String, String> headers,
                                          @Part() MultipartBody.Part videoFile);

    @POST("/video/add/data")
    Call<VideoPostResponse> postVideo(@HeaderMap Map<String, String> headers,
                                      @Body HashMap<String, Object> body);

    @POST("/audio/catagory/getAll")
    Call<AudioCategoryResponse> getAllAudioCategory(@HeaderMap Map<String, String> headers);

    @POST("/audio/get/by/catagory")
    Call<AudioResponse> getAllAudio(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/insert/like")
    Call<AddLikeResponse> getLikeVideo(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/get/comments")
    Call<VideoCommentResponse> getVideoComments(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/insert/comment")
    Call<VideoAddCommentResponse> addVideoComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/insert/sub/comment")
    Call<SubCommentResponse> addVideoCommentReply(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/follows/followingStatus")
    Call<FollowResponse> removeFollow(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/follows/following")
    Call<FollowResponse> followRequest(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("/search/")
    Call<SearchHomeResponse> search_home(@HeaderMap Map<String, String> headers, @Body SearchHomeParam searchHomeParam);

    @POST("/follows/following")
    Call<FollowsPeoplesResponse> follow_peoples(@HeaderMap Map<String, String> headers, @Body FollowPeoplesParam followPeoplesParam);


    @POST("/follows/followingStatus")
    Call<FollowsPeoplesResponse> Unfollow_peoples(@HeaderMap Map<String, String> headers, @Body UnFollowPeoplesParam unFollowPeoplesParam);

    @POST("/video/insert/comment/like")
    Call<VideoCommentLikeResponse> getAllVideoCommentLike(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @FormUrlEncoded
    @POST("/video/getbyhashtag")
    Call<HashtagProfileResponse> getHashtagByVideo(@HeaderMap Map<String, String> headers, @Field("key") String key);

    @FormUrlEncoded
    @POST("/status/editImage")
    Call<PostTextResponse> CreateTextPost(@HeaderMap Map<String, String> headers, @Field("caption") String caption, @Field("imageUrl") String imageUrl, @Field("colourCode") String colourCode);


    @POST("/post/insert")
    Call<ApiResponse> InsertTextPost(@HeaderMap Map<String, String> headers, @Body TextPostUploadParam textPostUploadParam);

    @FormUrlEncoded
    @POST("/user/adharAuth")
    Call<ApiResponse> VerifyAdharNumber(@HeaderMap Map<String, String> headers, @Field("adharNumber") String adharNumber);

    @POST("/user/get/user/profile")
    Call<SVSProfileModel> getSVSProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/user/me")
    Call<GetProfilebyUserResponse> GetMyUserProfile(@HeaderMap Map<String, String> headers, @Body UserStoryParameter body);


    @POST("/video/getbyhashtag")
    Call<HashtagVideoResponse> searchVideoByHashtag(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("post/getbyhashtag")
    Call<PostHashTagResponse> searchPostByHashtag(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/getAll")
    Call<VideosResponse> getAllVideosForYou(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/search")
    Call<SearchResponse> searchFromVideo(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @FormUrlEncoded
    @POST("/post/postuser/setting")
    Call<GetReportResponse> postusersetting(@HeaderMap Map<String, String> headers, @Field("postId") String postId, @Field("friendId") String friendId);


    @FormUrlEncoded
    @POST("/post/updateuser/post")
    Call<ApiResponse> updateuserpost(@HeaderMap Map<String, String> headers, @Field("postId") String postId, @Field("friendId") String friendId, @Field("pushNotification") boolean pushNotification,
                                     @Field("isMute") boolean isMute, @Field("report") String report, @Field("isReportedByme") boolean isReportedByme);

    @POST("/video/getVideosbyotherUserId")
    Call<VideosResponse> getVideoByUser(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/catagory/getAll")
    Call<VideoCategoryResponse> getVideoCategories(@HeaderMap Map<String, String> headers);


    @POST("/video/searchVideosByCat")
    Call<VideoByCategoryResponse> getVideoByCategories(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @FormUrlEncoded
    @POST("post/get/user/media")
    Call<ProfilesVideoResponse1> usermedia(@HeaderMap Map<String, String> headers, @Field("userId") String userId);

    @POST("post/get/user/admin/media")
    Call<ProfilesVideoResponse1> videoTabUserMedia(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("post/get/tagPost")
    Call<ProfilesVideoResponse1> tagUserMedia(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @FormUrlEncoded
    @POST("post/get/latestPhotos")
    Call<ProfilesVideoResponse1> otherUserCamera(@HeaderMap Map<String, String> headers, @Field("userId") String userId);


    @POST("/video/add/view")
    Call<ApiResponse> addViewToVideo(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/video/searchByAudiaId")
    Call<MusicVideoResponse> searchVideoByAudio(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/audio/getProfileAudio")
    Call<FavMusicAudioResponse> favMusicByUser(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/advertise/delete")
    Call<CampaignDeleteResponse> deleteCampaign(@HeaderMap Map<String, String> headers, @Body HashMap body);

    @POST("/advertise/get/drafted")
    Call<CampaignDraftedResponse> saveCampaignDraft(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/campaign/get/duplicate")
    Call<CampaignDuplicateResponse> getcampaignduplicate(@HeaderMap Map<String, String> headers);

    @POST("/campaign/getCampaignById")
    Call<CampaignPreviewResponse> getcampaignpreview(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/campaign/add")
    Call<CreateNewCampaignResponse> createNewCampaign(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/campaign/update")
    Call<CampaignUpdateResponse> getupdatecampaign(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/campaign/getCampaignById")
    Call<CampaignPreviewResponse> getCampaignDetail(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("/advertise/add/signUpUser")
    Call<CampaignSignUpResponse> getCampaignSignUp(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/follows/followsData")
    Call<FollowListResponse> getFollowerData(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @Multipart
    @POST("/media/insert")
    Call<UploadimageResponse> uploadImageRequest(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part image);

    @POST("/status/get/colors")
    Call<ColorsResponseNew> getColors(@HeaderMap Map<String, String> headers);

    @POST("/post/activity/getAll")
    Call<FeelingResponse> getFeelingList(@HeaderMap Map<String, String> headers);

    @POST("/post/activity/sub/getAll")
    Call<FeelingResponse> getFeelingSubList(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/bulk/remove")
    Call<ApiResponse> deleteVideo(@HeaderMap Map<String, String> headers, @Body DeleteVideoParam deleteVideoParam);

    @POST("/audio/get/by/catagory")
    Call<AudioResponse> getAudioByCategory(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/postSaved/savePostBy")
    Call<ApiResponse> savePost(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/trending/get/trendingHashtgs")
    Call<TrendHashtagResponse> trend_hashTag(@HeaderMap Map<String, String> headers);

    @POST("trending/get/topHashtags")
    Call<TrendHashtagResponse> top_hashTag(@HeaderMap Map<String, String> headers);

    @POST("/trending/get/trendingPeoples")
    Call<TrendPeopleResponse> trend_people(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/trending/get/topPeoples")
    Call<TrendPeopleResponse> top_people(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/trending/get/trendingPosts")
    Call<FeedResponse> trend_post(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("trending/get/topPosts")
    Call<FeedResponse> top_post(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/trending/get/trendingImages")
    Call<FeedResponse> trend_images(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/trending/get/topImages")
    Call<FeedResponse> top_images(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/trending/get/post/trendingVideos")
    Call<FeedResponse> trend_videos(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/trending/get/post/topVideos")
    Call<FeedResponse> top_videos(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/report/getByType")
    Call<ReportTypeResponse> getReportTypes(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/report/add")
    Call<ApiResponse> addReport(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/post/commentLike")
    Call<VideoCommentLikeResponse> insertPostCommentLike(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/advertise/insert/comment/like")
    Call<VideoCommentLikeResponse> insertAdPostCommentLike(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/video/make/dueo")
    Call<UrlResponse> makeDuo(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/make/trio")
    Call<UrlResponse> makeTrio(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/admin/allUsers")
    Call<FriendsListResponse> searchFriendsList(@HeaderMap Map<String, String> headers, @Body Map<String, String> body);

    @POST("/admin/updateUser")
    Call<ApiResponse> delUserAccount(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/user/get/activityHistory")
    Call<UserActivityResponse> getActivityHistory(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/media/getAllmedia")
    Call<CollectionResponse> getUserCollectionData(@HeaderMap Map<String, String> headers);


    @POST("/postSaved/getSaved")
    Call<SavedDataResponse> getSavedData(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/user/me")
    Call<UserSettingsResponse> getUserSettings(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/user/update")
    Call<UserSettingUpdateResponse> updateUserSettings(@HeaderMap Map<String, String> headers, @Body UserSettingParams data);

    @POST("/user/changePassword")
    Call<UpdatePasswordResponse1> updateUserPassword(@HeaderMap Map<String, String> headers, @Body UpdatePasswordParams data);

    @POST("/aboutPrivacy/get")
    Call<UserPrivacyResponse> getPrivacyPolicy(@HeaderMap Map<String, String> headers, @Body AboutRequestParameter paramter);

    @POST("/faq/getAll")
    Call<UserFAQResponse> getFAQ(@HeaderMap Map<String, String> headers);

    @POST("/userBlockList/get")
    Call<UserBlockListResponse> getBlockedUserList(@HeaderMap Map<String, String> headers);

    @POST("/userBlockList/removeUser")
    Call<RemoveBlockedUserResponse> removeBlockedUser(@HeaderMap Map<String, String> headers, @Body RemoveBlockedUserParameter removeUser);

    @POST("/userBlockList/add")
    Call<UserFAQResponse> addUserToBlockList(@HeaderMap Map<String, String> headers, @Body BlockUserParameters blockedUser);

    @POST("/userFeedback/add")
    Call<UserFeedbackResponse> uploadFeedback(@HeaderMap Map<String, String> headers, @Body UserFeedbackParameter feedback);

    @POST("/video/extract/audio")
    Call<UrlResponse> extractAudio(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/video/merge/audio")
    Call<UrlResponse> mergeAudio(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/follows/followsData")
    Call<FeedFollowResponse> getFeedFollowerData(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/faq/catagory/getAll")
    Call<UserFAQCategoryResponse> getFAQCategories(@HeaderMap Map<String, String> headers);

    @POST("/faq/getAll/byCat")
    Call<UserFAQResponse> getFAQbyCategoryID(@HeaderMap Map<String, String> headers, @Body FAQParametr paramters);

    @POST("/post/getAllInsightByUserId")
    Call<UserInsightsResponse> getInsights(@HeaderMap Map<String, String> headers, @Body InsightsParameter feedback);

    @POST("/post/insigntGraphByPostId")
    Call<UserInsightGraphResponse> getInsightsGraph(@HeaderMap Map<String, String> headers, @Body InsightGraphParameter feedback);

    @POST("post/getAllInsightByPostId")
    Call<SinglePostInsightResponse> getAllInsightByPostId(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/notification/all")
    Call<AllNotificationResponse> getAllNotification(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);


    @POST("/notification/unreadCount")
    Observable<AllNotificationResponse> getUnReadNotificationCount(@HeaderMap Map<String, String> headers);

    @POST("/notification/remove/one")
    Call<ApiResponse> deleteNotification(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("post/getByPostId")
    Call<PostByIdResponse> postById(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/get/accountOverview")
    Call<CampaignAccountOverviewResponse> getCampAccountOverview(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/get/completedAdvts")
    Call<CampCompletedResponse> getCampCompleted(@HeaderMap Map<String, String> headers);

    @POST("/advertise/get/runnungAdvts")
    Call<CampCompletedResponse> getCampRunning(@HeaderMap Map<String, String> headers);

    @POST("/advertise/get/advtStat")
    Call<CampaignViewResponse> getCampView(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/advertise/get/signUpUser")
    Call<CampaignSignUpLeadsResponse> getSignupLeads(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/addPayment")
    Call<CampaignPaymentResponse> getPaymentStatus(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/post/deletePostByPostId")
    Call<DeleteStoryResponse> deletePostByID(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

//    @POST("/userBlockList/add")
//    Call<BlockUserResponse> blockUser(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/stories/insert")
    Observable<ApiResponse> insertUserStory(@HeaderMap Map<String, String> headers, @Body MultipleStoryDataModel dataModel);

    @Multipart
    @POST("/media/insert/bulkInsert")
    Observable<UploadBulkMediaResponse> uploadMedia(@HeaderMap Map<String, String> headers, @Part List<MultipartBody.Part> image);

    @POST("/post/removeComment")
    Call<ApiResponse> deletePostComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/delete/comment")
    Call<ApiResponse> deleteAdPostComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/delete/comment")
    Call<ApiResponse> deleteSvsComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("/video/delete/subcomment")
    Call<ApiResponse> deleteSubSvsComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/removeSubComment")
    Call<ApiResponse> deleteSubPostComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/delete/subcomment")
    Call<ApiResponse> deleteAdSubPostComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @Multipart
    @POST("/audio/add")
    Call<AudioUploadResponse> uploadAudio(@HeaderMap Map<String, String> headers,
                                          @Part MultipartBody.Part file);


    @POST("/advertise/get/ramdom/runnungAdvts")
    Call<ShowFeedAdOne> showAdvtFeedOne(@HeaderMap Map<String, String> headers);

    @Multipart
    @POST("/media/insert")
        //Call<DocumentResponse> document(@Part("document_file\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("user_id") String user_id,@Part("document_type") String document_type,@Part("document_number") String document_number);
    Call<ProfilePhotoUploadResponse> insertFiles(@HeaderMap Map<String, String> headers,
                                                 @Part MultipartBody.Part file,
                                                 @Part("type") RequestBody CustomerID,
                                                 @Part("BackupType") RequestBody BackupType,
                                                 @Part("ProductName") RequestBody ProductName,
                                                 @Part("AppVersionAppVersion") RequestBody AppVersionAppVersion,
                                                 @Part("AppType") RequestBody AppType,
                                                 @Part("IMEINumber") RequestBody IMEINumber,
                                                 @Part("DeviceInfo") RequestBody DeviceInfo,
                                                 @Part("Remark") RequestBody Remark,
                                                 @Part("FileName") RequestBody FileName,
                                                 @Part("Extentsion") RequestBody Extentsion);

    @Multipart
    @POST("/media/recordInsert")
    Call<InsertRecord> insertRecord(@HeaderMap Map<String, String> headers,
                                    @Part MultipartBody.Part file,
                                    @Part("type") RequestBody type,
                                    @Part("mobile") RequestBody mobile,
                                    @Part("userId") RequestBody userid);

    @POST("/video/get/by/videoid")
    Call<VideoByPostIdResponse> getVideoData(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/topics/getAll")
    Call<TopicsResponse> getAdInterest(@HeaderMap Map<String, String> headers);

    @POST("/post/getLikes")
    Call<LikeDetailsResponse> getLikeDetails(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/userBlockList/removeUser")
    Call<RemoveBlockedUserResponse> removeBlockedUserProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

//    @POST("/userBlockList/userUnblock")
//    Call<BlockUserResponse> unblockuser(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

//
//    @POST("/search/peoples")
//    Call<PeopleSearchResponse> searchPeople(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


//    @POST("/trending/get/trendingPeoples")
//    Call<PeopleSearchResponse> searchPeople(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("/trending/get/topPosts")
    Call<TopResponse> hashTagTop(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);


    @POST("/trending/get/trendingPosts")
    Call<TopResponse> hashTagMostTrending(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/search/videos")
    Call<VideoSearchResponse> searchVideo(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/search/images")
    Call<VideoSearchResponse> searchImages(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("search/hashtags")
    Call<HashtagSearchResponse> searchHashTag(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @GET()
    Call<ElasticSearchRespone> elasticSearch(@Url String str);


    @POST("/post/share")
    Call<UserFeedbackResponse> reSharePost(@HeaderMap Map<String, String> headers, @Body Map<String, String> body);

    @GET()
    Call<com.meest.videomvvmmodule.model.elasticsearch.ElasticSearchRespone> userElasticSearch(@Url String str);


    @POST("chat/following")
    Call<MessageResponse> message(@HeaderMap Map<String, String> headers,
                                  @Body HashMap<String, Object> body);

    @POST("chat/following")
    Single<MessageResponse> message1(@HeaderMap Map<String, String> headers,
                                     @Body HashMap<String, Object> body);

    /*@FormUrlEncoded
    @POST("/chat/chatHeads")
    Call<CreateGroupResponse> createChatHeads(@HeaderMap Map<String, String> headers, @Field("toUserId") String toUserId);*/

    @POST("/chat/chatHeads")
    Call<CreateGroupResponse> createChatHeads2(@HeaderMap Map<String, String> headers, @Body() Map map);

    @POST("/chat/createRoom")
    Call<CreateRoomResponseModel> createRoom(@HeaderMap Map<String, String> headers, @Body CreateRoomRequestModel createRoomRequestModel);


    @POST("/chatSetting/get")
    Call<ChatSettingResponse> GetChatsSetting(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("/chatSetting/update")
    Call<JSONObject> UpdateChatSetting(@HeaderMap Map<String, String> headers, @Body UpdateChatSettingParam updateChatSettingParam);

    @POST("/chat/getDocsMedia")
    Call<GetDocsMediaChatsResponse> getDocsMedia(@HeaderMap Map<String, String> headers, @Body Map body);

    @POST("chat/getBackground")
    Call<WallpaperModel> getChatBackground(@HeaderMap Map<String, String> headers);

    @POST("/chat/bulk/delete")
    Call<BulkDeleteRequestResponse> bulkDelete(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/chat/chatheaddelete")
    Call<BulkDeleteChatHeadResponse> chatDelete(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/chat/acceptedStatus")
    Call<AcceptRejectResponse> acceptRejectRequest(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/userBlockList/add")
    Call<AcceptRejectResponse> block(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/chat/profile")
    Call<GetPendingUserResponse> getPendingUserDetails(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    //========================================================
    @GET("/status/get/allcolors")
    Call<SolidColor> getSolidColor(@HeaderMap Map<String, String> headers);

    @GET("/pub/allFonts")
    Call<FontResponse> getFont(@HeaderMap Map<String, String> headers);

    @GET("/pub/allGradients")
    Call<GradientColor> getGradientColor(@HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST("/chat/deleteChat")
    Call<JsonObject> deleteChat(@HeaderMap Map<String, String> headers, @Field("messageId") String messageId);

    @FormUrlEncoded
    @POST("/chat/bulkDeleteChat")
    Call<JsonObject> deleteMultipleChat(@HeaderMap Map<String, String> headers, @Body Map body);

    @POST("/follows/followsData")
    Call<GetForwardListResponse> getFollowerData(@HeaderMap Map<String, String> headers);

    @GET("/follows/followsData")
    Call<GetForwardListResponse> getFollowerDataGet(@HeaderMap Map<String, String> headers);

    @GET("/follows/getBackgroundImages")
    Call<WallpaperNewModel> getWallpaper(@HeaderMap Map<String, String> headers);

    @POST("/chat/chatHead/color")
    Call<JSONObject> saveTextAppearance(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/chat/chatHead/color/byUserId")
    Call<SaveTextAppearanceModel> getChatTheme(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/chat/forwardMessage")
    Call<ForwardMessageResponse> forwardMultipleMessges(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("chat/totalUnread")
    Call<ChatUnreadCountResponse> chatTotalUnReadCount(@HeaderMap Map<String, String> headers);

    @POST("/chat/bulkDeleteChat")
    Call<BulkChatDeleteResponse> bulkDeleteChat(@HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

    @POST("/chat/getFollowingDetails")
    Call<CheckChatHeadNFollowResponse1> checkFollowNChatHead(@HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

    @POST("/userTopics/getAll")
    Call<UserSelectedTopics> getSelectedTopics(@HeaderMap Map<String, String> headers);

    //    @POST("/userTopics/update")
//    Call <UserUpdatedSelectedTopics> getUpdatedSelectedTopics(@HeaderMap Map<String, String> headers, @Body HashMap<String, ArrayList<String>> updateProfileParam);
//    @POST("/userTopics/update")
//    Call <UserUpdatedSelectedTopics> getUpdatedSelectedTopics (@Header("x-token") String token, @Part("topic") List<String> topic);
//    @POST("/userTopics/update")
//    Call <UserUpdatedSelectedTopics> getUpdatedSelectedTopics (@Header("x-token") String token, @Part List<MultipartBody.Part> topic);
    @POST("/userTopics/update")
    Call<UserUpdatedSelectedTopics2> getUpdatedSelectedTopics(@HeaderMap Map<String, String> headers, @Body UpdateTopicsParams updateTopicsParams);
}