package com.geargames.common.billing;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 08.10.12
 * Time: 10:46
 * интерфейс биллинговых агрегаторов
 */
public abstract class Billing {

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onDestroy();

    public abstract boolean checkBillingSupported();

    public static final int GOOGLE_MARKET = 0;
    public static final int APPLESTORE = 1;
    public static final int PAYPAL = 2;
    public static final int GG = 3;

}
