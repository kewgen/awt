package com.geargames.common.resource;

/**
 * User: mkutuzov
 * Date: 12.03.13
 */
public abstract class ResourceManager {
    public abstract Resource loadResource(String name);
    public abstract void unLoadResource(String name);

    public void loadResources(StateDescription stateDescription){

    }
}
