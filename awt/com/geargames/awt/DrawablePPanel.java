package com.geargames.awt;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 25.12.12
 */
public abstract class DrawablePPanel extends DrawablePElement {

    //todo: Константы должны быть проекто-зависимыми, т.е. нужно определить класс DrawablePPanelLayers в своем проекте и перечислить в нем необходимые константы
    public static final byte BOTTOM_LAYER = -100;
    public static final byte MIDDLE_LAYER = 0;
    public static final byte TOP_LAYER = 100;

    /**
     * Обработчик события появления панельки верхнего уровня.
     */
    public abstract void onShow();

    /**
     * Обработчик события сокрытия панельки верхнего уровня.
     */
    public abstract void onHide();

    /**
     * Получить номер слоя, в котором должна располагаться панелька.
     *
     * @return - одно из значений BOTTOM_LAYER, MIDDLE_LAYER или TOP_LAYER
     */
    public abstract byte getLayer();

}
