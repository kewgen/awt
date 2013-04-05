package com.geargames.awt.utils;

import com.geargames.awt.components.HorizontalScrollView;
import com.geargames.awt.components.VerticalScrollView;
import com.geargames.awt.utils.motions.CenteredElasticInertMotionListener;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.awt.utils.motions.StubMotionListener;
import com.geargames.common.Graphics;
import com.geargames.common.util.Region;
import com.geargames.common.util.Math;

/**
 * User: mikhail.kutuzov
 * Date: 10.28.11
 * Time: 10:01 AM
 */
public class ScrollHelper {

    public static int getXTextBegin(int format, Region region, int width) {
        switch (getHorizontalFormat(format)) {
            case Graphics.LEFT:
                return region.getMinX();
            case Graphics.HCENTER:
                return region.getCenterX() - (width >> 1);
            case Graphics.RIGHT:
                return region.getMaxX() - width;
            default:
                return region.getMinX();
        }
    }

    public static int getHorizontalFormat(int format) {
        return 13 & format;
    }

    public static int getVerticalFormat(int format) {
        return 50 & format;
    }

    public static int getXBegin(Region region, int columnCount, int columnWidth, int format) {
        int size = columnCount * columnWidth;
        if (size <= region.getWidth()) {
            switch (getHorizontalFormat(format)) {
                case Graphics.LEFT:
                    return region.getMinX();
                case Graphics.HCENTER:
                    return (region.getMinX() + ((region.getWidth() - size) >>> 1));
                case Graphics.RIGHT:
                    return region.getMaxX() - size;
                default:
                    return region.getMaxX();
            }
        } else {
            return region.getMinX();
        }
    }

    public static int getYBegin(Region region, int rawCount, int rawHeight, int format) {
        int size = rawCount * rawHeight;
        if (size <= region.getHeight()) {
            switch (getVerticalFormat(format)) {
                case Graphics.TOP:
                    return region.getMinY();
                case Graphics.VCENTER:
                    return (region.getMinY() + ((region.getHeight() - size) >>> 1));
                case Graphics.BOTTOM:
                    return region.getMaxY() - size;
                default:
                    return region.getMinY();
            }
        } else {
            return region.getMinY();
        }
    }

    /**
     * Настроить один из MotionListener-ов, переданных в качестве параметров, для работы с вертикальным меню.
     * @param inertMotionListener
     * @param stubMotionListener
     * @param region область отображения меню
     * @param itemsAmount количество элементов меню
     * @param itemSize вертикальный размер элемента
     * @param format форматирование (в случае меню влезающего полностью в область отображения)
     * @return
     */
    public static MotionListener createVerticalMotionListener(InertMotionListener inertMotionListener,
                                                              StubMotionListener stubMotionListener,
                                                              Region region, int itemsAmount, int itemSize, int format) {
        int size = itemsAmount * itemSize;
        if (size <= region.getHeight()) {
            stubMotionListener.create(getYBegin(region, itemsAmount, itemSize, format));
            return stubMotionListener;
        } else {
            return adjustVerticalInertMotionListener(inertMotionListener, region, itemsAmount, itemSize);
        }
    }

    public static MotionListener createCenteredHorizontalMenuMotionListener(CenteredElasticInertMotionListener centeredElasticInertMotionListener,
                                                                            StubMotionListener stubMotionListener,
                                                                            Region region, int itemsAmount, int itemSize, int itemOffset, int format) {

        int size = itemsAmount * itemSize;
        if (size <= region.getWidth()) {
            stubMotionListener.create(getXBegin(region, itemsAmount, itemSize, format));
            return stubMotionListener;
        } else {
            return adjustHorizontalCenteredMenuMotionListener(centeredElasticInertMotionListener, region, itemsAmount, itemSize, itemOffset);
        }
    }

    /**
     * Настраиваем и выдаём один из MotionListener-ов переданных в параметрах функции, в зависимости от размера области
     * отображения горизонтального меню и количества элементов.
     * @param inertMotionListener
     * @param stubMotionListener
     * @param region регион отображения списка
     * @param itemsAmount количество элеменов
     * @param itemSize горизонтальный размер элемента
     * @param itemMargin расстояние между элементами
     * @param itemOffset
     * @param format форматирование содержимого для меню влезающего полностью в область вывода
     * @return
     */
    public static MotionListener createFreeHorizontalMenuMotionListener(InertMotionListener inertMotionListener,
                                                                        StubMotionListener stubMotionListener,
                                                                        Region region, int itemsAmount, int itemSize, int itemMargin, int itemOffset, int format) {
        int size = itemsAmount * itemSize;
        if (size <= region.getWidth()) {
            stubMotionListener.create(getXBegin(region, itemsAmount, itemSize, format) + itemOffset + (itemMargin / 2));
            return stubMotionListener;
        } else {
            return adjustHorizontalInertMotionListener(inertMotionListener, region, itemsAmount, itemSize);
        }
    }

