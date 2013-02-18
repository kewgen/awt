package com.geargames.common;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 01.06.12
 * Time: 13:07
 */
public abstract class Event {


    public final static int EVENT_NULL = 0;
    public static final byte EVENT_TICK = 1;

    public final static int EVENT_KEY_PRESSED = 100;
    public final static int EVENT_KEY_RELEASED = 101;
    public final static int EVENT_KEY_REPEATED = 102;
    public final static int EVENT_KEY = 103;
    public final static int EVENT_TOUCH_PRESSED = 105;
    public final static int EVENT_TOUCH_RELEASED = 106;
    public final static int EVENT_TOUCH_MOVED = 107;
    public final static int EVENT_TOUCH_DOUBLE_CLICK = 108;
    @Deprecated // Исправлена опечатка
    public final static int EVENT_TOUCH_DOUBBLE_CLICK = EVENT_TOUCH_DOUBLE_CLICK;
    public final static int EVENT_TOUCH_LONG_CLICK = 109;
    public final static int EVENT_KEY_UP = 110;
    public final static int EVENT_KEY_DOWN = 111;

    public final static int EVENT_SYNTHETIC_CLICK = 112;

    public final static int EVENT_TIMER_END = 120;
    public final static int EVENT_SYSEDIT_OK = 121;             //вышли из системного эдита
    public final static int EVENT_SYSEDIT_CANCEL = 122;

    public Event(int uid_, int param_, Object data_, int x_, int y_) {
        this.uid = uid_;
        this.param = param_;
        this.data = data_;
        point_x = x_;
        point_y = y_;
        /*ObjC uncomment*///return self;
    }

    public int getUid() {
        return uid;
    }

    public int getParam() {
        return param;
    }

    public Object getData() {
        return data;
    }

    public int getX() {
        return point_x;
    }

    public int getY() {
        return point_y;
    }

    private int uid;                    // ID сообщения
    private int param;                  // параметр сообщения
    private int point_x;
    private int point_y;
    private Object data;                // данные сообщения


}
