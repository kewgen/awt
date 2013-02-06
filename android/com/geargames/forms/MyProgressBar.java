package com.geargames.forms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class MyProgressBar extends AsyncTask<String, Integer, Void> {

    ProgressDialog progressDialog;

    private Context context;//todo нельзя хранить контекст

    public MyProgressBar(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        //this.progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    protected void onProgressUpdate__(final Integer... progress) {
        super.onProgressUpdate(progress);
        //this.progressBar.setProgress(progress[0]);
        handler.post(new Runnable() {
            public void run() {
                progressDialog.setProgress(progress[0]);
            }
        });
    }

    @Override
    protected Void doInBackground(String... strings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        //Toast.makeText(KSIVAFTPliteActivity.this, "Finished download", Toast.LENGTH_LONG).show();
    }

    private Handler handler = new Handler();
}