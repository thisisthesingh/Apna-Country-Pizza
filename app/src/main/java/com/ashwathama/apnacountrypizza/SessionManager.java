package com.ashwathama.apnacountrypizza;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_PHONE = "phoneNo";
    public static final String KEY_PASSWORD = "pass";

    public SessionManager(Context _context) {
        context = _context;
        usersSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String fullName, String phoneNo, String pass) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_PHONE, phoneNo);
        editor.putString(KEY_PASSWORD, pass);

        editor.commit();

    }

    public HashMap<String, String> getUsersDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_PHONE, usersSession.getString(KEY_PHONE, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));

        return userData;
    }

    public boolean checkLogin() {

        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }

    }

    public void logoutUserFromSessiom() {
        editor.clear();
        editor.commit();
    }

}
