package com.geargames;

import android.accounts.Account;
import android.app.Activity;
import android.widget.VideoView;
import com.geargames.common.*;
import com.geargames.common.String;
import com.geargames.packer.Canvas;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 11.10.12
 * Time: 12:59
 * портинг на старые версии
 */
public class PortVersion {


    //портирование констант и методов под старые версии
    public static void videoViewResume(VideoView videoView) {
        if (PortPlatform.getLevelAPI() >= PortPlatform.FROYO)
            videoView.resume();
    }

    public static String getUserName(Activity activity, String type) {
        if (type == null) type = String.valueOfC("com.google");
        String name = String.valueOfC("");
        try {
            android.accounts.AccountManager am = android.accounts.AccountManager.get(activity); // "this" references the current Context
            Account[] accounts = am.getAccountsByType(type.toString());
            for (Account account : accounts) {
                if (name.length() == 0) name = String.valueOfC(account.name);
                Debug.trace(account.toString());
            }
        } catch (Exception e) {
            Debug.logEx(e);
        }
        return name;
    }

    public static void setPreserveEGLContextOnPause(Canvas canvas, boolean b) {
        if (PortPlatform.getLevelAPI() >= PortPlatform.HONEYCOMB) canvas.setPreserveEGLContextOnPause(b);
    }

}
