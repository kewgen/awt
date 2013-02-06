package com.geargames.forms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import com.geargames.Debug;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 16.10.12
 * Time: 11:32
 * системное окно с одной или двумя кнопками
 */
public abstract class SystemDialog extends com.geargames.common.SystemDialog {

    public SystemDialog(final Object midlet, final int id) {
        super(midlet);
        //запускаем в графическом потоке ПОСЛЕ того как вернули SystemDialog
//        ((Activity) midlet).runOnUiThread(new Runnable() {
//            public void run() {
//                Debug.trace(" SystemDialog.showDialog.id:" + id);
//                ((Activity) midlet).showDialog(id);
//            }
//        });
    }

    public Dialog onCreateDialog(final int did) {
        Debug.trace(" SystemDialog.onCreateDialog.did:" + did);
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) midlet);
        switch (type) {
            case ONE_BUTTON:
                builder.setMessage(text.toString())
                        .setCancelable(false)
                        .setPositiveButton(textPositiveButton.toString(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                actionPositiveButton(dialog, did);
                            }
                        })
                ;
                return builder.create();
            case TWO_BUTTONS:
                builder.setMessage(text.toString())
                        .setCancelable(false)
                        .setPositiveButton(textPositiveButton.toString(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                actionPositiveButton(dialog, did);
                            }
                        })
                        .setNegativeButton(textNegativeButton.toString(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                actionNegativeButton(dialog, did);
                            }
                        });
                return builder.create();
        }
        return null;
    }

    @Override
    public void close(Object dialog) {
        ((DialogInterface) dialog).cancel();
    }

}
