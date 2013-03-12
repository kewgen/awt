package com.geargames.forms;

import com.geargames.MIDlet;
import com.geargames.common.String;
import com.geargames.common.env.SystemEnvironment;

import javax.swing.*;

/**
 * Вызов анимированного сообщения
 */
public class Notification {
    public static void showMessage(MIDlet miDlet, String str) {
        showMessage(miDlet, str, 0, 0);
    }
    public static void showMessage(MIDlet miDlet, String str, int sx, int sy) {
        SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("Message: ").concat(str));
        JOptionPane.showMessageDialog(new JFrame("Message"), str);
    }
}
