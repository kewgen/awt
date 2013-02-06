package com.geargames.awt.utils;

import com.geargames.Debug;
import com.geargames.awt.ScrollViewItem;

/**
 * User: mikhail.kutuzov
 * Date: 07.11.11
 * Time: 19:47
 */
public class SimpleItemAction extends ItemAction {
    private static SimpleItemAction instance = new SimpleItemAction();

    public void action(ScrollViewItem viewItem) {
        Debug.trace(viewItem.getText().toString());
    }

    public static SimpleItemAction getInstance() {
        return instance;
    }
}
