package Utils;

import android.content.SharedPreferences;

import application.MainApplication;

/**
 * Created by Ewan on 2017/1/14.
 */

public class PreferencesHelper {
    private static final String IS_LOGIN_PREFS = "Is_Login";

    private static final String PREF_KEY_USER_ID = "UserId";

    private  static final String PREF_KEY_DISPLAY_NAME = "DisplayName" ;

    private static  final String PREF_KEY_PROFILE_LINK = "Profile";

    public static Boolean getBoolean(String key, Boolean defValue) {
        return MainApplication.getPreferences().getBoolean(key, defValue);
    }

    public static void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = MainApplication.getPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(String key, String defValue) {
        return MainApplication.getPreferences().getString(key, defValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = MainApplication.getPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defValue) {
        return MainApplication.getPreferences().getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = MainApplication.getPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static boolean getLoginCheck() {
        return getBoolean(IS_LOGIN_PREFS, false);
    }

    public static void setLoginCheck(boolean tested) {
        putBoolean(IS_LOGIN_PREFS, tested);
    }

    public static String getUserID() {
        return getString(PREF_KEY_USER_ID, "");
    }

    public static void setUserID(String value) {
        putString(PREF_KEY_USER_ID, value);
    }

    public static  String getProfileLink(){return getString(PREF_KEY_PROFILE_LINK,"");}

    public static  void setProfileLink(String value){putString(PREF_KEY_PROFILE_LINK,value);}

    public static String getDisplayName(){
        return getString(PREF_KEY_DISPLAY_NAME,"");
    }

    public static void setDisplayName(String value){
        putString(PREF_KEY_DISPLAY_NAME,value);
    }
}
