package com.meest.meestbhart.register.fragment.otp.model;

public class RegisteP {
    String firstName;
    String lastName;
    String password;
    String username;
    String gender;
    String mobile;
    String email;

    String gToken;
    String fbToken;
    String location;
    String accountType;
    String deviceVoipToken;
    String friendReferral;
    String referral;

    String notification;
    String mediaAutoDownload;
    String dnd;
    String lat;
    String lag;
    String fireBaseToken;

    String deviceName;
    String deviceModel;

    String deviceVersion;
    String osType;
    String imeiNumber;
    String timeZone;
    String dateZone;


  //  String status;
    String dob;
    String fcmToken;

    public RegisteP(String firstName, String lastName, String password, String username, String gender,
                    String mobile, String email, String gToken, String fbToken, String location, String accountType,
                    String deviceVoipToken, String friendReferral, String referral, String notification, String mediaAutoDownload,
                    String dnd, String lat, String lag, String deviceName, String deviceModel, String deviceVersion,
                    String osType, String imeiNumber, String timeZone, String dateZone,  String dob, String fcmToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.gToken = gToken;
        this.fbToken = fbToken;
        this.location = location;
        this.accountType = accountType;
        this.deviceVoipToken = deviceVoipToken;
        this.friendReferral = friendReferral;
        this.referral = referral;
        this.notification = notification;
        this.mediaAutoDownload = mediaAutoDownload;
        this.dnd = dnd;
        this.lat = lat;
        this.lag = lag;
        this.fireBaseToken = fireBaseToken;
        this.deviceName = deviceName;
        this.deviceModel = deviceModel;
        this.deviceVersion = deviceVersion;
        this.osType = osType;
        this.imeiNumber = imeiNumber;
        this.timeZone = timeZone;
        this.dateZone = dateZone;
     //   this.status = status;
        this.dob = dob;
        this.fcmToken = fcmToken;
    }
}
