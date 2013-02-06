package app;

import gg.microedition.*;
import gg.microedition.MIDlet;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.TextField;

//Форма ввода
public class SysEditFmo implements CommandListener {

    final static boolean DEBUG = false;

    private Form form_edit;
    private Command command_ok, command_back;
    private TextField item_text;

    private Render rend;
    private gg.microedition.Display display;

    private boolean is_editing_chat;

    public SysEditFmo(Render rend, Manager canv, MIDlet midlet) {
        this.rend = rend;
        this.display = Display.getDisplay(midlet);//Display.getDisplay(app.manager.midlet);

        form_edit = new Form("Текст");
        command_ok = new Command("ОК", Command.OK, 1);
        command_back = new Command("Назад", Command.BACK, 0);
        item_text = new TextField("Текст:", "", 120, TextField.ANY);
        form_edit.append(item_text);
        form_edit.addCommand(command_ok);
        form_edit.addCommand(command_back);
        form_edit.setCommandListener(this);
    }

    public void show(int max_size) {
        //Manager.logEx(" show title:" + title + ", str:" + str + ", limit:" + max_size);
        item_text.setMaxSize(max_size);
        display.setCurrent(form_edit);
    }

    public void commandAction(Command c, Displayable s) {

        if (rend.commandAction((c == command_ok ? Render.COMMAND_OK : (c == command_back ? Render.COMMAND_BACK : Render.COMMAND_ANOTHER)), gg.microedition.String.valueOfC(item_text.getString()))) {
            if (!is_editing_chat) {
                item_text.setConstraints(TextField.ANY);
                item_text.setMaxSize(Render.MAX_CHAT_LENGTH);
            } else if (c == command_ok) {
                item_text.setString("");
            }
        }

    }

    public void commandAction(int resultCode, gg.microedition.String str) {}

    public void setText(java.lang.String str) {
        item_text.setString(str);
    }

    public void setConstrPassword() {
        item_text.setConstraints(TextField.PASSWORD);
    }

}