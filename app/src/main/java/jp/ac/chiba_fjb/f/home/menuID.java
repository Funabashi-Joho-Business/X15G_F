package jp.ac.chiba_fjb.f.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by x15g010 on 2017/11/24.
 */

public class menuID {
    private String menuid ="";

    public String getMenuid(){
        return this.menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
}
