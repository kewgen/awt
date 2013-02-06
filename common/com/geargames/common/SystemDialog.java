package com.geargames.common;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 16.10.12
 * Time: 11:32
 * системное окно с одной или двумя кнопками
 */
public abstract class SystemDialog {

    public SystemDialog(Object midlet) {
        this.midlet = midlet;
    }

    public abstract void actionPositiveButton(Object dialog, int id);

    public abstract void actionNegativeButton(Object dialog, int id);

    public abstract void close(Object dialog);//закрыть диалог

    public void setHeader(String header) {
        this.header = header;
    }

    public void setText(String text) {
        this.text = text;
        typeUpdate();
    }

    public void setTextPositiveButton(String textPositiveButton) {
        this.textPositiveButton = textPositiveButton;
        typeUpdate();
    }

    public void setTextNegativeButton(String textNegativeButton) {
        this.textNegativeButton = textNegativeButton;
        typeUpdate();
    }

    private void typeUpdate() {
        type = textNegativeButton == null ? ONE_BUTTON : TWO_BUTTONS;
    }


    protected Object midlet;
    protected byte type;
    protected String header;
    protected String text;
    protected String textPositiveButton;
    protected String textNegativeButton;

    protected final byte ONE_BUTTON = 0;//одна кнопка
    protected final byte TWO_BUTTONS = 1;

}
