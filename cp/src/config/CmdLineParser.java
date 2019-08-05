package config;

/*
<CmdLine> ::= <StartParametersWithArguments>.
<StartParametersWithArguments> ::= <ConfigDir>
-config-dir <ConfigDirPath> | -config-name <ConfigNamePath> |
    -saves-dir <SavesDirPath>.
*/

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
            }
            else
                System.out.println("Command " + n + " not found, see help for commands list");
        } else
            parseStartCommand();
    }

    private void printIncorrectArgumentError(String argument, String usage) {
        if (argument.length() > 0 && argument.charAt(0) == '-')
            System.out.println("key " + argument + " not found, see help start for details");
        else
            System.out.println("unexpected argument " + argument + ", " + usage);
    }

    private void parseHelp() {
        String n = next();
        if (n == null) {
            System.out.println("usage: [command] [keys]\ncommand list: start (default), help\n" +
                    "to get more detailed information, see help <command>");
            return;
        }

        String c = next();
        if (c != null && (n.equals("start") || n.equals("help"))) {
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


}