    /**
     * Настраиваем InertMotionListener под область вывода.
     * @param motionListener
     * @param region область вывода списка
     * @param itemsAmount количество элементов списка
     * @param itemSize линейный размер элемента списка
     * @return
     */
    public static InertMotionListener adjustVerticalInertMotionListener(InertMotionListener motionListener, Region region, int itemsAmount, int itemSize) {
        int size = itemsAmount * itemSize;
        motionListener.create(region.getMinY(), region.getMaxY(), size);
        return motionListener;
    }

    public static ElasticInertMotionListener adjustVerticalInertMotionListener(ElasticInertMotionListener motionListener, VerticalScrollView scrollView) {
        int size = scrollView.getItemsAmount() * scrollView.getItemSize();
        motionListener.create(scrollView.getDrawRegion().getMinY(), scrollView.getDrawRegion().getMaxY(), size, scrollView.getItemSize());
        return motionListener;
    }

    public static InertMotionListener adjustHorizontalInertMotionListener(InertMotionListener motionListener, Region region, int itemsAmount, int itemSize) {
        int size = itemsAmount * itemSize;
        motionListener.create(region.getMinX(), region.getMaxX(), size);
        return motionListener;
    }

    /**
     * Настраивает MotionListener горизонтального списка таким образом, чтобы элементы списка располагались по центру
     * компонента, в случае, если все элементы списка видимы, в этом случае список будет не прокручиваемым.
     * @param elasticInertMotionListener
     * @param stubMotionListener
     * @param scrollView
     * @return
     * @author abarakov
     */
    public static MotionListener adjustHorzCenteredListElasticInertMotionListener(
            ElasticInertMotionListener elasticInertMotionListener, StubMotionListener stubMotionListener, HorizontalScrollView scrollView) {
        int size = scrollView.getItemsAmount() * scrollView.getItemSize();
        int left = getXBegin(scrollView.getDrawRegion(), scrollView.getItemsAmount(), scrollView.getItemSize(), Graphics.HCENTER);
        if (size <= scrollView.getDrawRegion().getWidth()) {
            stubMotionListener.create(left);
            return stubMotionListener;
        } else {
            elasticInertMotionListener.create(left, Math.min(left + size, scrollView.getDrawRegion().getMaxX()), size, scrollView.getItemSize());
            return elasticInertMotionListener;
        }
    }


    /**
     * Настраиваем CenteredElasticInertMotionListener под размеры области и точку вывода на экране.
     * @param motionListener
     * @param region внутренняя область списка
     * @param itemsAmount количество элементов списка
     * @param itemSize линейный размер списка
     * @param itemOffset сдвиг элемента списка
     * @return
     */
    public static CenteredElasticInertMotionListener adjustHorizontalCenteredMenuMotionListener(
            CenteredElasticInertMotionListener motionListener, Region region, int itemsAmount, int itemSize, int itemOffset) {
        int size = itemsAmount * itemSize;
        motionListener.create(region.getMinX(), itemOffset, size, itemSize);
        return motionListener;
    }

    /**
     * Создаём MotionListener который не крутится (заглушка).
     * @param region
     * @param itemsAmount
     * @param itemSize
     * @return
     */
    public static MotionListener createStubMotionListener(Region region, int itemsAmount, int itemSize) {
        StubMotionListener motionListener = new StubMotionListener();
        motionListener.create(getYBegin(region, itemsAmount, itemSize, Graphics.VCENTER));
        return motionListener;
    }

    /**
     * Настраиваем StubMotionListener (не крутящийся) на параметры экрана.
     * @param motionListener
     * @param region
     * @param itemsAmount
     * @param itemSize
     * @param format одно из значений Graphics.LEFT и т.п.
     * @return
     */
    public static MotionListener adjustStubMotionListener(StubMotionListener motionListener, Region region, int itemsAmount, int itemSize, int format) {
        motionListener.create(getYBegin(region, itemsAmount, itemSize, format));
        return motionListener;
    }

}
