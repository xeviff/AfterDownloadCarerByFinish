package cat.hack3.mangrana.utils;

public class EasyLogger extends Output {

    private final String identifier;

    public EasyLogger() {
        identifier = Thread.currentThread().getStackTrace()[2].getClass().getSimpleName();
    }
    public EasyLogger(String identifier) {
        this.identifier = identifier;
    }

    public EasyLogger(Class clazz) {
        identifier = clazz.getSimpleName();
    }

    public void nLog(String msg, Object... params){
        log("{0}: {1}", identifier, msg(msg, params));
    }

    public void nLogD(String msg, Object... params){
        nLog("{0}: {1} - "+getCurrentTime(), identifier, msg(msg, params));
    }

    public void nHLog(String msg, Object... params){
        log("{0}: SHOULD NOT HAPPEN! {1}", identifier, msg(msg, params));
    }

}
