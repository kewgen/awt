package com.geargames.twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.geargames.Debug;
import com.geargames.common.*;
import com.geargames.common.social.Social;
import com.geargames.common.social.SocialNetwork;
import oauth.signpost.OAuth;

import java.lang.String;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 22.10.12
 * Time: 17:07
 */
public class TwitterManager implements SocialNetwork {

    private final Activity activity;
    private SharedPreferences prefs;
    private Handler mTwitterHandler = new Handler();
    private Social socialManager;

    public TwitterManager(Activity activity, String consumerKey, String consumerSecret) {
        this.activity = activity;
        Constants.CONSUMER_KEY = consumerKey;
        Constants.CONSUMER_SECRET = consumerSecret;

        mTwitterHandler = new Handler();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void login(Social socialManager) {
        this.socialManager = socialManager;
        clearCredentials();
        if (TwitterUtils.isAuthenticated(prefs)) {
            Debug.trace("Is logged.");
        } else {
            Intent i = new Intent(activity, PrepareRequestTokenActivity.class);
            i.putExtra("Tweet_msg", getTweetMsg());
            activity.startActivity(i);
        }
    }

    public void logout() {
        clearCredentials();
    }

    public void sendPost(com.geargames.common.String message, Social social) {

        if (TwitterUtils.isAuthenticated(prefs)) {
            sendTweet(message.toString());
        } else {
            Intent i = new Intent(activity, PrepareRequestTokenActivity.class);
            i.putExtra("tweet_msg", message.toString()/*getTweetMsg()*/);
            activity.startActivity(i);
        }

    }

    public void sendPost(com.geargames.common.String message, Image image, boolean fake) {

    }

    public boolean isLogged() {
        try {
            return TwitterUtils.isAuthenticated(prefs);
        } catch (Exception e) {
            Debug.trace(e.toString());
        }
        return false;
    }

    private java.lang.String getTweetMsg() {
        return "Tweeting from Android App at " + new Date().toLocaleString();
    }

    private void sendTweet(final String msg) {
        Thread t = new Thread() {
            public void run() {

                try {
                    TwitterUtils.sendTweet(prefs, msg);
                    mTwitterHandler.post(mUpdateTwitterNotification);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        };
        t.start();
    }

    final Runnable mUpdateTwitterNotification = new Runnable() {
        public void run() {
            Toast.makeText(activity.getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();
        }
    };

    private void clearCredentials() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.remove(OAuth.OAUTH_TOKEN);
        edit.remove(OAuth.OAUTH_TOKEN_SECRET);
        edit.commit();
    }

    @Override
    public com.geargames.common.String getName() {
        return com.geargames.common.String.valueOfC("twitter");
    }

}
