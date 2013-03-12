package com.geargames.common;

/**
 * Users: kewgen, mikhail kutuzov, abarakov
 * Date: 01.06.12
 * Time: 13:07
 */
public class Event {
    public final static int EVENT_NULL = 0;
//    public final static int EVENT_TICK = 1;

    public final static int EVENT_KEY_PRESSED  = 100; //todo: тоже самое, что EVENT_KEY_DOWN?
    public final static int EVENT_KEY_RELEASED = 101; //todo: тоже самое, что EVENT_KEY_UP?
    public final static int EVENT_KEY_REPEATED = 102;
    public final static int EVENT_KEY          = 103;
    public final static int EVENT_KEY_DOWN     = 111;
    public final static int EVENT_KEY_UP       = 110;

    public final static int EVENT_TOUCH_PRESSED      = 105;
    public final static int EVENT_TOUCH_RELEASED     = 106;
    public final static int EVENT_TOUCH_MOVED        = 107;
    public final static int EVENT_TOUCH_DOUBLE_CLICK = 108;
    public final static int EVENT_TOUCH_LONG_CLICK   = 109;
    public final static int EVENT_SYNTHETIC_CLICK    = 112;

//    public final static int EVENT_TIMER = 120; // EVENT_TIMER_END

    public final static int EVENT_SYSEDIT_OK = 121;             // Вышли из системного эдита
    public final static int EVENT_SYSEDIT_CANCEL = 122;

    private int uid;        // ID сообщения
    private int param;      // Параметр сообщения
    private int point_x;
    private int point_y;
    private Object data;    // Данные сообщения

    //todo:  point_x + param -> param0;  point_y -> param1

    public Event(int uid, int param, Object data, int x, int y) {
        this.uid     = uid;
        this.param   = param;
        this.data    = data;
        this.point_x = x;
        this.point_y = y;
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

}
