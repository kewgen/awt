package com.geargames.media;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.VideoView;
import com.geargames.Debug;
import com.geargames.PortPlatform;
import com.geargames.PortVersion;


public class VideoPlayer implements MediaPlayer.OnSeekCompleteListener {

    private String path;
    private Activity activity;
    private VideoView videoView;
    private int surfaceID;

    public VideoPlayer(String path, Activity activity, int surfaceID) {
        this.path = path;
        this.activity = activity;
        this.surfaceID = surfaceID;
    }

    public void play() {
        try {
            videoView = (VideoView) activity.findViewById(surfaceID);
            videoView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    onClose();
                    return false;
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    onClose();
                }
            });
            //MediaController mediaController = new MediaController(this);
            //mediaController.setAnchorView(videoView);
            //mediaController.setMediaPlayer(videoView);
            //videoView.setMediaController(mediaController);
            //videoView.setVideoPath("/sdcard/media/video/1.avi"); android.resource://opengl.tutorial/2130968576
            //String path = "android.resource://" + getPackageName() + "/" + R.raw.video;
            //path = "android.resource://opengl.tutorial/raw/video";
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();
        } catch (Exception e) {
            Debug.logEx(e);
        }
    }

    public void stop() {
        try {
            if (videoView != null) {
                videoView.stopPlayback();
                if (Build.VERSION.SDK_INT >= PortPlatform.FROYO)//Build.VERSION_CODES.FROYO)
                    PortVersion.videoViewResume(videoView);
                videoView = null;
            }
        } catch (Exception e) {//java.lang.IllegalArgumentException: Receiver not registered: android.widget.VideoView$VideoPlaybackBroadcastReceiver
            e.printStackTrace();
        }
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Debug.trace("VideoPlayer.onBufferingUpdate percent:" + percent);

    }

    public void onClose() {
        //Debug.log("VideoPlayer.onCompletion called");
        stop();
        if (mOnStopPlayerListener != null) mOnStopPlayerListener.onStopPlayer();
        mOnStopPlayerListener = null;//блокируем возможность повторного вызова
        //OnStopPlayerListener.onStopPlayer();
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Debug.trace("VideoPlayer.onVideoSizeChanged called");
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Debug.trace("VideoPlayer.onPrepared called");
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Debug.trace("VideoPlayer.surfaceChanged called");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Debug.trace("VideoPlayer.surfaceDestroyed called");
    }


    public void surfaceCreated(SurfaceHolder holder) {
        Debug.trace("VideoPlayer.surfaceCreated called");
        //playVideo();
    }

    protected void onPause() {
        Debug.trace("VideoPlayer.onPause");
        releaseMediaPlayer();
    }

    protected void onDestroy() {
        Debug.trace("VideoPlayer.onDestroy");
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {

    }

    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Debug.trace("VideoPlayer.onSeekComplete");
    }


    public interface OnStopPlayerListener  {
        abstract void onStopPlayer();
    }

    public void setOnStopPlayerListener(OnStopPlayerListener listener)
    {
        mOnStopPlayerListener = listener;
    }

    private OnStopPlayerListener mOnStopPlayerListener;

}
