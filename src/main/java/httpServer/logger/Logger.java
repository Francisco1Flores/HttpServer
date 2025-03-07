package httpServer.logger;

import java.util.Arrays;

public class Logger {
    public static void info(Object... message) {
        System.out.print("[INFO]:");
        Arrays.stream(message).forEach(m -> System.out.print(" " + m));
        System.out.println();
    }

    public static void error(Object... message) {
        System.out.print("[ERROR]:");
        Arrays.stream(message).forEach(m -> System.out.print(" " + m));
        System.out.println();
    }

    public static void warn(Object... message) {
        System.out.print("[WARN]:");
        Arrays.stream(message).forEach(m -> System.out.print(" " + m));
        System.out.println();
    }


    public static void server(Object... message) {
        System.out.print("[SERVER]:");
        Arrays.stream(message).forEach(m -> System.out.print(" " + m));
        System.out.println();
    }
}
