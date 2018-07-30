package com.ingic.saeedni.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ingic.saeedni.activities.MainActivity;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.UserEnt;
import com.ingic.saeedni.retrofit.GsonFactory;

import java.util.Locale;


public class BasePreferenceHelper extends PreferenceHelper {

    protected static final String KEY_LOGIN_STATUS = "islogin";
    protected static final String KEY_TERMS_STATUS = "isTerm";
    protected static final String _TOKEN = "token";
    protected static final String Firebase_TOKEN = "Firebasetoken";
    protected static final String KEY_TWITTER_LOGN = "isTwitterLogin";
    protected static final String USERNAME = "userName";
    protected static final String USERID = "userId";
    protected static final String KEY_USER = "key_user";
    protected static final String BADGE_COUNT = "BADGE_COUNT";
    protected static final String KEY_DEFAULT_LANG = "keyLanguage";
    private static final String UserType = "User";
    private static final String FILENAME = "preferences";
    private static final String PHONENUMBER = "PHONENUMBER";
    private Context context;


    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public static String getUSERID() {
        return USERID;
    }


    public void setIsTwitterLogin(boolean isTwitterLogin) {
        putBooleanPreference(context, FILENAME, KEY_TWITTER_LOGN, isTwitterLogin);
    }

    public boolean isTwitterLogin() {
        return getBooleanPreference(context, FILENAME, KEY_TWITTER_LOGN);
    }

    public Integer getBadgeCount() {
        return getIntegerPreference(context, FILENAME, BADGE_COUNT);
    }

    public void setBadgeCount(Integer _BADGE_COUNT) {
        putIntegerPreference(context, FILENAME, BADGE_COUNT, _BADGE_COUNT);
    }

    public String getToken() {
        return getStringPreference(context, FILENAME, _TOKEN);
    }

    public void setToken(String _token) {
        putStringPreference(context, FILENAME, _TOKEN, _token);
    }

    public String getFirebase_TOKEN() {
        return getStringPreference(context, FILENAME, Firebase_TOKEN);
    }

    public void setFirebase_TOKEN(String _token) {
        putStringPreference(context, FILENAME, Firebase_TOKEN, _token);
    }

    public void setUsrName(String _token) {
        putStringPreference(context, FILENAME, USERNAME, _token);
    }

    public String getUserName() {
        return getStringPreference(context, FILENAME, USERNAME);
    }

    public void setUsrId(String userId) {
        putStringPreference(context, FILENAME, USERID, userId);
    }

    public String getUserId() {
        return getStringPreference(context, FILENAME, USERID);
    }

    public UserEnt getUser() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_USER), UserEnt.class);
    }

    public void putUser(UserEnt user) {
        putStringPreference(context, FILENAME, KEY_USER, GsonFactory
                .getConfiguredGson().toJson(user));
    }

    public RegistrationResultEnt getRegistrationResult() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_USER), RegistrationResultEnt.class);
    }

    public void putRegistrationResult(RegistrationResultEnt user) {
        putStringPreference(context, FILENAME, KEY_USER, GsonFactory
                .getConfiguredGson().toJson(user));
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean isLogin) {
        putBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS, isLogin);
    }

    public boolean isLogin() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }

    public String getUserType() {
        return getStringPreference(context, FILENAME, UserType);
    }

    public void setUserType(String usertype) {
        putStringPreference(context, FILENAME, UserType, usertype);
    }

    public String getPhonenumber() {
        return getStringPreference(context, FILENAME, PHONENUMBER);
    }

    public void setPhonenumber(String phonenumber) {
        putStringPreference(context, FILENAME, PHONENUMBER, phonenumber);
    }

    public void putLang(Activity activity, String lang) {
        Log.v("lang", "|" + lang);

        if (lang.equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        putStringPreference(context, FILENAME, KEY_DEFAULT_LANG, lang);
        Locale locale = new Locale(lang);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.setLocale(locale);
        conf.locale = locale;
//        conf.setLayoutDirection(Locale.ENGLISH);
        resources.updateConfiguration(conf, dm);
        /*//Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        android.content.res.Configuration conf = resources.getConfiguration();
//        conf.setLocale(new Locale(lang));
        conf.locale = new Locale(lang);
        resources.updateConfiguration(conf, dm);*/
        ((MainActivity) activity).restartActivity();
    }

    public void putLang( String lang) {
        putStringPreference(context, FILENAME, KEY_DEFAULT_LANG, lang);

    }


    public String getLang() {
        return getStringPreference(context, FILENAME, KEY_DEFAULT_LANG);
    }

    public boolean isLanguageArabic() {
        return getLang().equalsIgnoreCase("ar");
    }


    public boolean istermsCheck() {
        return getBooleanPreference(context, FILENAME, KEY_TERMS_STATUS);

    }

    public void setTermsCheck(boolean isChecked) {
        putBooleanPreference(context, FILENAME, KEY_TERMS_STATUS, isChecked);
    }
}
