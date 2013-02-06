package com.geargames.awt.utils;

import com.geargames.awt.Drawable;
import com.geargames.awt.TextHint;

/**
 * User: mikhail.kutuzov
 * Date: 23.11.11
 * Time: 18:42
 */
public class HintClickListener extends ClickListener {
    private static HintClickListener instance = new HintClickListener();

    private HintClickListener() {
    }


    public void onEvent(Drawable source, int x, int y) {
        TextHint textHint = (TextHint) source;
        textHint.hide();
    }

    public static HintClickListener getInstance() {
        return instance;
    }

}
