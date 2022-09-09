package cat.hack3.mangrana.utils;

public class EasyLogger extends Output {

    private final String identifier;

    public EasyLogger() {
        String packagedClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        identifier = packagedClassName.substring(packagedClassName.lastIndexOf('.')+1);
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
        log(msg("{0}: {1} - {2}", identifier, msg(msg, params), getCurrentTime()));
    }

    public void nHLog(String msg, Object... params){
        log("{0}: SHOULD NOT HAPPEN! {1}", identifier, msg(msg, params));
    }

}
