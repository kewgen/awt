package com.geargames.billing;

import android.app.Activity;
import android.os.Bundle;
import com.geargames.MIDlet;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 04.10.12
 * Time: 12:05
 * биллинг от 2pay
 */
public class GGBilling extends Billing {
    private MIDlet miDlet;

    public GGBilling(MIDlet miDlet) {

        this.miDlet = miDlet;
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onStart(Object activity) {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Object data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean checkBillingSupported() {
        return true;
    }

    @Override
    public void sendPay(Activity activity, Bundle bundle) {
        int payID = bundle.getInt(Billing.PAY_ID);
        int projectID = bundle.getInt(Billing.PROJECT_ID_IN_BILLING);//ид в системе 2pay
        int userID = bundle.getInt(Billing.USER_ID);
        String addInfo = bundle.getString(Billing.ADD_TO_URL);

        String str = url + "&id_project=" + projectID + "&v1=" + userID + addInfo;
        miDlet.platformRequest(com.geargames.common.String.valueOfC(str), false);
        //String.valueOfC("http://m.xsolla.com/index.php?action=pslist&id_project=7495").concatC("&v1=").concatI(getNetwork().getUid(false)).concatC("&out=").concatI(gold)
    }

    @Override
    public String toString() {
        return "GGBilling{" +
                "url='" + url + '\'' +
                '}';
    }

    //http://m.xsolla.com/index.php?action=pslist&id_project=7495&v1={UID}&out={GOLD}&local={LANG}&game={GAME}
    private String url = "http://m.xsolla.com/index.php?action=pslist";
}
