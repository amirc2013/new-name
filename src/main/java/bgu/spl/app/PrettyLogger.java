package bgu.spl.app;

import sun.rmi.runtime.Log;

import java.util.logging.*;

/**
 * Created by matan_000 on 24/12/2015.
 */
public class PrettyLogger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static Logger getLogger(String name){
        Logger l = Logger.getLogger(name);
        l.setUseParentHandlers(false);
        Handler conHdlr = new PrettyHandler();
        conHdlr.setFormatter(new Formatter() {
            public String format(LogRecord record) {
                String msg = String.format("(%s) %s -> %s \n", record.getLevel(),record.getLoggerName(),record.getMessage());
                if(record.getLevel() == Level.SEVERE)
                    msg = ANSI_RED + msg;
                if(record.getLevel() == Level.WARNING)
                    msg = ANSI_YELLOW + msg;
                return msg + ANSI_RESET;
            }
        });

        l.addHandler(conHdlr);
        return l;
    }

    static class PrettyHandler extends ConsoleHandler{
        public PrettyHandler() {
            super();
            setOutputStream(System.out);
        }
    }
}
