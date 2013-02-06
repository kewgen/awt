package com.geargames.common.social;

import com.geargames.common.String;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 24.08.12
 * Time: 18:04
 */
public interface Social {

    public void onActivityResult(int requestCode, int resultCode, Object data);

    public void share(SocialNetwork socialNetwork);
    public void shareFacebook();
    public void shareVKontakte();
    public void shareTwitter();

    public boolean isFacebookShared();
    public boolean isVKontakteShared();
    public boolean isTwitterShared();

    public void sendPostOnFacebook(String message, Object image);
    public void sendPostOnVkontakte(String message, Object image);
    public void sendPostOnTwitter(String message);

    public void logoutFacebook();
    public void logoutVKontakte();
    public void logoutTwitter();


    public String getFacebookURL();
    public com.geargames.common.String getVkontakteURL();
    public String getTwitterURL();

    //соцсеть зарегистрирована
    public void onShared(SocialNetwork socialNetwork);
    //пост размещен на стену
    public void onPostSended(boolean isSended, SocialNetwork socialNetwork);

    void sendMail(String message_text, String message_subject);
}
