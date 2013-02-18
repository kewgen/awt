package com.geargames.awt.components;

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
     * Return an amount of rectangular items to be shown.
     *
     * @return
     */
    public abstract int getItemsAmount();

    /**
     * Return a linear size of the item to be shown.
     *
     * @return
     */
    public abstract int getItemSize();

    /**
     * Вернуть регион прикосновения, который обрабатываются окном списка.
     *
     * @return
     */
    public abstract Region getDrawRegion();

    /**
     * Вернет true, если включён строгий режим отсечения крайних элементов списка
     * Если true, то крайние элементы (справа/снизу) списка не будут отрисованы, если хотя бы часть элемента списка
     * выходит за пределы компонента-списка
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
     * Вернет true, если прокрутка списка с помощью касаний запрещена
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
     * Вернуть процентную долю списка прокрученную от начальной позиции до текущей.
     *
     * @return
     */
    public abstract int getScrollPercent();

    /**
     * Вернуть верхнюю (в случае вертикальной прокрутки)|левую(в случае горизонтальной) координату полотна относительно
     * левой верхней кромки области прокрутки.
     *
     * @return
     */
    protected int getPosition() {
        return getMotionListener().getPosition();
    }

    /**
     * Вернет true, если компонент должен быть перенастроен перед отрисовкой
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
     * Настроить перед отрисовкой.
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
