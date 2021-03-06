package com.geargames.awt.components;

import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Индикатор не получающий события панели.
 */
public class PSimpleIndicator extends PSpriteProgressIndicator {
    public PSimpleIndicator(PObject prototype) {
        super(prototype);
    }

    public boolean event(int code, int param, int x, int y) {
        return false;
    }
}
