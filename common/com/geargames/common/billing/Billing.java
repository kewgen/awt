package com.geargames.common.billing;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 08.10.12
 * Time: 10:46
 * интерфейс биллинговых агрегаторов
 */
public abstract class Billing {

    public abstract void onStart(Object activity);

    public abstract void onStop();

    public abstract void onDestroy();

    //get answer from services
    public abstract void onActivityResult(int requestCode, int resultCode, Object data);

    public abstract boolean checkBillingSupported();

    //verify on unfinished purchase
    public abstract com.geargames.common.String getAndCleanSavedPurchase();

    public static final int GOOGLE_MARKET = 0;
    public static final int APPLESTORE = 1;
    public static final int PAYPAL = 2;
    public static final int GG = 3;
    public static final int AMAZON = 4;

}
