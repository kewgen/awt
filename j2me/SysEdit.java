package gg;


import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

//Форма ввода
public class SysEdit implements CommandListener {

    final static boolean DEBUG = false;

    private Form form;
    private String form_title;

    private Command ok = new Command("ok", Command.OK, 1);
    private Command exit = new Command("exit", Command.EXIT, 0);
    private TextField field;

    //private Application app;
    private Display display;

    //public SysEdit(Application app, gg.microedition.Display display) {
    //    super();
    //    this.app = app;
    //    this.display = display.getDisplay();//Display.getDisplay(app.manager.midlet);
    //}

    public void show(String title, String str, int max_size, boolean PASSWORD) {
        //Manager.logEx(" show title:" + title + ", str:" + str + ", limit:" + max_size);
        try {
            if (field == null) field = new TextField("", "", max_size, PASSWORD ? TextField.PASSWORD : TextField.ANY);
            field.setLabel("");
            field.setMaxSize(max_size);
            field.setConstraints(PASSWORD ? TextField.PASSWORD : TextField.ANY);
            field.setString(str.length() < max_size ? str : str.substring(0, max_size - 1));

            form_title = title;

        } catch (Exception e) {
            //Manager.logEx(e);
        }
        initForm();
        //Manager.paused(10);
    }

    public void commandAction(Command c, Displayable s) {
        //Manager.logEx(" command:" + c.getLabel() + ", str:" + field.getString());
        if (c == ok) {
            //edit.seditEnd(field.getString());
            //app.eventAdd(Event.EVENT_SYSEDIT_OK, 0, null);
        } else if (c == exit) {
            //app.eventAdd(Event.EVENT_SYSEDIT_CANCEL, 0, null);
        }
        //Manager.paused(10);
    }

    public String getText() {
        return field.getString();
    }

    private void initForm() {
        if (form == null) {
            form = new Form(form_title);
            form.append(field);

            form.addCommand(ok);
            form.addCommand(exit);

            form.setCommandListener(this);
        }
        form.setTitle(form_title);
        display.setCurrent(form);
    }

}