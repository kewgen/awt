package com.geargames.forms;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
import com.geargames.Debug;
import com.geargames.PortPlatform;

/**
 * всплывающее сообщения, закрывается примерно через 6 секунд
 */
public class Notification {

    private static Toast toast;
    public static void showMessage(final Context context, final String str) {
        showMessage(context, str, PortPlatform.getW() / 2, PortPlatform.getH() / 2);
    }

    public static void showMessage(final Context context, final String str, final int sx, final int sy) {
        Debug.trace("Notification:" + str);
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                if (toast != null) toast.cancel();
                int duration = Toast.LENGTH_LONG;
                toast = Toast.makeText(context, str, duration);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, sx, sy);
                toast.show();
            }
        });
    }
}
