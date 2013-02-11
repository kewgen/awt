package com.geargames.awt.components;

import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Date: 18.01.13
 * Time: 16:07
 */
public abstract class PValueComponent extends PContentPanel {
    public PValueComponent(PObject prototype) {
        super(prototype);
    }

    public abstract short getValue();
}
