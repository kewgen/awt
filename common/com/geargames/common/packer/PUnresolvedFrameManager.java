package com.geargames.common.packer;

/**
 * User: mikhail v. kutuzov
 */
public abstract class PUnresolvedFrameManager {

    public abstract PFrame getFrame(int frameId);
    public abstract void putFrameImage(byte[] data, int frameId);

}
