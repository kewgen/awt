//
package gg;

import javax.microedition.io.Connector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import java.io.IOException;
import app.Manager;

public class SMSSend implements Runnable {

    public Manager canv;
    public Thread subThread;

    private MessageConnection smsconn;//SMS message connection for inbound text messages.
    private TextMessage txtmessage;
    gg.microedition.String senderAddress;// Address of the message's sender

    public gg.microedition.String msg;
    public gg.microedition.String phone = gg.microedition.String.valueOfC("");

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

        senderAddress = gg.microedition.String.valueOfC("");
        for (int ss_i = 0; ss_i < 6; ss_i++)
            senderAddress = senderAddress.concatCh((char)(sms_prefix[ss_i]));
        senderAddress = senderAddress.concat(phone);

    }

    public void run() {

        if (Manager.DEBUG) {
            System.out.println("sendSMS() phone=" + phone + "\tmsg=" + msg);
        }

        if (smsconn == null) {

            try {

                smsconn = (MessageConnection) Connector.open(senderAddress.toString());

            } catch (ConnectionNotFoundException e) {
                result = 3;//if the requested connection cannot be made, or the protocol type does not exist
                sms_thread = false;
                return;
            } catch (IllegalArgumentException e) {
                result = 1;//if a parameter is invalid
                sms_thread = false;
                return;
            } catch (IOException e) {
                result = 2;//if some other kind of I/O error occurs
                sms_thread = false;
                return;
            } catch (SecurityException e) {
                result = 4;//if a requested protocol handler is not permitted
                sms_thread = false;
                return;
            }
        }

        try {

            txtmessage = (TextMessage) smsconn.newMessage(MessageConnection.TEXT_MESSAGE);
            txtmessage.setPayloadText(msg.toString());
            smsconn.send(txtmessage);

            if (Manager.DEBUG)
                System.out.println("SMS sended. " + senderAddress + "|" + msg);

        } catch (IllegalArgumentException e) {
            result = 5;//if the message is incomplete or contains invalid information. This exception is also thrown if the payload of the message exceeds the maximum length for the given messaging protocol.
            sms_thread = false;
            return;
        } catch (java.io.InterruptedIOException e) {
            result = 6;//if a timeout occurs while either trying to send the message or if this Connection object is closed during this send operation
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
        } catch (IOException t) {
            result = 9;//if the message could not be sent or because of network failure
            sms_thread = false;
            return;
        }

        try {
            smsconn.close();
        } catch (Exception t) {
            result = 11;
        }

        sms_thread = false;

    }

}
