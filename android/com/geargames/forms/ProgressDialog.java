package com.geargames.forms;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.geargames.Debug;

public class ProgressDialog {

    private Dialog dialog;
    private Context context;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void onStart(final String msg) {
        if (context == null) return;
        if (dialog != null) dialog.stop();
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                try {
                    dialog = new Dialog(context);
                    dialog.onPreExecute(msg);
                    dialog.execute(msg);
                } catch (Exception e) {
                    Debug.trace("ProgressDialog error. " + e.toString());
                }
            }
        });
    }

    public void onStop() {
        if (dialog != null) dialog.stop();
        dialog = null;
    }


    class Dialog extends AsyncTask<String, Integer, Void> {

        public Dialog(Context context) {
            this.context = context;
            isStopped = false;
        }

        public void onPreExecute(String msg) {
            message = msg;
            Thread.currentThread().setName(Thread.currentThread().getName() + ".Dialog");
            super.onPreExecute();
            progressDialog = new android.app.ProgressDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            //dialog.setTitle("Title");
            progressDialog.setCancelable(true);//на время теста
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            Debug.trace(" Dialog.doInBackground." + toString());
            while (!isStopped) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            Debug.trace(" Dialog.onPostExecute." + toString());
            super.onPostExecute(result);
            if (progressDialog != null) progressDialog.dismiss();
            progressDialog = null;
        }

        public void stop() {
            isStopped = true;
        }

        @Override
        public String toString() {
            return "Dialog{" +
                    "message=" + message +
                    ", isStopped=" + isStopped +
                    '}';
        }

        private android.app.ProgressDialog progressDialog;
        private Context context;
        private boolean isStopped;
        private String message;
    }
}