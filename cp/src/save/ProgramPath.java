package save;

import control.ControlException;

import java.io.File;

//Страшный модуль для рассчёта директории, в которой находится программа
//Замечательный язык java не позволяет это сделать по-нормальному
//Поэтому будем делать через жопу

public class ProgramPath {
    private static String programPath;

    public static void init() throws ControlException{
        try {
            programPath = (new File(ProgramPath.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI())).getParentFile().toString();
        } catch (Exception e) {
            throw new ControlException("Unable to get ProgramPath", e);
        }
    }

    public static String get() {
        return programPath;
    }
}