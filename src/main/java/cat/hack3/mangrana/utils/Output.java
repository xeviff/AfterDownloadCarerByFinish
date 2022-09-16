package cat.hack3.mangrana.utils;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Output {

    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    protected Output(){}

    private static void log (String msg) {
        System.out.println(msg);
    }
    public static void log (String msg, Object... params) {
        log(msg(msg, params));
    }

    public static void logWithDate(String msg) {
        log(msg+" - "+getCurrentTime());
    }

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return dateFormat.format(new Date());
    }

    public static String msg(String msg, Object... params) {
        return MessageFormat.format(msg, params);
    }

}
