package cat.hack3.mangrana.downloads.workers;

import com.google.api.services.drive.model.File;
import org.apache.commons.lang.StringUtils;

import java.rmi.UnexpectedException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static cat.hack3.mangrana.utils.Output.log;

public class RetryEngine {

    private final int minutesToWait;
    private final int elementsMustHave;
    private final Function<File, List<File>> childrenRetriever;

    public RetryEngine(int minutesToWait) {
        this(minutesToWait, 0, null);
    }
    public RetryEngine(int minutesToWait, int elementsMustHave, Function<File, List<File>> childrenRetriever) {
        this.minutesToWait = minutesToWait;
        this.elementsMustHave = elementsMustHave;
        this.childrenRetriever = childrenRetriever;
    }

    public File tryWaitAndRetry (Supplier<File> checker) throws UnexpectedException {
        File file = null;
        boolean waitForChildren = elementsMustHave > 0;
        while (Objects.isNull(file) && waitForChildren) {
            file = checker.get();
            if (Objects.isNull(file)) {
                waitBeforeNextRetry(minutesToWait, null);
            } else if (waitForChildren) {
                List<File> children = childrenRetriever.apply(file);
                if (children.size() < elementsMustHave) {
                    waitBeforeNextRetry(minutesToWait, null);
                } else {
                    int shorterTime = minutesToWait/3;
                    waitBeforeNextRetry(shorterTime, "waiting a bit more for courtesy: "+shorterTime+"min");
                    waitForChildren = false;
                }
            }
        }
        return file;
    }

    public void waitBeforeNextRetry(int currentMinutesToWait, String forcedMessage) throws UnexpectedException {
        String msg = StringUtils.isNotEmpty(forcedMessage)
                ? forcedMessage
                : "waiting "+currentMinutesToWait+" minutes before the next try";
        log(msg);
        try {
            TimeUnit.MINUTES.sleep(currentMinutesToWait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UnexpectedException("failed TimeUnit.MINUTES.sleep");
        }
    }

}
