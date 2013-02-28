package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.VanishingLabel;
import com.geargames.awt.cinematics.FinishAdviser;

/**
 * user: Mikhail V. Kutuzov
 * date: 10.12.11
 * time: 20:11
 */
public class VanishFinishAdviser extends FinishAdviser {
    private VanishingLabel label;

    public VanishFinishAdviser(VanishingLabel label) {
        this.label = label;
    }

    public void onTick() {
    }

    public boolean isFinished() {
        return label.isFullTransparent();
    }
}
