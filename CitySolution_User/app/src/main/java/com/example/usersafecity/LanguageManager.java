package com.example.usersafecity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class LanguageManager {

    private Context ct;
    private SharedPreferences sharedPreferences;

    public LanguageManager(Context ct) {
        this.ct = ct;
        sharedPreferences=ct.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void updateResource(String code)
    {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources= ct.getResources();
        Configuration configuration=resources.getConfiguration();
        configuration.locale=locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        setLang(code);
    }

    public String getLang()
    {
        return sharedPreferences.getString("langg","en");
    }
    public void setLang(String code)
    {

        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("langg",code);
        ed.commit();

    }


}
