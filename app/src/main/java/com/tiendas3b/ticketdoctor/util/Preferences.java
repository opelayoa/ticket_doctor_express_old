package com.tiendas3b.ticketdoctor.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dfa on 29/02/2016.
 */
public class Preferences {

    public static final String FILE_PREFERENCES = "ticket_doctor";
//    public static final String KEY_FAKE_PASS = "Pass";
//    public static final String KEY_FAKE_USU = "User";
    public static final String KEY_NAME = "A";
    public static final String KEY_LAST_NAME = "B";
    public static final String KEY_PASS = "C";
    public static final String KEY_LOGIN = "D";
    public static final String KEY_ALIAS = "E";
    public static final String KEY_PROFILE = "F";
    private final Context mContext;

    public Preferences(Context context) {
        mContext = context;
    }

    public void saveData(String user, String password, long profileId) {
        setSharedStringSafe(KEY_LOGIN, user);
        setSharedStringSafe(KEY_PASS, password);
        setSharedLongSafe(KEY_PROFILE, profileId);
//        setSharedStringSafe(KEY_LOGIN, user);
//        setSharedStringSafe(KEY_PASS, password);
//        Encryption encryption = new Encryption();
//        setSharedStringSafe(KEY_FAKE_USU, encryption.generateRandomString());
//        setSharedStringSafe(KEY_FAKE_PASS, encryption.generateRandomString());

    }

    private void setSharedLongSafe(String key, long value) {
        setSharedString(key, new Encryption().encrypt(String.valueOf(value)));
    }

    public void deleteData() {
        removeShared(KEY_LOGIN);
        removeShared(KEY_PASS);
        removeShared(KEY_PROFILE);
//        removeShared(KEY_FAKE_USU);
//        removeShared(KEY_FAKE_PASS);
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences goSharedPreferences = mContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        return goSharedPreferences.edit();
    }

    public void setSharedStringSafe(String key, String value) {
        setSharedString(key, new Encryption().encrypt(value));
    }

    public void setSharedString(String key, String value) {
        SharedPreferences.Editor voEditor = getEditor();
        voEditor.putString(key, value);
        voEditor.commit();
    }

    public String getSharedStringSafe(String key, String defaultValue) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences pref = mContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        String str = new Encryption().decrypt(pref.getString(key, defaultValue));
        return str == null ? defaultValue : str;
    }

    public void removeShared(String key) {
        SharedPreferences.Editor voEditor = getEditor();
        voEditor.remove(key);
        voEditor.commit();
    }

    public void removeSharedFile(String psFile) {
        SharedPreferences.Editor voEditor = getEditor();
        voEditor.clear();
        voEditor.commit();
    }

    public long getSharedLongSafe(String key, long defaultValue) {
        SharedPreferences pref = mContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        String str = new Encryption().decrypt(pref.getString(key, ""));
        return str == null ? defaultValue : Long.parseLong(str);
    }
}
