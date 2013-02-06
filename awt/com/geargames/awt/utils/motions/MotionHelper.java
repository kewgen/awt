package com.geargames.awt.utils.motions;

import com.geargames.awt.HorizontalScrollView;

/**
 * User: mkutuzov
 * Date: 18.03.12
 */
public class MotionHelper {
    public static int INDEX_OUT_OF_RANGE = -1;

    public static int getCentralPositionFromHorizontalScroll(HorizontalScrollView scrollView) {
        int itemSize = scrollView.getItemSize();
        CenteredElasticInertMotionListener motionListener = (CenteredElasticInertMotionListener) scrollView.getMotionListener();
        if (motionListener == null) return 0;
        int n = (-motionListener.getPosition() + motionListener.getCenter() + scrollView.getItemSize() / 2) / itemSize;
        if (n < 0 || n >= scrollView.getItemsAmount()) {
            return INDEX_OUT_OF_RANGE;
        } else {
            return n;
        }
    }
}
