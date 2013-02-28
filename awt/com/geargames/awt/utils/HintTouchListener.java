package com.geargames.awt.utils;

import com.geargames.awt.Drawable;
import com.geargames.awt.TextHint;
import com.geargames.common.Event;

/**
 * Users: mikhail.kutuzov, abarakov
 * Date: 23.11.11
 */
public class HintTouchListener extends TouchListener {

    public HintTouchListener() {
    }

    public void onEvent(Drawable source, int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED /*&& ! ((TextHint)source).getScrollableArea().getTouchRegion().isWithIn(x, y)*/) {
            TextHint textHint = (TextHint) source;
            textHint.hide();
        }
    }

}