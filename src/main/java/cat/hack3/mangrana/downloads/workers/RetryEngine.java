package cat.hack3.mangrana.downloads.workers;

import cat.hack3.mangrana.utils.Output;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

import static cat.hack3.mangrana.utils.Output.logDate;

public class RetryEngine<D> {

    private final int minutesToWait;
    private final ChildrenRequirements<D> childrenRequirements;
    private String downloadId;

    public static class ChildrenRequirements<D> {
        final int children;
        final Function<D, List<D>> retriever;
        final Function<D, Boolean> constraint;

        public ChildrenRequirements(int childrenMustHave, Function<D, List<D>> childrenRetriever, Function<D, Boolean> constraint){
            this.children = childrenMustHave;
            this.retriever = childrenRetriever;
            this.constraint = constraint;
        }
    }

    public RetryEngine(int minutesToWait, String downloadId) {
        this(minutesToWait, downloadId, new ChildrenRequirements<>(0, null, null));
    }
    public RetryEngine(int minutesToWait, String downloadId, ChildrenRequirements<D> childrenRequirements) {
        this.minutesToWait = minutesToWait;
        this.childrenRequirements = childrenRequirements;
        this.downloadId = downloadId;
    }

    public D tryUntilGotDesired(Supplier<D> tryToGet, IntConsumer waitFunction) {
        D desired = null;
        boolean waitForChildren = childrenRequirements.children > 0;
        while (Objects.isNull(desired)) {
            desired = tryToGet.get();
            if (Objects.isNull(desired)) {
                log("couldn't find the element");
                waitFunction.accept(minutesToWait);
            } else if (waitForChildren) {
                childrenCheckingLoop(desired, waitFunction);
            }
        }
        log("found desired element and returning it");
        return desired;
    }

    private void childrenCheckingLoop(D got, IntConsumer waitFunction) {
        boolean waitForChildren=true;
        boolean childrenConstraintSatisfied = childrenRequirements.constraint == null;
        while (waitForChildren) {
            List<D> children = childrenRequirements.retriever.apply(got);
            if (children.size() < childrenRequirements.children) {
                log("there is no enough child elements yet");
                waitFunction.accept(minutesToWait);
            } else {
                if (!childrenConstraintSatisfied) {
                    childrenConstraintSatisfied = children.stream().allMatch(childrenRequirements.constraint::apply);
                }
                else {
                    waitForChildren = false;
                    int shorterTime = minutesToWait / 3;
                    waitBeforeNextRetry(shorterTime, "waiting a bit more for courtesy: " + shorterTime + "min");
                }
            }
        }
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

    private void log (String msg) {
        Output.log(downloadId+"<RetryEngine>: "+msg);
    }

}
