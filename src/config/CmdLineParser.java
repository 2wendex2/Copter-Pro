package ru.w2tksoft.cp.config;

/*
<CmdLine> ::= <StartParametersWithArguments>.
<StartParametersWithArguments> ::= <ConfigDir>
-config-dir <ConfigDirPath> | -config-name <ConfigNamePath> |
    -saves-dir <SavesDirPath>.
*/

import control.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CmdLineParser {
    private String[] args;
    private int ind;
    private boolean notStart = true;

    public static boolean parse(String[] args) {
        CmdLineParser clp = new CmdLineParser(args);
        clp.parseCmdLine();
        return clp.notStart;
    }

    private CmdLineParser(String[] args) {
        this.args = args;
        this.ind = 0;
    }

    private String next() {
        if (ind < args.length)
            return args[ind++];
        else
            return null;
    }

    private String peek() {
        if (ind < args.length)
            return args[ind];
        else
            return null;
    }

    private void parseCmdLine() {
        String n = peek();
        if (n != null && (n.length() == 0 || n.charAt(0) != '-')) {
            next();
            if (n.equals("help"))
                parseHelp();
            else if (n.equals("start")) {
                parseStartCommand();
            } else if (n.equals("version"))
                parseVersion();
            else if (n.equals("history"))
                parseHistory();
            else
                System.out.println("Command " + n + " not found, see help for commands list");
        } else
            parseStartCommand();
    }

    private void printIncorrectArgumentError(String argument, String usage) {
        if (argument.length() > 0 && argument.charAt(0) == '-')
            System.out.println("key " + argument + " not found, " + usage);
        else
            System.out.println("unexpected argument " + argument + ", " + usage);
    }

    private void parseHelp() {
        String n = next();
        if (n == null) {
            System.out.println("usage: [command] [keys]\ncommand list: start (default), help, " +
                    "version, history\n" +
                    "to get more detailed information, see help <command>");
            return;
        }

        String c = next();
        if (c != null && (n.equals("start") || n.equals("help") || n.equals("version") || n.equals("history"))) {
            printIncorrectArgumentError(n, "usage help [command]");
            return;
        }

        if (n.equals("start")) {
            System.out.println("usage: [start] [keys]\nkeys:\n" +
                    "-config-dir <path> — directory for config file (default: program directory)\n" +
                    "-config-name <name> — name of config file (default: config.dat)\n" +
                    "-saves-dir <path> — directory for saves files (default: <-config-dir>/saves)");
        } else if (n.equals("help")) {
            System.out.println("help — to show all commands\nhelp <command> — to show help about command");
        } else if (n.equals("version")) {
            System.out.println("show game version");
        } else if (n.equals("history")) {
            System.out.println("show game version history");
        } else {
            System.out.println("command " + n + " not found, see help for commands list");
        }
    }

    private void parseStartCommand() {
        for (String n = next(); n != null; n = next()) {
            String p = next();
            if ((p == null || (p.length() > 0 && p.charAt(0) == '-')) &&
                    (n.equals("-config-dir") || n.equals("-config-name") || n.equals("-saves-dir"))) {
                System.out.println("key " + n + " parameter is missing");
                return;
            }

            if (n.equals("-config-dir")) {
                Config.setConfigDir(p);
            } else if (n.equals("-config-name")) {
                Config.setConfigName(p);
            } else if (n.equals("-saves-dir")) {
                Config.setSavesDir(p);
            } else {
                printIncorrectArgumentError(n, "see help start for details");
                return;
            }
        }

        notStart = false;
    }

    private void parseVersion() {
        String n = next();
        if (n != null) {
            printIncorrectArgumentError(n, "usage version");
            return;
        }

        System.out.println(Config.version);
    }

    private void parseHistory() {
        String n = next();
        if (n != null) {
            printIncorrectArgumentError(n, "usage history");
            return;
        }


        InputStream is = Main.class.getResourceAsStream("/history.txt");
        if (is == null) {
            System.out.println("Unable to open resourse history.txt");
            return;
        }

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("UTF-8 encoding not supported");
            return;
        }

        char[] buf = new char[2048];

        try {
            for (int i = isr.read(buf, 0, buf.length); i > 0; i = isr.read(buf, 0, buf.length))
                System.out.print(new String(buf, 0, i));
            isr.close();
            is.close();
        } catch (IOException e) {
            System.out.println("Error reading resourse history.txt");
        }
    }
}
