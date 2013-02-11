package com.geargames.common.packer;

/**
 * User: mikhail v. kutuzov
 */
public abstract class PUnresolvedFrameManger {
    public abstract PFrame getFrame(int frameId);
    public abstract void putFrameImage(byte[] data, int frameId);
}
