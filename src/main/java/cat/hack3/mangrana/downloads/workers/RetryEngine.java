package cat.hack3.mangrana.downloads.workers;

import cat.hack3.mangrana.utils.Output;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class RetryEngine<D> {

    private final int minutesToWait;
    private final ChildrenRequirements<D> childrenRequirements;
    private final Consumer<String> logger;

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

    public RetryEngine(int minutesToWait, Consumer<String> logger) {
        this(minutesToWait, new ChildrenRequirements<>(0, null, null), logger);
    }
    public RetryEngine(int minutesToWait, ChildrenRequirements<D> childrenRequirements, Consumer<String> logger) {
        this.minutesToWait = minutesToWait;
        this.childrenRequirements = childrenRequirements;
        this.logger = logger;
    }

    public D tryUntilGotDesired(Supplier<D> tryToGet) {
        IntConsumer defaultWaitFunction = time -> waitBeforeNextRetry(time, null);
        return tryUntilGotDesired(tryToGet, defaultWaitFunction);
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
                    int shorterTime = 1;
                    waitBeforeNextRetry(shorterTime, "waiting a bit more for courtesy: " + shorterTime + "min");
                }
            }
        }
    }

    public void waitBeforeNextRetry(int currentMinutesToWait, String forcedMessage) {
        String msg = StringUtils.isNotEmpty(forcedMessage)
                ? forcedMessage
                : "waiting "+currentMinutesToWait+" minutes before the next try - "+ Output.getCurrentTime();
        log(msg);
        try {
            TimeUnit.MINUTES.sleep(currentMinutesToWait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("failed TimeUnit.MINUTES.sleep");
            e.printStackTrace();
        }
    }

    private void log (String msg) {
        logger.accept("<RetryEngine> "+msg);
    }

}
