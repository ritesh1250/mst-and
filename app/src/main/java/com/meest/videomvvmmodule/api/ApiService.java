package com.meest.videomvvmmodule.api;

import com.google.gson.JsonObject;
import com.meest.Paramaters.UpdatePasswordParams;
import com.meest.Paramaters.UserSettingParams;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.login.model.ForgotPasswordOtpVerify;
import com.meest.meestbhart.login.model.ForogtPasswordMobile;
import com.meest.meestbhart.login.model.LoginResponse;
import com.meest.meestbhart.login.model.ResetPasswordParam;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.meestbhart.register.DefaultAppResponse;
import com.meest.meestbhart.register.DefaultParam;
import com.meest.meestbhart.register.fragment.choosetopic.model.ChooseTopicResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicParam;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.UpdateSelectedTopicsResponse;
import com.meest.meestbhart.register.fragment.otp.model.MobileOtpVerificationParam;
import com.meest.meestbhart.register.fragment.otp.model.OtpVerificartionResponse;
import com.meest.meestbhart.register.fragment.otp.model.RegisterNewResponse;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyEmailAndMobileParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyMobileParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifyEmailResponse;
import com.meest.meestbhart.register.fragment.username.model.VerifyUserNameResponse;
import com.meest.meestbhart.register.fragment.username.model.VerifyUsernameParam;
import com.meest.responses.FeedSubCommentResponse;
import com.meest.responses.FollowerListResponse;
import com.meest.responses.FriendsListResponse;
import com.meest.responses.SavedDataResponse;
import com.meest.responses.SubCommentResponse;
import com.meest.responses.SvsSubCommentResponse;
import com.meest.responses.UserActivityResponse;

import com.meest.responses.UserBlockListResponse;
import com.meest.responses.UserSettingUpdateResponse;
import com.meest.responses.UserSettingsResponse;

import com.meest.responses.VideoCommentLikeResponse;
import com.meest.responses.VideoSearchResponse;
import com.meest.social.socialViewModel.model.BlockUserResponse;
import com.meest.social.socialViewModel.model.CreateGroupResponse;
import com.meest.social.socialViewModel.model.HashtagSearch;
import com.meest.social.socialViewModel.model.MediaPostResponse1;
import com.meest.social.socialViewModel.model.MessageResponse;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;
import com.meest.social.socialViewModel.model.RemoveBlockedUserResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.social.socialViewModel.model.TopResponse;
import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.social.socialViewModel.model.UpdatePasswordResponse1;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.social.socialViewModel.model.VerifyOtpResponse;
import com.meest.social.socialViewModel.model.comment.VideoAddCommentResponse;
import com.meest.social.socialViewModel.model.comment.VideoCommentResponse;
import com.meest.social.socialViewModel.model.search.ElasticSearchRespone;
import com.meest.social.socialViewModel.model.search.HashtagSearchResponse;
import com.meest.social.socialViewModel.model.search.PeopleSearchResponse;
import com.meest.social.socialViewModel.model.subcomment.ReportTypeNewResponse;
import com.meest.videomvvmmodule.model.AdsModel;
import com.meest.videomvvmmodule.model.CashFreeTransaction;
import com.meest.videomvvmmodule.model.DiamondPurchase;
import com.meest.videomvvmmodule.model.Explore;
import com.meest.videomvvmmodule.model.LanguageResponseMedley;
import com.meest.videomvvmmodule.model.PaytmTransaction;
import com.meest.videomvvmmodule.model.ShareResponse;
import com.meest.videomvvmmodule.model.comment.Comment;
import com.meest.videomvvmmodule.model.follower.Follower;
import com.meest.videomvvmmodule.model.follower.Followerstatus;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.model.music.SearchMusic;
import com.meest.videomvvmmodule.model.notification.Notification;
import com.meest.videomvvmmodule.model.user.ProfileCategory;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.user.SearchUser;
import com.meest.videomvvmmodule.model.user.TopCreatorResponse;
import com.meest.videomvvmmodule.model.user.UploadImageResponse;
import com.meest.videomvvmmodule.model.user.User;
import com.meest.videomvvmmodule.model.videos.AddPostResponse;
import com.meest.videomvvmmodule.model.videos.DataUploadToS3;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.model.videos.UploadVideoThumbnailResponse;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.model.wallet.ActiveGatewayResponse;
import com.meest.videomvvmmodule.model.wallet.CoinPlan;
import com.meest.videomvvmmodule.model.wallet.CoinRate;
import com.meest.videomvvmmodule.model.wallet.MyWallet;
import com.meest.videomvvmmodule.model.wallet.RewardingActions;
import com.meest.videomvvmmodule.view.music_bottomsheet.AddFavoriteResponse;
import com.meest.videomvvmmodule.view.music_bottomsheet.FavoriteMusicsResponse;
import com.meest.videomvvmmodule.viewmodel.OtpResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

