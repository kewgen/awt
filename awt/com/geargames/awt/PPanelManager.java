package com.geargames.awt;

import com.geargames.Debug;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Date: 12.02.13
 * Time: 17:10
 * Базовый класс для действий с игровыми окнами.
 */
public abstract class PPanelManager {
    private int eventX;
    private int eventY;

    private ArrayList drawableElements;
    private ArrayList callableElements;

    private ArrayList preDeafElements;
    private ArrayList preHideElements;
    private ArrayList previousModals;

    private DrawablePElement modal;

    private boolean hideAll;

    protected PPanelManager() {
        drawableElements = new ArrayList();
        callableElements = new ArrayList();
        preDeafElements = new ArrayList();
        preHideElements = new ArrayList();
        previousModals = new ArrayList();
        modal = null;
        hideAll = false;
    }

    /**
     * Вызывать в цикле доставки событий.
     *
     * @param code
     * @param param
     * @param x
     * @param y
     */
    public void event(int code, int param, int x, int y) {
        eventX = x;
        eventY = y;
        if (modal == null) {
            if (!preDeafElements.isEmpty()) {
                callableElements.removeAll(preDeafElements);
                preDeafElements.clear();
            }
            int size = callableElements.size();
            for (int i = 0; i < size; i++) {
                ((Drawable) callableElements.get(i)).event(code, param, x, y);
            }
        } else {
            if (code == Event.EVENT_TICK) {
                if (Drawable.DEBUG) {
                    Debug.trace("TICK-TACK");
                }
            }
            modal.event(code, param, x, y);
        }
    }

    /**
     * Вернуть экранную координату X.
     * @return
     */
    public int getEventX() {
        return eventX;
    }

    /**
     * Вернуть экранную координату Y.
     * @return
     */
    public int getEventY() {
        return eventY;
    }

    /**
     * Вызывать в цикле рисования.
     *
     * @param graphics
     */
    public void draw(Graphics graphics) {
        if (!preHideElements.isEmpty()) {
            drawableElements.removeAll(preHideElements);
            preHideElements.clear();
        }
        if (hideAll) {
            drawableElements.clear();
            callableElements.clear();
            preHideElements.clear();
            preDeafElements.clear();
            previousModals.clear();
            hideAll = false;
        }
        int size = drawableElements.size();
        for (int i = 0; i < size; i++) {
            ((Drawable) drawableElements.get(i)).draw(graphics);
        }
    }

    /**
     * Рисовать панель на графическом контексте.
     *
     * @param element
     */
    public void show(DrawablePPanel element) {
        drawableElements.add(element);
        callableElements.add(element);
        element.init();
        element.onShow();
    }

    /**
     * Скрыть панель на графическом контексте.
     *
     * @param element
     */
    public void hide(DrawablePPanel element) {
        preHideElements.add(element);
        preDeafElements.add(element);
        element.onHide();
    }

    public void hideAll() {
        hideAll = true;
    }

    /**
     * Показать в модальном режиме.
     *
     * @param element
     */
    public void showModal(DrawablePElement element) {
        if (modal != null) {
            previousModals.add(modal);
        }
        modal = element;
        element.init();
        drawableElements.add(element);
    }

    /**
     * Скрыть модальный элемент. Перейти в обычный режим работы.
     */
    public void hideModal() {
        preHideElements.add(modal);
        if (previousModals.size() > 0) {
            modal = (DrawablePPanel) previousModals.remove(previousModals.size() - 1);
        } else {
            modal = null;
        }
    }

    /**
     * Работаем в модальном режиме?
     *
     * @return
     */
    public boolean isModal() {
        return modal != null;
    }

    public void onScreenResize() {
        int size = drawableElements.size();
        for (int i = 0; i < size; i++) {
            ((DrawablePPanel) drawableElements.get(i)).init();
        }
    }


}
