package com.geargames.platform.forms;

import com.geargames.common.logging.Debug;
import com.geargames.platform.MIDlet;

import javax.swing.*;

/**
 * Вызов анимированного сообщения
 */
public class Notification {

    public static void showMessage(MIDlet miDlet, String str) {
        showMessage(miDlet, str, 0, 0);
    }
    public static void showMessage(MIDlet miDlet, String str, int sx, int sy) {
        Debug.debug("Message: "+str);
        JOptionPane.showMessageDialog(new JFrame("Message"), str);
    }

}
