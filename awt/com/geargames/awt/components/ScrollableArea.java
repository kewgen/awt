package com.geargames.awt.components;

import com.geargames.awt.utils.MotionListener;
import com.geargames.common.Graphics;

/**
 * User: mikhail.kutuzov, abarakov
 * Date: 04.11.11
 * Time: 20:04
 */
public abstract class ScrollableArea extends PElement {
    private boolean stuck;
    private boolean strictlyClipped;
    private boolean initiated = false;

    /**
     * Вернуть количество элементов списка.
     *
     * @return
     */
    public abstract int getItemsAmount();

    /**
     * Вернуть линейный размер элемента (ширина - для горизонтального компонента, высота - для вертикального).
     *
     * @return
     */
    public abstract int getItemSize();

    /**
     * Вернуть наибольшее количество целых элементов, которые могут быть одновременно видимы в списке.
     * Элементы, которые видимы частично, не учитываются.
     *
     * @return
     */
    // getVisibleItemAmount
    public int getShownItemsAmount() {
        return getDrawableAreaSize() / getItemSize();
    }

    /**
     * Вернуть размеры скроллируемой области.
     *
     * @return
     */
    public int getScrollableAreaSize() {
        return getItemsAmount() * getItemSize();
    }

    /**
     * Вернуть размеры видимой области (ширина - для горизонтального компонента, высота - для вертикального).
     *
     * @return
     */
    public abstract int getDrawableAreaSize();

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

    public final boolean getStrictlyClipped() {
        return strictlyClipped;
    }

    public void setStrictlyClipped(boolean strictlyClipped) {
        this.strictlyClipped = strictlyClipped;
        setInitiated(false);
    }

    /**
     * Вернет true, если прокрутка списка с помощью касаний запрещена.
     *
     * @return
     */
    public boolean isStuck() {
        return stuck;
    }

    public final boolean getStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
        setInitiated(false);
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
     * Вернет true, если компонент должен быть инициализирован
     *
     * @return
     */
    public boolean isInitiated() {
        return initiated;
    }

    /**
     * Установить признак инициализации.
     * True, если компонент инициализирован и false, если компоненту требуется инициализация.
     *
     * @param initiated
     */
    public void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    /**
     * Метод вызывается каждый раз, когда компонент не инициализирован и его требуется отрисовать.
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
