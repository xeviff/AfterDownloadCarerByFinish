package cat.hack3.mangrana.downloads.workers;

import java.util.List;

public interface ParametrizedHandler extends Handler {
    @Override
    default void handle() {
        handle(null);
    }

    void handle(List<String> parameters);
}
