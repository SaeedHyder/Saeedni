package com.ingic.saeedni.global;


public class AppConstants {

    public static String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    //Code For success and failure
    public static String CODE_SUCCESS = "Success";
    public static String CODE_FAILURE = "Error";

    //Activity result code
    public static final int CAMERA_REQUEST = 1001;
    public static final int GALLERY_REQUEST = 1002;
    public static final int VIDEO_REQUEST = 1003;
    public static final int CV_REQUEST = 1004;

    public static boolean is_show_trainer = false;
    public static String user_id;
    public static String _token;

    public static String TECHNICIAN = "technician";
    public static String USER = "user";

    public static String twitter = "twitter";
    public static String DateFormat_YMD = "yyyy-MM-dd";
    public static String DateFormat_DMY = "dd-MM-yyyy";

    public static String fixPassword = "123456";

    public static float zoomIn =20;

    //Firebase Constants
    // broadcast receiver intent filters
    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String Device_Type = "android";
    // id to handle the notification in the notification tray
    public static  int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final int CREATE_REQUEST = 0;
    public static final int ACCEPT_QUOTATION = 2;
    public static final int CANCEL_QUOTATION = 3;
    public static final int CANCEL_JOB = 4;
    public static final int ACCEPT_QUOTE = 5;
    public static final int TECH_ACCEPT_ASSIGN_JOB = 6;
    public static final int COMPLETE_JOB = 7;
    public static final int TECH_ACCEPT_JOB = 1;
    public static final int TECH_REJECT_JOB = 2;
    public static final int TECH_MARK_COMPLETE = 1;




}
