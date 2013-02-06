package com.geargames.facebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.facebook.android.*;
import com.geargames.common.social.Social;
import com.geargames.common.social.SocialNetwork;
import com.geargames.forms.Notification;
import com.geargames.packer.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 05.05.12
 * Time: 16:25
 */
public class FacebookManager implements SocialNetwork {

    private static boolean DEBUG = false;
    private Social socialManager;

    public FacebookManager(Activity activity, String APP_ID) {
        this.activity = activity;
        this.APP_ID = APP_ID;

        mFacebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);

        SessionStore.restore(mFacebook, activity);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());

        mHandler = new Handler();

//        SessionEvents.addAuthListener(mSessionListener);
//        SessionEvents.addLogoutListener(mSessionListener);

        //handlePendingAction();
    }

    public void login(Social socialManager) {
        this.socialManager = socialManager;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (mFacebook.isSessionValid()) {
//                        SessionEvents.onLogoutBegin();
//                        AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFacebook);
//                        asyncRunner.logout(activity, new LogoutRequestListener());
                        FacebookManager.log("Facebook.isSessionValid");
                    } else {
                        mFacebook.authorize(activity, new String[]{}, new LoginDialogListener());
                    }
                } catch (Exception e) {
                    FacebookManager.logEx(e);
                }
            }
        });
    }

    public void logout() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (mFacebook.isSessionValid()) {
                        SessionEvents.onLogoutBegin();
                        AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFacebook);
                        asyncRunner.logout(activity, new LogoutRequestListener());
                    }
                } catch (Exception e) {
                    FacebookManager.logEx(e);
                }
            }
        });
    }

    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }


    private final class LoginDialogListener implements Facebook.DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
            FacebookManager.log("Facebook.SessionEvents.onComplete");
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
            FacebookManager.log("Facebook.SessionEvents.onFacebookError");
        }

        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
            FacebookManager.log("Facebook.SessionEvents.onError");
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
            FacebookManager.log("Facebook.SessionEvents.onCancel");
        }
    }

    private class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response, final Object state) {
            // callback should be run in the original thread,
            // not the background thread
            mHandler.post(new Runnable() {
                public void run() {
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }

    private class SessionListener implements SessionEvents.AuthListener, SessionEvents.LogoutListener {

        public void onAuthSucceed() {
            FacebookManager.log("onAuthSucceed");
            //SessionStore.save(mFacebook, activity);
        }

        public void onAuthFail(String error) {
            FacebookManager.log("onAuthFail");
        }

        public void onLogoutBegin() {
            FacebookManager.log("onLogoutBegin");
        }

        public void onLogoutFinish() {
            FacebookManager.log("onLogoutFinish");
            //SessionStore.clear(activity);
        }
    }


    public class SampleAuthListener implements SessionEvents.AuthListener {

        public void onAuthSucceed() {
            //Notification.showMessage(activity, "You have logged in!");
            mAsyncRunner.request("me", new SampleRequestListener());
            SessionStore.save(mFacebook, activity);
            //FacebookManager.log("AuthListener.onAuthSucceed");
            if (socialManager != null) socialManager.onShared(FacebookManager.this);
        }

        public void onAuthFail(String error) {
            FacebookManager.log("Login Failed: " + error);
        }
    }

    public class SampleLogoutListener implements SessionEvents.LogoutListener {
        public void onLogoutBegin() {
            Notification.showMessage(activity, "Logging out...");
            FacebookManager.log("LogoutListener.onLogoutBegin");
        }

        public void onLogoutFinish() {
            Notification.showMessage(activity, "You have logged out!");
            SessionStore.clear(activity);
            FacebookManager.log("LogoutListener.onLogoutFinish");
        }
    }

    public class SampleRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                // process the response here: executed in background thread
                FacebookManager.log("Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String name = json.getString("name");

                Notification.showMessage(activity, "Hello there, " + name + "!");
            } catch (JSONException e) {
                FacebookManager.log("JSON Error in response");
            } catch (FacebookError e) {
                FacebookManager.log("Facebook Error: " + e.getMessage());
            }
        }
    }

    public class SampleDialogListener extends BaseDialogListener {

        private Social social;

        public SampleDialogListener(Social social) {
            this.social = social;
        }

        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                FacebookManager.log("Dialog Success! post_id=" + postId);
                mAsyncRunner.request(postId, new WallPostRequestListener());
                social.onPostSended(true, FacebookManager.this);
            } else {
                FacebookManager.log("No wall post made");
                logout();
            }
        }

        public void onFacebookError(FacebookError e) {
            FacebookManager.log("DialogListener.onFacebookError");
        }

        public void onError(DialogError e) {
            FacebookManager.log("DialogListener.onError");
        }

        public void onCancel() {//здесь можно сбросить токен
            FacebookManager.log("DialogListener.onCancel");
//            SessionStore.clear(activity);
//            mFacebook.setAccessToken(null);
//            mFacebook.setAccessExpires(0);
        }

    }

    public class WallPostRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            FacebookManager.log("Got response: " + response);
            String message = "<empty>";
            try {
                JSONObject json = Util.parseJson(response);
                message = json.getString("message");
            } catch (JSONException e) {
                FacebookManager.log("JSON Error in response");
            } catch (FacebookError e) {
                FacebookManager.log("Facebook Error: " + e.getMessage());
            }
            final String text = "Your Wall Post: " + message;
            //Notification.showMessage(activity, text);
        }
    }

    public boolean isLogged() {
        return mFacebook.isSessionValid();
    }

    private int MESSAGE_MAX_LEN = 140;//максим длина сообщения на отправку друзьям

    public void sendPost(final com.geargames.common.String string, final Social social) {
        final String msg = string.length() > MESSAGE_MAX_LEN ? string.substring(0, MESSAGE_MAX_LEN).toString() : string.toString();

        activity.runOnUiThread(new Runnable() {
            public void run() {

                //post on user's wall.
                Bundle params = new Bundle();
                params.putString("caption", "My Prehistoric Park");
                //params.putString("description", "http://facebook.com/PrehistoricPark");
                params.putString("link", "http://facebook.com/PrehistoricPark");
                params.putString("picture", "http://pfp2.ru/img/icon_160x160_frame.png ");
                params.putString("name", msg);
                mFacebook.dialog(activity, "feed", params, new SampleDialogListener(social));

                //post on friend's wall.
//                Bundle params = new Bundle();
//                params.putString("to", "");
//                mFacebook.dialog(activity, "feed", params, new SampleDialogListener());

                //Send requests with no friend pre-selected and user
                //selects friends on the dialog screen.
//                Bundle params = new Bundle();
//                params.putString("message", msg);
//                mFacebook.dialog(activity, "apprequests", params, new SampleDialogListener(social));

                //send request to a particular friend.
//                Bundle params = new Bundle();
//                params.putString("message", "Hey there!");
//                params.putString("to", "");
//                mFacebook.dialog(activity, "apprequests", params, new SampleDialogListener());

            }
        });
    }

    public void sendPost(com.geargames.common.String message, com.geargames.common.Image image, boolean fake) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendImage(final com.geargames.common.String string, final Image image) {
        final String msg = string.length() > MESSAGE_MAX_LEN ? string.substring(0, MESSAGE_MAX_LEN).toString() : string.toString();

        activity.runOnUiThread(new Runnable() {
            public void run() {

                Bitmap bi = image.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bi.compress(Bitmap.CompressFormat.PNG, 100, stream);  // where bm is bitmap from Sdcard
                byte[] imageBytes = stream.toByteArray();

                // byte[] imageBytes = image.getBitmap().getNinePatchChunk();
                Bundle params = new Bundle();
                params.putByteArray("source", imageBytes);
                params.putString("message", "A wall picture");
                try {
                    mFacebook.request("me/feed", params, "POST");
                } catch (MalformedURLException e) {
                    FacebookManager.logEx(e);
                } catch (FileNotFoundException e) {
                    FacebookManager.logEx(e);
                } catch (IOException e) {
                    FacebookManager.logEx(e);
                }
            }
        });
    }

    public void publishPhoto(String imageURL) {
        FacebookManager.log("Post to Facebook!");

        try {

            JSONObject attachment = new JSONObject();
            attachment.put("message", "Type your message to share");
            attachment.put("name", "Your Application Name");
            attachment.put("href", "Any hyperLink");
            attachment.put("description", "Description about Application");

            JSONObject media = new JSONObject();
            media.put("type", "image");
            media.put("src", "URL path of posting image");
            media.put("href", "Any hyperLink");

            attachment.put("media", new JSONArray().put(media));

            JSONObject properties = new JSONObject();

            JSONObject prop1 = new JSONObject();
            prop1.put("text", "Text or captionText to Post");
            prop1.put("href", "Any hyperLink");
            properties.put("Get the App for free(or any custom message))", prop1);

            // u can make any number of prop object and put on "properties" for    ex:    //prop2,prop3

            attachment.put("properties", properties);

            FacebookManager.log(attachment.toString());

            final Bundle params = new Bundle();
            params.putString("attachment", attachment.toString());

            (activity).runOnUiThread(new Runnable() {
                public void run() {
                    mFacebook.dialog(activity, "stream.publish", params, new PostPhotoDialogListener());
                }
            });

        } catch (JSONException e) {
            FacebookManager.logEx(e);
        }
    }

    public class PostPhotoDialogListener extends BaseDialogListener {

        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                FacebookManager.log("Dialog Success! post_id=" + postId);
                Toast.makeText(activity, "Successfully shared on Facebook!", Toast.LENGTH_LONG).show();

            } else {
                FacebookManager.log("No wall photo made");
            }
        }
    }

    private Activity activity;
    private String APP_ID;
    private Handler mHandler;


    //private SessionListener mSessionListener = new SessionListener();
    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;

    private boolean isLogged;


    /*
        @SuppressWarnings("incomplete-switch")
        private void handlePendingAction() {
            switch (pendingAction) {
                case POST_PHOTO:
                    postPhoto();
                    break;
                case POST_STATUS_UPDATE:
                    postStatusUpdate();
                    break;
            }
            pendingAction = PendingAction.NONE;
        }


        private void postStatusUpdate() {
            if (user != null) {
                final String message = String
                        .format("Updating status for %s at %s", user.getFirstName(), (new Date().toString()));
                Request request = Request
                        .newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {
                            @Override
                            public void onCompleted(Response response) {
                                showAlert(message, response.getGraphObject(), response.getError());
                            }
                        });
                Request.executeBatchAsync(request);
            } else {
                pendingAction = PendingAction.POST_STATUS_UPDATE;
            }
        }

        private void postPhoto() {
            Bitmap image = BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_icon);
            Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    showAlert("Photo Post", response.getGraphObject(), response.getError());
                }
            });
            Request.executeBatchAsync(request);
        }

        private void showAlert(String message, GraphObject result, Exception exception) {
            String title = null;
            String alertMessage = null;
            if (exception == null) {
                title = "Success";
                String id = "0";
                alertMessage = String.format("Successfully posted '%s'.\nPost ID: %s", message, id);
            } else {
                title = "Error";
                alertMessage = exception.getMessage();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title).setMessage(alertMessage).setPositiveButton("OK", null);
            builder.show();
        }


        private PendingAction pendingAction = PendingAction.NONE;
        private GraphUser user;

        private enum PendingAction {
            NONE,
            POST_PHOTO,
            POST_STATUS_UPDATE
        }
    */
    @Override
    public com.geargames.common.String getName() {
        return com.geargames.common.String.valueOfC("facebook");
    }

    private static void log(String str) {
        if (!FacebookManager.DEBUG) com.geargames.Debug.trace("Facebook." + str);
    }

    private static void logEx(Exception e) {
        com.geargames.Debug.logEx(e);
    }

}
