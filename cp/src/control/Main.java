package control;

import config.CmdLineParser;
import config.Config;
import menu.MainMenu;
import menu.Menu;
import save.ProgramPath;

public class Main {
    public static void main(String[] args) throws ControlException {
        if (CmdLineParser.parse(args))
            return;

        try {
            Config.defaultNull();
            ProgramPath.init();

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