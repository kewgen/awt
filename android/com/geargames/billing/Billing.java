package com.geargames.billing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.geargames.MIDlet;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 04.10.12
 * Time: 11:54
 * интерфейс биллинговых агрегаторов
 */
public abstract class Billing extends com.geargames.common.billing.Billing {

    public abstract void onCreate(Activity activity, Bundle savedInstanceState);

    public abstract void sendPay(Activity activity, Bundle bundle);

    //сохранение платежа на случай неудачной отправки
    protected void setSave(String lang) {
        SharedPreferences settings = activity.getSharedPreferences("gbil", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("save", lang);
        editor.commit();
    }

    protected String getSave() {
        SharedPreferences settings = activity.getSharedPreferences("gbil", 0);
        return settings.getString("save", null);
    }

    public com.geargames.common.String getAndCleanSavedPurchase() {
        String save = getSave();
        if (save == null) return null;
        setSave(null);
        return com.geargames.common.String.valueOfC(save);
    }

    protected MIDlet activity;

    //bundled keys
    public static final String PAY_ID = "PAY_ID";
    public static final String PROJECT_ID_IN_BILLING = "PROJECT_ID_IN_BILLING";//ид в конкретной системе биллинга
    public static final String USER_ID = "USER_ID";
    public static final String ADD_TO_URL = "ADD_TO_URL";

}
