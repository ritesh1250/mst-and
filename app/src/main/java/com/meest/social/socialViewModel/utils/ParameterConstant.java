package com.meest.social.socialViewModel.utils;

public interface ParameterConstant {

    public interface SocialPrefrences {
        String SOCIAL_PREFRENCES = "socialprefrences";
        String USER_ID = "userid";

        String USER= "user";
        String VIDEO= "video";
        String POST= "post";

        //Gender Dilogue
        String MALE="male";
        String FEMALE="female";
        String OTHER="Prefer not to disclose";
    }

    public interface Permission{
        int PERMISSION_CODE = 123;
        int CAMERA_PERMISSION_CODE = 124;
        int STORAGE_PERMISSION_CODE = 127;
        int VIDEO_PERMISSION_CODE = 125;
        int SVS_VIDEO_PERMISSION_CODE = 126;
        int REQUEST_LOCATION = 1;
        int PICK_MEDIA_CODE = 10;
    }
}
