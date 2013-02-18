package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.common.Graphics;
import com.geargames.common.util.NullRegion;

/**
 * User: mikhail v. kutuzov
 * Абстрактный класс игровая панель, предназначен для доставки событий приложения на уровень компонентов и поддерживает
 * "проглатывание" всех событий панели "компонентом-потребителем" + поддерживает отрисовку компонентов на игровом экране.
 */
public abstract class PContentPanel extends PObjectElement {
    private PElement dedicatedConsumer;
    private ArrayList children;
    private ArrayList activeChildren;

    public PContentPanel(PObject prototype) {
        super(prototype);
        children = new ArrayList();
        activeChildren = new ArrayList();
        dedicatedConsumer = null;

        ArrayList indexes = prototype.getIndexes();
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                createSlotElementByIndex(index, prototype);
            } else {
                createDefaultElementByIndex(index);
            }
        }
    }

    /**
     * Создать "слотовый" объект по индексу объекта.
     */
    protected abstract void createSlotElementByIndex(IndexObject index, PObject prototype);

    /**
     * Создать все остальные не "слотовые" объекты по индексу объекта.
     */
    protected void createDefaultElementByIndex(IndexObject index) {
        PPrototypeElement simple = new PPrototypeElement();
        simple.setPrototype(index.getPrototype());
        addPassiveChild(simple, index.getX(), index.getY());
        simple.setRegion(NullRegion.instance);
    }

    /**
     * Вернуть здесь объекты, которые изображаются на панели.
     *
     * @return
     */
    protected ArrayList getChildren() {
        return children;
    }

    /**
     * Вернуть объекты которые могут обробатывать события.
     *
     * @return
     */
    protected ArrayList getActiveChildren() {
        return activeChildren;
    }

    public void draw(Graphics graphics, int x, int y) {
        for (int i = 0; i < getChildren().size(); i++) {
            PElement element = (PElement) getChildren().get(i);
            if (element.isVisible()) {
                element.draw(graphics, x + element.getX(), y + element.getY());
            }
        }
    }

    /**
     * В панель содержимого пришло событие, если оно лежит  в пределах панели, то мы пытаемся разослать его дочерним
     * элементам и обработав ответ, решить: захватил ли какой либо из них контроль над событиями.
     * Данная реализация предполагает, что координаты важны только для тычковых событий.
     *
     * @param code
     * @param param
     * @param x
     * @param y
     * @return
     */
    public boolean event(int code, int param, int x, int y) {
        if (code == Event.EVENT_TICK) {
            int size = getActiveChildren().size();
            for (int i = 0; i < size; i++) {
                PElement child = (PElement) getActiveChildren().get(i);
                child.event(code, param, 0, 0);
            }
        } else {
            if (dedicatedConsumer != null) {
                if (!dedicatedConsumer.event(code, param, x - dedicatedConsumer.getX(), y - dedicatedConsumer.getY())) {
//                    dedicatedConsumer = null;
//                } else {
//                    return true;
                }
                if (code == Event.EVENT_TOUCH_RELEASED) {
                    dedicatedConsumer = null;
                    return false;
                } else
                    return true;
            } else {
                int size = getActiveChildren().size();
                for (int i = size-1; i >= 0; i--) {
                    PElement child = (PElement) getActiveChildren().get(i);
                    if (child.isVisible()) {
                        int clientX = x - child.getX();
                        int clientY = y - child.getY();
                        if (child.getTouchRegion().isWithIn(clientX, clientY)) {
                            child.event(code, param, clientX, clientY);
                            if (code == Event.EVENT_TOUCH_PRESSED) {
                                dedicatedConsumer = child;
                                return true;
                            } else
                                return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Добавляем сдесь элементы на панель, в конкретную точку, выраженную смещениями элемента от левого верхнего угла
     * панели (x:y)
     *
     * @param element
     * @param x
     * @param y
     * @param active  true если мы кладём элемент который будет потреблять события
     */
    public void addChild(PElement element, int x, int y, boolean active) {
        element.setX(x);
        element.setY(y);
        children.add(element);
        if (active) {
            activeChildren.add(element);
        }
    }

    public void addActiveChild(PElement element, int x, int y) {
        element.setX(x);
        element.setY(y);
        children.add(element);
        activeChildren.add(element);
    }

    public void addActiveChild(PElement element, Index index) {
        addActiveChild(element, index.getX(), index.getY());
    }

    public void addActiveChild(PElement element) {
        children.add(element);
        activeChildren.add(element);
    }

    public void addPassiveChild(PElement element, int x, int y) {
        element.setX(x);
        element.setY(y);
        children.add(element);
    }

    public void addPassiveChild(PElement element, Index index) {
        addPassiveChild(element, index.getX(), index.getY());
    }

    public void addPassiveChild(PElement element) {
        children.add(element);
    }

    /**
     * Вернуть дочерний элемент который зарегистрировался как единственный потребитель событий на панели.
     *
     * @return
     */
    public PElement getDedicatedConsumer() {
        return dedicatedConsumer;
    }
}
