package cat.hack3.mangrana.utils;

public class Waiter {

    public static void waitMinutes(int minutes) {
        try {
            Thread.sleep(minutes * 60 * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
