package bgu.spl.app;

import java.util.logging.*;

/**
 * Created by matan_000 on 24/12/2015.
 */
public class PrettyLogger {
    public static Logger getLogger(String name){
        Logger l = Logger.getLogger(name);
        l.setUseParentHandlers(false);
        Handler conHdlr = new PrettyHandler();
        conHdlr.setFormatter(new Formatter() {
            public String format(LogRecord record) {
                return String.format("%s: %s - %s\n", record.getLevel(),record.getLoggerName(),record.getMessage());
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
