package com.togethernet.togethernet.GlobalApp.Preferences;

import android.content.SharedPreferences;
import android.content.Context;


public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "intro_slider-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String AUTOMATIC_CONNECTION = "AutomaticConnection";

    private static final String AUTOMATIC_SEARCH = "AutomaticSearch";

    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    // TODO -> Implementare completamente le impostazioni dell'app da qui

    public void setAtuomaticConnection(boolean setting) {
        editor.putBoolean(AUTOMATIC_CONNECTION, setting);
        editor.commit();
    }

    public void setAutomaticSearch(boolean setting) {
        editor.putBoolean(AUTOMATIC_SEARCH, setting);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isAutomaticConnectionSetted() { return pref.getBoolean(AUTOMATIC_CONNECTION, true); }
}