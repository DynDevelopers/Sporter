package com.dynamo.sporter.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dynamo.sporter.LoginFragment;
import com.dynamo.sporter.model.Player;
import com.google.firebase.auth.FirebaseAuth;


public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "user";
    private static final String KEY_USERID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PREFERRED_SPORTS = "preferredSports";
    private static final String KEY_AGE = "age";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONENO = "phoneNo";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_STATE = "state";
    private static final String KEY_CITY = "city";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_CLUBID = "clubid";
    private static final String KEY_CLUBADMIN = "clubadmin";
    private static final String KEY_DESCRIPTION = "description";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if ( mInstance == null)
            return (mInstance = new SharedPrefManager(context));
        return mInstance;
    }

    public boolean userLogin(Player player) {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();

        editor.putString(KEY_USERID, player.getId());
        editor.putString(KEY_PREFERRED_SPORTS, player.getPreferredSports());
        editor.putString(KEY_AGE, player.getAge());
        editor.putString(KEY_FIRSTNAME, player.getFirstname());
        editor.putString(KEY_LASTNAME, player.getLastname());
        editor.putString(KEY_EMAIL, player.getEmail());
        editor.putString(KEY_PHONENO, player.getPhoneNo());
        editor.putString(KEY_COUNTRY, player.getCountry());
        editor.putString(KEY_STATE, player.getState());
        editor.putString(KEY_CITY, player.getCity());
        editor.putString(KEY_GENDER, player.getGender());
        editor.putString(KEY_PINCODE, player.getPincode());
        editor.putString(KEY_CLUBID, player.getClubid());
        editor.putBoolean(KEY_CLUBADMIN, player.isClubAdmin());
        editor.putString(KEY_DESCRIPTION, player.getDescription());

        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (preferences.getString(KEY_USERID, null) != null);
    }

    public boolean logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUserId() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USERID, null);
    }

    public String getClubID() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_CLUBID, null);
    }

    public String getFirstname() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_FIRSTNAME, null);
    }

    public String getLastname() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_LASTNAME, null);
    }

    public String getEmail() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_EMAIL, null);
    }

    public String getPhoneNo() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_PHONENO, null);
    }

    public String getCountry() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_COUNTRY, null);
    }

    public String getState() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_STATE, null);
    }

    public String getCity() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_CITY, null);
    }

    public String getPincode() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_PINCODE, null);
    }

    public String getDescription() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_DESCRIPTION, null);
    }

    public String getGender() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_GENDER, null);
    }

    public boolean isClubAdmin() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_CLUBADMIN, false);
    }

    public String getAge() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_AGE, null);
    }

    public String getPreferredSport() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_PREFERRED_SPORTS, null);
    }

    public boolean updateClubID(String clubID) {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CLUBID, clubID);
        editor.apply();
        return true;
    }

    public boolean updateClubAdmin(boolean isClubAdmin) {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_CLUBADMIN, isClubAdmin);
        editor.apply();
        return true;
    }
}
