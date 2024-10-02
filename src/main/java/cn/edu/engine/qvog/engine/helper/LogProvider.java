package cn.edu.engine.qvog.engine.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogProvider {
    public static final Logger DEFAULT = Logger.getAnonymousLogger();

    static {
        DEFAULT.setLevel(Level.INFO);
    }

    public static void setLevel(Level level) {
        DEFAULT.setLevel(level);
    }


    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }
}
