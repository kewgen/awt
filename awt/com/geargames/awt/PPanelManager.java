package com.geargames.awt;

import com.geargames.common.Graphics;
import com.geargames.common.util.ArrayList;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 12.02.13, 11.04.13
 * Базовый класс для действий с игровыми окнами.
 */
public abstract class PPanelManager {
    private int eventX;
    private int eventY;

    // drawableElements + callableElements
    private ArrayList panelElements;       // Список видимых простых панелек
    private ArrayList modalElements;       // Список видимых модальных панелек
    //todo: Реализовать списки addedPanelElements и addedModalElements
//    private ArrayList addedPanelElements;  // Список простых панелек, которые должны быть отображены
//    private ArrayList addedModalElements;  // Список модальных панелек, которые должны быть отображены
    private ArrayList hiddenElements;      // Список панелек, которые должны быть скрыты

    protected PPanelManager() {
        panelElements = new ArrayList(16);
        modalElements = new ArrayList(8);
        hiddenElements = new ArrayList(8);
    }

    /**
     * Вызывать в цикле доставки событий.
     *
     * @param code
     * @param param
     * @param x
     * @param y
     */
    public void onEvent(int code, int param, int x, int y) {
        eventX = x;
        eventY = y;
        //todo: TextHint должен получать события, находясь в списке panelElements, а не в индивидуальном порядке
        TextHint hintElement = TextHint.getInstance();
        hintElement.onEvent(code, param, x, y);
        //todo: screen должен получать события последним, он же находится ниже всех
        //todo: проще создать копию списка panelElements, чем использовать список hiddenElements + в будущем списки addedPanelElements и addedModalElements
        if (!hiddenElements.isEmpty()) {
            panelElements.removeAll(hiddenElements);
            modalElements.removeAll(hiddenElements);
            hiddenElements.clear();
        }
        if (modalElements.isEmpty()) {
            for (int i = panelElements.size() - 1; i >= 0; i--) {
                //todo: если элемент панельки невидим или выключен, то событий он не должен получать
                DrawablePPanel panel = (DrawablePPanel) panelElements.get(i);
                if (panel.onEvent(code, param, x, y)) {
                    break;
                }
            }
        } else {
            DrawablePPanel modal = (DrawablePPanel) modalElements.get(modalElements.size() - 1);
            modal.onModalEvent(code, param, x, y); //todo: не нужно отдельного обработчика модальных эвентов
        }
    }

    /**
     * Вернуть экранную координату X в момент предыдущего события.
     *
     * @return
     */
    public int getEventX() {
        return eventX;
    }

    /**
     * Вернуть экранную координату Y в момент предыдущего события.
     *
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
        //todo: проще создать копию списка panelElements, чем использовать список hiddenElements + в будущем списки addedPanelElements и addedModalElements
        if (!hiddenElements.isEmpty()) {
            panelElements.removeAll(hiddenElements);
            modalElements.removeAll(hiddenElements);
            hiddenElements.clear();
        }
        int size = panelElements.size();
        for (int i = 0; i < size; i++) {
            //todo: если элемент панельки невидим, то и рисовать его не следует
            DrawablePPanel panel = (DrawablePPanel) panelElements.get(i);
            panel.draw(graphics);
        }
        //todo: TextHint должен отрисовываться находясь в списке panelElements, а не в индивидуальном порядке
        TextHint hintElement = TextHint.getInstance();
        hintElement.draw(graphics);
    }

    private void addByLayer(ArrayList list, DrawablePPanel element) {
        int insertIndex = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            DrawablePPanel item = (DrawablePPanel) list.get(i);
            if (item.getLayer() <= element.getLayer()) {
                insertIndex = i + 1;
                break;
            }
        }
        list.add(insertIndex, element);
    }

    /**
     * Отобразить панель.
     *
     * @param panel
     */
    public boolean show(DrawablePPanel panel) {
        //todo: Наверно следует возбудить исключение, если панелька уже отображена в модальном режиме
        boolean result = false;
        if (hiddenElements.contains(panel)) {
            hiddenElements.remove(panel);
            result = true;
        }
        if (!panelElements.contains(panel)) {
            addByLayer(panelElements, panel); //todo: нельзя модифицировать список здесь
            result = true;
        }
        if (result) {
            panel.init();
            panel.onShow();
        }
        return result;
    }

    /**
     * Скрыть панель.
     *
     * @param panel
     */
    public boolean hide(DrawablePPanel panel) {
        if (isVisible(panel)) {
            hiddenElements.add(panel);
            panel.onHide();
            return true;
        }
        return false;
    }

    /**
     * Скрыть все окона.
     */
    public void hideAll() {
        hideByLayer(DrawablePPanel.TOP_LAYER);
    }

    /**
     * Скрыть окна ниже верхнего уровня.
     */
    public void hideMiddle(){
        hideByLayer(DrawablePPanel.MIDDLE_LAYER);
    }

    /**
     * Скрыть окна ниже среднего уровня.
     */
    public void hideBottom(){
        hideByLayer(DrawablePPanel.BOTTOM_LAYER);
    }

    private void hideByLayer(byte layer) {
        for (int i = panelElements.size() - 1; i >= 0; i--) {
            DrawablePPanel panel = (DrawablePPanel) panelElements.get(i);
            if (panel.getLayer() <= layer) {
                hide(panel);
            }
        }
    }

    /**
     * Отобразить панель в модальном режиме.
     *
     * @param panel
     */
    public boolean showModal(DrawablePPanel panel) {
        //todo: Наверно следует возбудить исключение, если панелька уже отображена в немодальном режиме
        boolean result = show(panel);
        if (!modalElements.contains(panel)) {
            addByLayer(modalElements, panel);
            result = true;
        }
        return result;
    }

    /**
     * Вернет true, если панелька panel в настоящий момент видима
     *
     * @param panel
     * @return
     */
    public boolean isVisible(DrawablePPanel panel) {
        return panelElements.contains(panel) && !hiddenElements.contains(panel);
    }

    /**
     * Вернет true, если панелька panel в настоящий момент открыта в модальном режиме
     *
     * @param panel
     * @return
     */
    public boolean isModal(DrawablePPanel panel) {
        return modalElements.contains(panel) && !hiddenElements.contains(panel);
    }

    /**
     * Получить ссылку на активную модальную форму.
     *
     * @return
     */
    public DrawablePPanel getModalPanel() {
        for (int i = modalElements.size() - 1; i >= 0; i--) {
            DrawablePPanel panel = (DrawablePPanel) modalElements.get(i);
            if (!hiddenElements.contains(panel)) {
                return panel;
            }
        }
        return null;
    }

    public void onScreenResize() {
        int size = panelElements.size();
        for (int i = 0; i < size; i++) {
            DrawablePPanel panel = (DrawablePPanel) panelElements.get(i);
            panel.init();
        }
    }

}
