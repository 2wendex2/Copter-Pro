package ru.wendex.cp.control;

import ru.wendex.cp.config.CmdLineParser;
import ru.wendex.cp.config.Config;
import ru.wendex.cp.menu.MainMenu;
import ru.wendex.cp.menu.Menu;

public class Main {
    public static void main(String[] args) throws ControlException {
        if (CmdLineParser.parse(args))
            return;

        try {
            Config.defaultNull();


            Control.getInstance().init(new MainMenu());
            Control.getInstance().loop();
            Control.getInstance().destroy();
        } catch (ControlException | RuntimeException | Error e) {
            Control.getInstance().destroy();
            ControlException.trainFatalError(e);
            throw e;
        }
    }
}