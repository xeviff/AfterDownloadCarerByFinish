package cat.hack3.mangrana.downloads.workers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

import static cat.hack3.mangrana.utils.Output.msg;
import static cat.hack3.mangrana.utils.Waiter.waitMinutes;
import static cat.hack3.mangrana.utils.Waiter.waitSeconds;

public class RetryEngine<D> {

    private final String title;
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

    public RetryEngine(String title, int minutesToWait, Consumer<String> logger) {
        this(title, minutesToWait, new ChildrenRequirements<>(0, null, null), logger);
    }
    public RetryEngine(String title, int minutesToWait, ChildrenRequirements<D> childrenRequirements, Consumer<String> logger) {
        this.title = title;
        this.minutesToWait = minutesToWait;
        this.childrenRequirements = childrenRequirements;
        this.logger = logger;
    }

    public D tryUntilGotDesired(Supplier<D> tryToGet) {
        IntConsumer defaultWaitFunction = time -> waitBeforeNextRetry(time, Optional.empty());
        return tryUntilGotDesired(tryToGet, defaultWaitFunction);
    }

    public D tryUntilGotDesired(Supplier<D> tryToGet, IntConsumer waitFunction) {
        D desired = null;
        boolean waitForChildren = childrenRequirements.children > 0;
        while (Objects.isNull(desired)) {
            desired = tryToGet.get();
            if (Objects.isNull(desired)) {
                waitFunction.accept(minutesToWait);
            } else if (waitForChildren) {
                childrenCheckingLoop(desired);
            }
        }
        log("the try was satisfied and will return the desired element/s");
        return desired;
    }

    private void childrenCheckingLoop(D got) {
        boolean waitForChildren=true;
        boolean childrenConstraintSatisfied = childrenRequirements.constraint == null;
        while (waitForChildren) {
            List<D> children = childrenRequirements.retriever.apply(got);
            if (children.size() < childrenRequirements.children) {
                waitBeforeNextRetry(minutesToWait, Optional.of("Not enough children yet"));
            } else {
                if (!childrenConstraintSatisfied) {
                    childrenConstraintSatisfied = children.stream().allMatch(childrenRequirements.constraint::apply);
                }
                else {
                    waitForChildren = false;
                    waitSeconds(50);
                }
            }
        }
    }

    public void waitBeforeNextRetry(int currentMinutesToWait, Optional<String> message) {
        String descriptionMessage = "Desired element/s not found";
        String waitingMessage = msg(" and will retry after {0} minutes", currentMinutesToWait);
        if (message.isPresent()) descriptionMessage = message.get();
        log(descriptionMessage + waitingMessage);
        waitMinutes(currentMinutesToWait);
    }

    private void log (String msg) {
        logger.accept(msg("<RetryEngine.{0}> {1}", title, msg));
    }

}
