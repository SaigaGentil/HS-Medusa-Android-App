package com.example.login_register_me1;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Software created by Bruno Axel KAMERE on December 10th 2019 --Start date--
 * !!!This class handles session!!!
 */

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EXPIRES = "Expires";
    private static final String KEY_FIRST_NAME = "First_Name";
    private static final String KEY_LAST_NAME = "Last_Name";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * This logs in the user by saving the user's details and setting up a session
     * @param Username
     * @param Lastname
     *
     */
    public void LoginUser (String Username, String Lastname) {
        mEditor.putString(KEY_USERNAME, Username);
        mEditor.putString(KEY_LAST_NAME, Lastname);
        Date date = new Date();

        //Set user session for the next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * This checks whether the user is logged in or not
     * @return
     *
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value then the user is logged in */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /*Check if the session is expired by comparing the current date and session expiry date*/
        return currentDate.before(expiryDate);
    }

    /**
     * This fetches and returns user details
     *
     * @return user details
     */
     //Create a USER class first
    public User getUserDetails() {
        //Check if the user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setLastName(mPreferences.getString(KEY_LAST_NAME, KEY_EMPTY));
        user.setFirstname(mPreferences.getString(KEY_FIRST_NAME, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * This logs out the user by clearing the session
     *
     */
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
