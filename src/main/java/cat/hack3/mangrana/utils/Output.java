package cat.hack3.mangrana.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Output {
    private Output(){}
    public static void log (String msg) {
        System.out.println(msg);
    }

    public static void logWithDate(String msg) {
        log(msg+" - "+getCurrentTime());
    }

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

}
