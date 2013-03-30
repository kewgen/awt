package com.geargames.common.social;

import com.geargames.common.Image;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 24.08.12
 * Time: 18:58
 * Интерфейс социальной сети
 */
public interface SocialNetwork {

    public void login();

    public void logout();

    public void sendPost(String message, Social social);

    public void sendPost(String message, Image image);

    public boolean isLogged();
}
