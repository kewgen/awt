package com.geargames.forms;

import com.geargames.common.env.SystemEnvironment;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 16.10.12
 * Time: 11:32
 * системное окно с одной или двумя кнопками
 */
public abstract class SystemDialog extends com.geargames.common.SystemDialog {

    public SystemDialog(Object midlet, int id) {
        super(midlet);
    }

    protected Object onCreateDialog(int id) {
        SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Dialog.").concat(text));
        int action = -1;
        switch (type) {
            case ONE_BUTTON:
                JOptionPane.showMessageDialog(new JFrame(header.toString()),text);
                actionPositiveButton(null, id);
//                activity.showMessage("Сохранение проекта уже запущено!", false);
                return null;
            case TWO_BUTTONS:
                action = JOptionPane.showConfirmDialog(new JFrame(header.toString()), text);
                if (action == 0) {//ok
                    actionPositiveButton(null, id);
                } else if (action == 1) {//no
                    actionNegativeButton(null, id);
                } else if (action == 2) {//cancel
                    close(null);
                }
                return null;
            default:
                return null;
        }
    }

    @Override
    public void close(Object dialog) {

    }

}
