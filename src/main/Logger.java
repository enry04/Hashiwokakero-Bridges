package main;

public class Logger {
    public static void logInfo(String msg) {
        System.out.println(msg);
    }

    public static void logWarning(String msg) {
        System.out.println("\nWARNING:\n" + msg + "\n");
    }

    public static void logError(String msg) {
        System.out.println("\nERROR:\n" + msg + "\n");
    }

}
