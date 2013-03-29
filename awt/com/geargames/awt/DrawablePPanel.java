package com.geargames.awt;

/**
 * User: mikhail v. kutuzov, abarakov
 * Date: 25.12.12
 * Time: 23:38
 */
public abstract class DrawablePPanel extends DrawablePElement {

    /**
     * Обработчик события появления панельки верхнего уровня.
     */
    public abstract void onShow();
    /**
     * Обработчик события сокрытия панельки верхнего уровня.
     */
    public abstract void onHide();

}
