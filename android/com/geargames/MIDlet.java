package com.geargames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.geargames.common.String;
import com.geargames.common.social.Social;
import com.geargames.common.util.HashMap;
import com.geargames.packer.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Порт-wrapper для microedition
 */

public abstract class MIDlet extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide app id
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {//вызывается также при смене ориентации, API: желательно все сохранять в onStop()
        super.onDestroy();
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void setDisplay(android.view.View view) {
        Debug.trace("MIDlet.setDisplay.");
        setContentView(view);
    }

    public void notifyDestroyed() {
        finish();
        //android.os.Process.killProcess(android.os.Process.myPid());// Включили взад, т.к. процесс висел и держал ресурсы
    }

    public void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void repaint() {
    }//затычка для консоли

    public boolean platformRequest(String url, boolean inNewView) {
        //market://search?q=search query - открыть маркет
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
        return false;
    }

    public String getAppProperty(String str) {
        return null;//(str.equals("MIDlet-Version") ? "1.0.03" : "");
    }

    public InputStream getResourceAsStream(String path) throws IOException {//из папки Assets
        String name = path.substring(1, path.length());//убираем слеш
        return getResources().getAssets().open(name.toString());
        //return Manager.getInstance().getResources().openRawResource(getResourceByName(path));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//входящие сообщения из других приложений
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onVibration() {
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
    }

    public int getDPI() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    public String saveScreenShot(View view, String saveName) {
        //View view = findViewById(R.id.layoutRoot);
        Bitmap bitmap = view.getDrawingCache();
        int index = 1;
        File file;
        String fileName;
        do {
            fileName = saveName.concatI(index);
            file = new File(fileName.toString());
            index++;
        } while (file.exists());

        try {
            boolean isCreated = file.createNewFile();
            if (!isCreated) return null;
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            Debug.logEx(e);
        }
        return fileName;
    }

    public Image getScreenShot(View view, String saveName) {
        Bitmap bitmap = view.getDrawingCache();
        Image image = new Image(bitmap);
        return image;
    }

    public void marketBind() {
    }

    //передаем удачно проведенный платеж
    public boolean onPurchaseSuccess(java.lang.String signedData, int billing) {
        return false;
    }

    public void updateVersion() {
    }

    public void progressDialogShow(String str) {
    }

    public void progressDialogClose() {
    }

    public void addMetrics(String event, HashMap map, int uid) {
    }

    public void addErrorMetrics(Exception e) {
    }

    public String getSystemLanguage() {
        return null;
    }//системный язык

    public boolean isDebugMode() {
        return false;
    }//false - релизный режим, на этапе теста строго конроллируется сервером

    public abstract String getLanguage();

    public abstract void setLanguage(String lang);

    public abstract boolean isSplashTimeUp();

    public abstract Social getSocial();

    protected Manager manager;

}
