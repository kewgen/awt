package com.geargames.packer;

import android.view.MotionEvent;
import com.geargames.Debug;
import com.geargames.MIDlet;
import com.geargames.PortPlatform;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 10.10.12
 * Time: 18:39
 * враппер для портирования под старые версии
 */
public class ScaleGestureDetectorMy {

    public ScaleGestureDetectorMy(MIDlet midlet, float scaleMin, float scaleMax) {
        if (PortPlatform.getLevelAPI() >= 8) {
            mScaleDetector = new android.view.ScaleGestureDetector(midlet, new ScaleListener());
            this.scaleMin = scaleMin;
            this.scaleMax = scaleMax;
            setScaleFactor(1.0f);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        if (PortPlatform.getLevelAPI() >= 8) {
            try{
                mScaleDetector.onTouchEvent(event);
            } catch (IllegalArgumentException e) {//http://code.google.com/p/android/issues/detail?id=18990
                Debug.trace(e.toString());
            }
        }
    }

    public boolean isInProgress() {
        if (PortPlatform.getLevelAPI() >= 8)
            return mScaleDetector.isInProgress();
        return false;
    }

    public float getScaleFactor() {
        if (PortPlatform.getLevelAPI() >= 8)
            return mScaleFactor;
        return 1.0f;
    }

    public void setScaleFactor(float mScaleFactor) {
        this.mScaleFactor = mScaleFactor;
    }

    private class ScaleListener extends android.view.ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(android.view.ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
//            mScaleFactor *= detector.getCurrentSpan() / detector.getPreviousSpan();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(scaleMin, Math.min(mScaleFactor, scaleMax));
            //Debug.log(" change scale:" + mScaleFactor);

            return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(android.view.ScaleGestureDetector detector) {
//            Debug.trace("ScaleListener.onScaleBegin");
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(android.view.ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
//            Debug.trace("ScaleListener.onScaleEnd");
        }
    }

    private android.view.ScaleGestureDetector mScaleDetector;

    private float mScaleFactor;
    private float scaleMin;
    private float scaleMax;

}
