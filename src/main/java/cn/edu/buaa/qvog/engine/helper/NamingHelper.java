package cn.edu.buaa.qvog.engine.helper;

public class NamingHelper {
    private NamingHelper() {}

    public static String toReservedName(String name) {
        return "_@_" + name + "_@_";
    }
}
