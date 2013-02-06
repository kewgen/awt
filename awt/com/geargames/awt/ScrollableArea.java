package com.geargames.awt;

import com.geargames.awt.components.PElement;
import com.geargames.awt.utils.MotionListener;
import com.geargames.common.util.Region;
import com.geargames.common.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 04.11.11
 * Time: 20:04
 */
public abstract class ScrollableArea extends PElement {
    private boolean stuck;
    private boolean strictlyClipped;
    private boolean initiated = false;

    /**
     * Return an amount of rectangular items to be shawn.
     *
     * @return
     */
    public abstract int getItemsAmount();

    /**
     * Return a linear size of the item to be shawn.
     *
     * @return
     */
    public abstract int getItemSize();

    /**
     * Вернуть регион прикасновения к которому обрабатываются окном списка.
     *
     * @return
     */
    public abstract Region getDrawRegion();

    /**
     * Включён ли строгий режим отсечения крайних элементов списка?
     * Если включён: крайние элементы исчезают, зайдя за край окошка отображения.
     *
     * @return
     */
    public boolean isStrictlyClipped() {
        return strictlyClipped;
    }

    public void setStrictlyClipped(boolean strictlyClipped) {
        this.strictlyClipped = strictlyClipped;
    }

    /**
     * Разрешена ли прокрутка списка с помощью касаний?
     *
     * @return
     */
    public boolean isStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    /**
     * Вернуть процентную долю списка прокрурученную от начальной позиции до текущей.
     *
     * @return
     */
    public abstract int getScrollPercent();

    /**
     * Вернуть верхнюю(в случае вертикальной прокрутки)|левую(в случае горизонтальной) координату полотна относительно
     * левой верхней кромки области прокрутки .
     *
     * @return
     */
    protected int getPosition() {
        return getMotionListener().getPosition();
    }

    /**
     * Требует настройки перед отрисовкой?
     *
     * @return
     */
    public boolean isInitiated() {
        return initiated;
    }

    /**
     * Установить признак инициализации.
     *
     * @param initiated
     */
    public void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    /**
     * Настроим перед отрисовкой.
     *
     * @param graphics
     */
    public abstract void initiate(Graphics graphics);

    /**
     * Вернём использующуюся реализацию.
     *
     * @return
     */
    public abstract MotionListener getMotionListener();

}
