package com.geargames.billing.amazon;

import com.amazon.inapp.purchasing.BasePurchasingObserver;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.geargames.MIDlet;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 26.12.12
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
 class MyObserver/* extends BasePurchasingObserver*/ {

    public MyObserver() {

//        super(MIDlet.this);

    }

    public void onItemDataResponse(ItemDataResponse itemDataResponse) {

        //Check itemDataResponse.getItemDataRequestStatus();
        //Use itemDataResponse to populate catalog data

    }

    public void onPurchaseResponse(PurchaseResponse purchaseResponse) {

        //Check purchaseResponse.getPurchaseRequestStatus();
        //If SUCCESSFUL, fulfill content;

    }
}
