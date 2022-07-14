package cat.hack3.mangrana.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Output {
    public static void log (String msg) {
        System.out.println(msg);
    }

    public static void logDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        log(dateFormat.format(new Date()));
    }
}
