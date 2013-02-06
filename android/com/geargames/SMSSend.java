//
package com.geargames;

import android.telephony.SmsManager;
import com.geargames.common.String;

public class SMSSend implements Runnable {

    public Manager canv;
    public Thread subThread;

    public String msg;

    public String phone = com.geargames.common.String.valueOfC("");

    public boolean sms_thread;     // false - нет потока

    public int result = 0;

    public int sendSMS(Manager canv_) {

        canv = canv_;
        if (subThread == null) {

//            result = canv.game_cycle;
            sms_thread = true;

            subThread = new Thread(this);
            subThread.start();

        } else {
            result = 10;
        }
        return result;

    }

    public void setSenderAddress(byte[] sms_prefix) {

    }

    public void run() {

/*
        if (Manager.DEBUG) {
            System.out.println("sendSMS() phone=" + phone + "\tmsg=" + canv.rend.msg);
        }
*/

        try {

            SmsManager m = SmsManager.getDefault();
            m.sendTextMessage(phone.toString(), null, msg.toString(), null, null);

//            if (Manager.DEBUG)
//                System.out.println("SMS sended. " + senderAddress + "|" + canv.rend.msg);

        } catch (IllegalArgumentException e) {
            result = 5;//if the message is incomplete or contains invalid information. This exception is also thrown if the payload of the message exceeds the maximum length for the given messaging protocol.
            sms_thread = false;
            return;
        } catch (NullPointerException e) {
            result = 7;//if the parameter is null
            sms_thread = false;
            return;
        } catch (SecurityException e) {
            result = 8;//if the application does not have permission to send the message
            sms_thread = false;
            return;
        }

        sms_thread = false;

    }

}
