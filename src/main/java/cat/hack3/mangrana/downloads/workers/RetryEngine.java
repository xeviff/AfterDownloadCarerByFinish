package cat.hack3.mangrana.downloads.workers;

import cat.hack3.mangrana.exception.TooMuchTriesException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
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

    public D tryUntilGotDesired(Supplier<D> tryToGet) throws TooMuchTriesException {
        AtomicInteger loopCount = new AtomicInteger(1);
        D desired = null;
        boolean waitForChildren = childrenRequirements.children > 0;
        while (Objects.isNull(desired)) {
            desired = tryToGet.get();
            if (Objects.isNull(desired)) {
                waitLoopBehaviour(loopCount,
                        "Too much tries when retrieving desired element",
                        msg("The element was not found yet and will retry every {0} minutes", minutesToWait)
                );
            } else if (waitForChildren) {
                childrenCheckingLoop(desired);
            }
        }
        log("the try was satisfied and will return the desired element/s");
        return desired;
    }

    private void childrenCheckingLoop(D got) throws TooMuchTriesException {
        AtomicInteger loopCount = new AtomicInteger(1);
        boolean waitForChildren=true;
        boolean childrenConstraintSatisfied = childrenRequirements.constraint == null;
        while (waitForChildren) {
            List<D> children = childrenRequirements.retriever.apply(got);
            if (children.size() < childrenRequirements.children) {
                waitLoopBehaviour(loopCount,
                        msg("Too much tries when retrieving children from {0} while current is {1} and expected {2}",
                                got.toString(), children.size(), childrenRequirements.children),
                        msg("Not enough children yet and will retry every {0} minutes", minutesToWait)
                );
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

    private void waitLoopBehaviour(AtomicInteger loopCount, String noticeMessage, String errorMessage) throws TooMuchTriesException {
        if (loopCount.get() == 10)
            throw new TooMuchTriesException(errorMessage);
        if (loopCount.get()==1) log(noticeMessage);
        loopCount.incrementAndGet();
        waitMinutes(minutesToWait);
    }

    private void log (String msg) {
        logger.accept(msg("<RetryEngine.{0}> {1}", title, msg));
    }

}
