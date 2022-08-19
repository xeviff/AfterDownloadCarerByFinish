package cat.hack3.mangrana.downloads.workers;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.Output.logDate;

public class RetryEngine<D> {

    private final int minutesToWait;
    private final int childrenMustHave;
    private final Function<D, List<D>> childrenRetriever;

    public RetryEngine(int minutesToWait) {
        this(minutesToWait, 0, null);
    }
    public RetryEngine(int minutesToWait, int childrenMustHave, Function<D, List<D>> childrenRetriever) {
        this.minutesToWait = minutesToWait;
        this.childrenMustHave = childrenMustHave;
        this.childrenRetriever = childrenRetriever;
    }

    public D tryUntilGotDesired(Supplier<D> tryToGet, IntConsumer waitFunction) {
        D desired = null;
        boolean waitForChildren = childrenMustHave > 0;
        while (Objects.isNull(desired)) {
            desired = tryToGet.get();
            if (Objects.isNull(desired)) {
                log("couldn't find it");
                waitFunction.accept(minutesToWait);
            } else if (waitForChildren) {
                while (waitForChildren) {
                    List<D> children = childrenRetriever.apply(desired);
                    if (children.size() < childrenMustHave) {
                        log("there is no enough child elements yet");
                        waitFunction.accept(minutesToWait);
                    } else {
                        int shorterTime = minutesToWait / 3;
                        waitBeforeNextRetry(shorterTime, "waiting a bit more for courtesy: " + shorterTime + "min");
                        waitForChildren = false;
                    }
                }
            }
        }
        log("found desired element and returning it");
        return desired;
    }

    public D tryUntilGotDesired(Supplier<D> tryToGet) {
        IntConsumer defaultWaitFunction = time -> waitBeforeNextRetry(time, null);
        return tryUntilGotDesired(tryToGet, defaultWaitFunction);
    }

    public void waitBeforeNextRetry(int currentMinutesToWait, String forcedMessage) {
        String msg = StringUtils.isNotEmpty(forcedMessage)
                ? forcedMessage
                : "waiting "+currentMinutesToWait+" minutes before the next try";
        log(msg);
        logDate();
        try {
            TimeUnit.MINUTES.sleep(currentMinutesToWait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("failed TimeUnit.MINUTES.sleep");
            e.printStackTrace();
        }
    }

}