//import com.meest.responses.BlockUserResponse;
//import com.meest.responses.CreateGroupResponse;
//import com.meest.responses.MessageResponse;
//import com.meest.responses.RemoveBlockedUserResponse;
//import com.meest.responses.UnfollowResponse;

public interface ApiService {

    @FormUrlEncoded
    @POST("User/registration_new")
    Single<User> registrationUser(@Header("Unique-key") String apiKey, @FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("Post/user_details_on_basis_meest_id")
    Single<User> fetchUser(@Header("Unique-key") String apiKey, @FieldMap HashMap<String, Object> hashMap, @Field("device_token") String device_token);

    //    @POST("Post/sound_list")
//    Single<Musics> getSoundList(@Header("Unique-key") String apiKey, @Header("Secret-key") String token);
    @POST("Post/sound_list")
    Single<Musics> getSoundList(@Header("Unique-key") String apiKey);
    @FormUrlEncoded
    @POST("Post/fav_sound_list")
    Single<FavoriteMusicsResponse> getFavoriteList(@Header("Unique-key") String apiKey, @Field("user_id") String userId);
    @FormUrlEncoded
    @POST("Post/add_favourite")
    Single<AddFavoriteResponse> addFavoriteMusic(@Header("Unique-key") String apiKey, @Field("user_id") String userId, @Field("sound_id") String soundId);

    @POST("Post/favourite_sound")
    Single<SearchMusic> getFavSoundList(@Header("Unique-key") String apiKey, @Header("Secret-key") String token, @Body JsonObject ids);

    @FormUrlEncoded
    @POST("Post/user_list_search")
    Single<SearchUser> searchUser(@Header("Unique-key") String apiKey,
                                  @Field("user_id") String userId,
                                  @Field("keyword") String keyword,
                                  @Field("count") int count,
                                  @Field("start") int start);

    @FormUrlEncoded
    @POST("Post/sound_list_search")
    Single<SearchMusic> searchSoundList(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
                                        @Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("User/notification_setting")
    Single<RestResponse> updateToken(@Header("Secret-key") String token,
                                     @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST("User/check_username")
    Single<RestResponse> checkUsername(@Header("Unique-key") String apiKey, @Field("user_name") String userName);

    @GET("User/logout")
    Single<RestResponse> logOutUser(@Header("Unique-key") String apiKey, @Header("Secret-key") String token);

    @FormUrlEncoded
    @POST("Post/add_comment")
    Single<RestResponse> addComment(@Header("Unique-key") String apiKey,
                                    @Field("post_id") String postId,
                                    @Field("comment") String comment,
                                    @Field("first_name") String first_name,
                                    @Field("comment_id") String commentId,
                                    @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Post/add_comment_like")
    Single<RestResponse> addCommentLike(@Header("Unique-key") String apiKey,
                                        @Field("post_id") String postId,
                                        @Field("comment_id") String commentId,
                                        @Field("first_name") String first_name,
                                        @Field("user_id") String user_id);

    // @Field("comment_id") String commentId,
    /*@FormUrlEncoded
    @POST("Post/follow_unfollow")
    Single<RestResponse> followUnFollow(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
                                        @Field("to_user_id") String toUserId); */
    @FormUrlEncoded
    @POST("Post/follow_unfollow")
    Single<RestResponse> followUnFollow(@Header("Unique-key") String apiKey, @Field("from_user_id") String fromUserId, @Field("to_user_id") String toUserId);

    @FormUrlEncoded
    @POST("Post/top_creators")
    Single<TopCreatorResponse> getTopCreators(@Header("Unique-key") String apiKey, @Field("start") int start, @Field("count") int count, @Field("user_id") String toUserId);


    @FormUrlEncoded
    @POST("Post/follow_or_not")
    Single<Followerstatus> follow_OR_NOT(@Header("Unique-key") String apiKey, @Field("my_user_id") String my_user_id, @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("Post/delete_post")
    Single<RestResponse> deletePost(@Header("Unique-key") String apiKey,
                                    @Field("post_id") String postId);

//    @FormUrlEncoded
//    @POST("Post/like_unlike")
//    Single<RestResponse> likeUnlike(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
//                                    @Field("post_id") String postId);
//    @FormUrlEncoded
//    @POST("Post/add_like")
//    Single<RestResponse> likeUnlike(@Header("Unique-key") String apiKey, @Field("user_id") String userId,
//                                    @Field("post_id") String postId,@Field("first_name") String firstName);

    @FormUrlEncoded
    @POST("Post/like_unlike")
    Single<RestResponse> likeUnlike(@Header("Unique-key") String apiKey,
                                    @Field("post_id") String postId,
                                    @Field("first_name") String first_name,
                                    @Field("user_id") String userId);

    @Multipart
    @POST("User/user_update")
    Single<User> updateUser(@Header("Unique-key") String apiKey, @PartMap HashMap<String, RequestBody> hasMap);


    @Multipart
    @FormUrlEncoded()
    @POST("User/user_update")
    Single<User> updateUser(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
                            @PartMap HashMap<String, RequestBody> hasMap);

    @FormUrlEncoded
    @POST("Post/single_hash_tag_video")
    Single<Video> fetchHasTagVideo(@Header("Unique-key") String apiKey, @Field("hash_tag") String hashTag,
                                   @Field("count") int count,
                                   @Field("start") int start,
                                   @Field("my_user_id") String myUserId);

    @FormUrlEncoded
    @POST("Post/get_unique_key")
    Single<UploadVideoResponse> getUniqueKey(@Field("password") String password,
                                             @Field("user_email") String userEmail);


    //    @FormUrlEncoded
//    @POST("Post/hash_tag_search_video")
//    Single<Video> searchVideo(@Header("Unique-key") String apiKey, @Field("keyword") String keyword,
//                              @Field("count") int count,
//                              @Field("start") int start,
//                              @Field("my_user_id") String myUserId);
    @FormUrlEncoded
    @POST("Post/video_search")
    Single<Video> searchVideo(@Header("Unique-key") String apiKey, @Field("keyword") String keyword,
                              @Field("count") int count,
                              @Field("start") int start,
                              @Field("user_id") String myUserId);

    @FormUrlEncoded
    @POST("User/user_details")
    Single<User> getUserDetails(@Header("Unique-key") String apiKey, @Field("user_id") String userId,
                                @Field("my_user_id") String myUserId);

    @FormUrlEncoded
    @POST("Post/user_videos")
    Single<Video> getUserVideos(@Header("Unique-key") String apiKey, @Field("user_id") String userId,
                                @Field("count") int count,
                                @Field("start") int start,
                                @Field("my_user_id") String myUserId);

    @FormUrlEncoded
    @POST("Post/user_videos")
    Single<Video> getUserProfileVideos(@Header("Unique-key") String apiKey, @Field("user_id") String userId,
                                       @Field("count") int count,
                                       @Field("start") int start,
                                       @Field("my_user_id") String myUserId);

//    @FormUrlEncoded
//    @POST("Post/user_videos")
//    Single<MediaPostResponse> getMediaFeed(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @FormUrlEncoded
    @POST("Post/user_likes_videos")
    Single<Video> getUserLikedVideos(@Header("Unique-key") String apiKey, @Field("user_id") String userId,
                                     @Field("count") int count,
                                     @Field("start") int start,
                                     @Field("my_user_id") String myUserId);

    @FormUrlEncoded
    @POST("Post/user_likes_videos")
    Single<Video> getProfileUserLikedVideos(@Header("Unique-key") String apiKey, @Field("user_id") String userId,
                                            @Field("count") int count,
                                            @Field("start") int start,
                                            @Field("my_user_id") String myUserId);


    @FormUrlEncoded
    @POST("Post/explore_hash_tag_video")
    Single<Explore> getExploreVideos(@Header("Unique-key") String apiKey, @Field("count") int count,
                                     @Field("start") int start,
                                     @Field("my_user_id") String myUserId,
                                     @Field("keyword") String keyword);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @FormUrlEncoded
    @POST("Post/sound_video")
    Single<Video> getSoundVideos(@Header("Unique-key") String apiKey, @Field("count") int count,
                                 @Field("start") int start,
                                 @Field("sound_id") String soundId,
                                 @Field("my_user_id") String myUserId);

//    @FormUrlEncoded
//    @POST("Post/post_list")
//    Single<Video> getPostVideos(@Header("Unique-key") String apiKey,
//                                @Field("type") String type,
//                                @Field("page_count") int start,
//                                @Field("user_id") String myUserId,
//                                @Field("lat") Double lat,
//                                @Field("lng") Double lng);

    @FormUrlEncoded
    @POST("Post/post_list")
    Single<Video> getPostVideos(@Header("Unique-key") String apiKey,
                                @Field("type") String type,
                                @Field("count") int count,
                                @Field("start") int start,
                                @Field("user_id") String myUserId);

    @FormUrlEncoded
    @POST("Post/post_list")
    Single<Video> getPostDeepLinkVideos(@Header("Unique-key") String apiKey,
                                        @Field("type") String type,
                                        @Field("count") int count,
                                        @Field("start") int start,
                                        @Field("user_id") String myUserId,
                                        @Field("deep_link_id") String DeeplinkingId,
                                        @Field("lat") Double lat,
                                        @Field("lng") Double lng);
/*    @FormUrlEncoded
    @POST("Post/latest_post_list")
    Single<Video> getPostVideos(@Header("Unique-key") String apiKey,
                                @Field("type") String type,
                                @Field("count") int count,
                                @Field("page_count") int page_count,
                                @Field("start") int start,
                                @Field("user_id") String myUserId);*/

    /*@FormUrlEncoded
    @POST("Post/latest_post_list")
    Single<Video> getPostVideos(@Header("Unique-key") String apiKey,
                                @Field("type") String type,
                                @Field("count") int count,
                                @Field("start") int start,
                                @Field("user_id") String myUserId);*/

    @FormUrlEncoded
    @POST("Post/post_list_by_id")
    Single<Video> getPostById(@Header("Unique-key") String apiKey, @Field("post_id") String postId);

    @FormUrlEncoded
    @POST("Post/post_list_by_id_new")
    Single<Video> getPostByIdNew(@Header("Unique-key") String apiKey, @Field("post_id") String postId, @Field("user_id") String userId);

    @POST("User/profile_category_list")
    Single<ProfileCategory> getProfileCategoryList(@Header("Unique-key") String apiKey, @Header("Secret-key") String token);

    @Multipart
    @POST("Post/video_upload_to_s3")
    Single<UploadVideoResponse> getVideoPostUrl(@Header("Unique-key") String apiKey, @Part MultipartBody.Part postVideo);

    @Multipart
    @POST("Post/video_upload_with_image_s3")
    Single<UploadVideoThumbnailResponse> getVideoAndThumbnailUrl(@Header("Unique-key") String apiKey, @Part MultipartBody.Part postVideo, @Part MultipartBody.Part thumbnail);


   /*
   @Multipart
    @POST("Post/add_post")
    Single<User> uploadPost(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
                            @PartMap HashMap<String, RequestBody> hasMap,
                            @Part MultipartBody.Part postVideo,
                            @Part MultipartBody.Part postImage,
                            @Part MultipartBody.Part postSound,
                            @Part MultipartBody.Part soundImage);
                            */

    @FormUrlEncoded
    @POST("Post/add_post")
    Single<AddPostResponse> uploadPost(@Header("Unique-key") String apiKey,
                                       @FieldMap HashMap<String, Object> hasMap);

    @FormUrlEncoded
    @POST("Post/commet_list")
    Single<Comment> getPostComments(@Header("Unique-key") String apiKey, @Field("post_id") String postId,
                                    @Field("count") int count,
                                    @Field("start") int start);

    @FormUrlEncoded
    @POST("Post/delete_comment")
    Single<RestResponse> deleteComment(@Header("Unique-key") String apiKey, @Field("comments_id") String commentsId, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Post/following_list")
    Single<Follower> getFollowingList(@Header("Unique-key") String apiKey,
                                      @Field("user_id") String userId,
                                      @Field("count") int count,
                                      @Field("start") int start,
                                      @Field("my_user_id") String my_user_id);

    @FormUrlEncoded
    @POST("Post/following_list_for_other")
    Single<Follower> getFollowingListForOther(@Header("Unique-key") String apiKey,
                                              @Field("user_id") String userId,
                                              @Field("count") int count,
                                              @Field("start") int start,
                                              @Field("my_user_id") String my_user_id);

    /*@FormUrlEncoded
    @POST("Post/follower_list")
    Single<Follower> getFollowerList(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
                                     @Field("user_id") String userId,
                                     @Field("count") int count,
                                     @Field("start") int start);*/
    @FormUrlEncoded
    @POST("Post/follower_list")
    Single<Follower> getFollowerList(@Header("Unique-key") String apiKey,
                                     @Field("user_id") String userId,
                                     @Field("count") int count,
                                     @Field("start") int start,
                                     @Field("my_user_id") String my_user_id);


    @FormUrlEncoded
    @POST("Post/follower_list_for_other")
    Single<Follower> getFollowerListForOther(@Header("Unique-key") String apiKey,
                                             @Field("user_id") String userId,
                                             @Field("count") int count,
                                             @Field("start") int start,
                                             @Field("my_user_id") String my_user_id);

    @FormUrlEncoded
    @POST("Post/report")
    Single<RestResponse> reportSomething(@Header("Unique-key") String apiKey, @FieldMap HashMap<String, Object> fieldMap);

    //    @GET("Wallet/my_wallet_coin")
//    Single<MyWallet> getMyWalletDetails(@Header("Unique-key") String apiKey, @Header("Secret-key") String token);
//
//    @GET("Wallet/coin_rate")
//    Single<CoinRate> getCoinRate(@Header("Unique-key") String apiKey, @Header("Secret-key") String token);

  /*  @FormUrlEncoded
    @POST("User/notification_list")
    Single<Notification> getNotificationList(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
                                             @Field("user") String userId,
                                             @Field("count") int count,
                                             @Field("start") int start);*/

    @FormUrlEncoded
    @POST("User/notification_list")
    Single<Notification> getNotificationList(@Header("Unique-key") String apiKey,
                                             @Field("user_id") String user_id,
                                             @Field("count") int count,
                                             @Field("start") int start);


    @POST("/notification/all")
    Single<AllNotificationResponse> getAllNotification(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @FormUrlEncoded
    @POST("/follows/followsData")
    Single<FollowerListResponse> followsData(@HeaderMap Map<String, String> headers, @Field("userId") String userId);

    @POST("/notification/remove/one")
    Single<ApiResponse> deleteNotification(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    //bvdfkvbdfkvbdkfbvbvdsfkbvfd
    @FormUrlEncoded
    @POST("Post/increase_video_view")
    Single<RestResponse> increaseView(@Header("Unique-key") String apiKey, @Field("post_id") String postId);

    @Multipart
    @POST("User/verify_request")
    Single<RestResponse> verifyRequest(@Header("Unique-key") String apiKey,
                                       @PartMap HashMap<String, RequestBody> hasMap);

    @FormUrlEncoded
    @POST("User/submit_aadhar_otp")
    Single<OtpResponse> verifyOtp(@Header("Unique-key") String apiKey,
                                  @Field("user_id") String user_id,
                                  @Field("otp") String otp,
                                  @Field("client_id") String client_id);

    //
    //    @FormUrlEncoded
    //    @POST("Post/category_wise_sound_list")
    //    Single<SearchMusic> categoryWiseSoundList(@Header("Unique-key") String apiKey, @Header("Secret-key") String token,
    //                                              @Field("sound_category_id") String deviceToken);
    @FormUrlEncoded
    @POST("Post/category_wise_sound_list")
    Single<SearchMusic> categoryWiseSoundList(@Header("Unique-key") String apiKey,
                                              @Field("sound_category_id") String deviceToken);

    //    =============================wallet Api Start==============================================================
    @FormUrlEncoded
    @POST("Wallet/my_wallet_coin")
    Single<MyWallet> getMyWalletDetails(@Header("Unique-key") String apiKey, @Field("user_id") String userId);//working api

    @GET("Wallet/coin_rate")
    Single<CoinRate> getCoinRate(@Header("Unique-key") String apiKey);//working api

    @FormUrlEncoded
    @POST("Wallet/purchase_coin")
    Single<RestResponse> purchaseCoin(@Header("Unique-key") String apiKey,
                                      @Field("coin") String coinAmount,
                                      @Field("user_id") String userId);//working api

    @FormUrlEncoded
    @POST("User/test_paytm_integration")
    Single<DiamondPurchase> test_paytmCheckSum(@Header("Unique-key") String apiKey,
                                               @Field("amount") String amount,
                                               @Field("user_id") String user_id,
                                               @Field("quantity") String diamondQuantity,
                                               @Field("diamond_id") String diamondId);//working api

    @FormUrlEncoded
    @POST("User/paytm_integration")
    Single<DiamondPurchase> paytmCheckSum(@Header("Unique-key") String apiKey,
                                          @Field("amount") String amount,
                                          @Field("user_id") String user_id,
                                          @Field("quantity") String diamondQuantity,
                                          @Field("diamond_id") String diamondId);//working api

    @FormUrlEncoded
    @POST("User/paytm_transaction")
    Single<PaytmTransaction> paytmTransaction(@Header("Unique-key") String apiKey,
                                              @Field("user_id") String user_id,
                                              @Field("order_id") String order_id,
                                              @Field("txn_id") String txn_id,
                                              @Field("payment_gateway") String payment_gateway);

    @FormUrlEncoded
    @POST("User/active_gateway")
    Single<ActiveGatewayResponse> activeGateway(@Header("Unique-key") String apiKey,
                                                @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Post/getlanguagebasisData")
    Single<LanguageResponseMedley> getLanguage(@Header("Unique-key") String apiKey,
                                               @Field("user_id") String user_id,
                                               @Field("language_name") String diamondQuantity);

    @FormUrlEncoded
    @POST("User/test_cashfree_integration")
    Single<CashFreeTransaction> test_cashFreeCheckSum(@Header("Unique-key") String apiKey,
                                                      @Field("amount") String amount,
                                                      @Field("user_id") String user_id,
                                                      @Field("quantity") String diamondQuantity,
                                                      @Field("diamond_id") String diamondId);//working api

    @FormUrlEncoded
    @POST("User/cashfree_integration")
    Single<CashFreeTransaction> cashFreeCheckSum(@Header("Unique-key") String apiKey,
                                                 @Field("amount") String amount,
                                                 @Field("user_id") String user_id,
                                                 @Field("quantity") String diamondQuantity,
                                                 @Field("diamond_id") String diamondId);//working api


    @FormUrlEncoded
    @POST("Wallet/coin_plan")
    Single<CoinPlan> getCoinPlans(@Header("Unique-key") String apiKey, @Field("user_id") String userId);//working api

    @FormUrlEncoded
    @POST("Wallet/send_coin")
    Single<RestResponse> sendCoin(@Header("Unique-key") String apiKey,
                                  @Field("coin") String coin,
                                  @Field("to_user_id") String toUserId,// working
                                  @Field("user_id") String myUserId);

    @FormUrlEncoded
    @POST("Wallet/redeem_request")
    Single<RestResponse> sendRedeemRequest(@Header("Unique-key") String apiKey,
                                           @Field("amount") String redeemCoinPrice,
                                           @Field("coins") String redeemCoin,
                                           @Field("redeem_request_type") String requestTypeType,
                                           @Field("user_id") String userId,
                                           @Field("account") String account);

    @FormUrlEncoded
    @POST("Wallet/add_coin")
    Single<RestResponse> rewardUser(@Header("Unique-key") String apiKey,
                                    @Field("user_id") String userId,
                                    @Field("rewarding_action_id") String rewardActionId);

    @FormUrlEncoded
    @POST("Wallet/rewarding_action")
    Single<RewardingActions> getRewardingAction(@Header("Unique-key") String apiKey, @Field("user_id") String userId);

//    =============================================wallet Api End=====================================

    //=============================================MAYANK NEW CODE====================================
    @Multipart
    @POST("User/file_upload_to_s3_part2")
    Single<UploadImageResponse> uploadImageRequest(@Header("Unique-key") String apiKey, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("Post/add_view")
    Single<AdsModel> addView(@Header("Unique-key") String apiKey, @Field("post_id") String postId, @Field("user_id") String userId);

    @Multipart
    @POST("Post/data_upload_to_s3")
    Single<DataUploadToS3> getDataUploadToS3(@Header("Unique-key") String apiKey,
                                             @Part MultipartBody.Part postVideo,
                                             @Part MultipartBody.Part thumbnail,
                                             @Part MultipartBody.Part sound);

    @FormUrlEncoded
    @POST("Post/add_share")
    Single<ShareResponse> sharePost(@Header("Unique-key") String apiKey,
                                    @Field("from_user_id") String userId,
                                    @Field("type") String first_type,
                                    @Field("post_or_user_id") String postOrUserId);



    @POST("/pub/loginUser")
    Single<LoginResponse> login(@Body HashMap<String, String> body);

    @POST("/pub/verifyUsername")
    Single<VerifyUserNameResponse> verifyUsername(@Body VerifyUsernameParam verifyParam);

    @POST("/pub/register")
    Single<RegisterNewResponse> register_user(@Body HashMap<String, String> body);

    @POST("/pub/verifyAll")
    Single<VerifyEmailResponse> verifyAll(@Body VerifiyEmailAndMobileParam verifyParam);

    @POST("/user/get/activityHistory")
    Single<UserActivityResponse> getActivityHistory(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/postSaved/getSaved")
    Single<SavedDataResponse> getSavedData(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/userBlockList/get")
    Single<UserBlockListResponse> getBlockedUserList(@HeaderMap Map<String, String> headers);

    @POST("/pub/forgotPassword")
    Single<ForogtPasswordMobile> forgotPassword(@Body VerifiyMobileParam verifyParam);

    @POST("/pub/verifyOTP")
    Single<VerifyOtpResponse> forgotPasswordVerifyOTP(@Body ForgotPasswordOtpVerify forgotPasswordOtpVerify);


    Single<VerifyOtpResponse> forgotPasswordverifyOTP(@Body ForgotPasswordOtpVerify forgotPasswordOtpVerify);

    @POST("/pub/verifyOtpAll")
    Single<OtpVerificartionResponse> otpVerification(@Body MobileOtpVerificationParam verifyParam);

    @POST("/pub/resetPassword")
    Single<LoginResponse> resetPassword(@Body ResetPasswordParam resetPasswordParam);

    @POST("/user/update")
    Single<UserSettingUpdateResponse> updateUserSettings(@HeaderMap Map<String, String> headers, @Body UserSettingParams data);

    @POST("/user/me")
    Single<UserSettingsResponse> getUserSettings(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/topics/getAll")
    Single<TopicsResponse> getAll_topic(@HeaderMap Map<String, String> headers, @Body TopicParam topicParam);

    @POST("/userTopics/insert")
    Single<UpdateSelectedTopicsResponse> userTopics_insert(@HeaderMap Map<String, String> headers, @Body ChooseTopicResponse chooseTopicResponse);

    @POST("user/setDefaultApp")
    Single<DefaultAppResponse> defaultApp(@HeaderMap Map<String, String> headers, @Body DefaultParam defaultParam);

    @POST("/user/update")
    Single<ApiResponse> update_profile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/admin/updateUser")
    Single<ApiResponse> delUserAccount(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/user/update")
    Single<ApiResponse> updateUserProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/trending/get/topPosts")
    Single<TopResponse> hashTagTop(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("search/hashtags")
    Single<HashtagSearch> searchHashTag(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/trending/get/trendingPosts")
    Single<TopResponse> hashTagMostTrending(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/report/getByType")
    Single<ReportTypeResponse> getReportTypes(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/userBlockList/removeUser")
    Single<RemoveBlockedUserResponse> removeBlockedUserProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/userBlockList/add")
    Single<BlockUserResponse> blockUser(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/user/get/userProfileById")
    Single<UserProfileRespone1> userProfile(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/follows/following")
    Single<ApiResponse> followingRequest(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/follows/followingStatus")
    Single<UnfollowResponse> followingStatus(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("chat/following")
    Single<MessageResponse> message(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/chat/chatHeads")
    Single<CreateGroupResponse> createChatHeads2(@HeaderMap Map<String, String> headers, @Body() Map map);

    @POST("/user/get/userMedia")
    Single<MediaPostResponse1> getUserMedia(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("post/get/tagPost")
//    Single<ProfilesVideoResponse1> tagUserMedia(@HeaderMap Map<String, String> headers, @Field("userId") String userId);
    Single<ProfilesVideoResponse1> tagUserMedia(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("post/get/user/admin/media")
    Single<ProfilesVideoResponse1> videoTabUserMedia(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/admin/allUsers")
    Single<FriendsListResponse> searchFriendsList(@HeaderMap Map<String, String> headers, @Body Map<String, String> body);

    @POST("/trending/get/trendingPeoples")
    Single<PeopleSearchResponse> searchPeople(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/search/peoples")
    Single<PeopleSearchResponse> searchPeoplelo(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @GET()
    Single<ElasticSearchRespone> elasticSearch(@Url String str);

    @POST("/user/get/userProfileById")
    Single<UserProfileRespone1> getUserProfileData(@HeaderMap Map<String, String> headers);

    @POST("/user/changePassword")
    Single<UpdatePasswordResponse1> updateUserPassword(@HeaderMap Map<String, String> headers, @Body UpdatePasswordParams data);

    @POST("/search/videos")
    Single<VideoSearchResponse> searchVideo(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/search/images")
    Single<VideoSearchResponse> searchImages(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("search/hashtags")
    Single<HashtagSearchResponse> searchHashTags(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/insertComment")
    Single<com.meest.social.socialViewModel.model.comment.ApiResponse> insertComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/getComments")
    Single<VideoCommentResponse> getComments(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/insert/comment")
    Single<com.meest.social.socialViewModel.model.comment.ApiResponse> insertAdComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/get/comments")
    Single<VideoCommentResponse> getAdComments(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/insert/comment")
    Single<VideoAddCommentResponse> addVideoComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/get/comments")
    Single<VideoCommentResponse> getVideoComments(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/insertSubComment")
    Single<ApiResponse> insertSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/getSubComment")
    Single<FeedSubCommentResponse> getFeedSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/insert/sub/comment")
    Single<ApiResponse> insertAdSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/get/sub/comments")
    Single<FeedSubCommentResponse> getAdFeedSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);


    @POST("/video/insert/sub/comment")
    Single<SubCommentResponse> addVideoCommentReply(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/get/sub/comments")
    Single<SvsSubCommentResponse> getSvsSubComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/video/insert/comment/like")
    Single<VideoCommentLikeResponse> getAllVideoCommentLike(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/advertise/insert/comment/like")
    Single<VideoCommentLikeResponse> insertAdPostCommentLike(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/post/commentLike")
    Call<VideoCommentLikeResponse> insertPostCommentLike(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/report/getByType")
    Single<ReportTypeNewResponse> getReportNewTypes(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);

    @POST("/video/delete/comment")
    Single<ApiResponse> deleteSvsComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/advertise/delete/comment")
    Single<ApiResponse> deleteAdPostComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/removeComment")
    Single<ApiResponse> deletePostComment(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> body);

    @POST("/post/commentLike")
    Single<VideoCommentLikeResponse> insertPostCommentsLike(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> body);
}
