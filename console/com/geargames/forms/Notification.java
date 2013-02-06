package com.geargames.forms;

import com.geargames.Debug;
import com.geargames.MIDlet;

import javax.swing.*;

/**
 * Вызов анимированного сообщения
 */
public class Notification {
    public static void showMessage(MIDlet miDlet, String str) {
        showMessage(miDlet, str, 0, 0);
    }
    public static void showMessage(MIDlet miDlet, String str, int sx, int sy) {
        Debug.trace("Message:" + str);
        JOptionPane.showMessageDialog(new JFrame("Message"), str);
    }
}
