package cat.hack3.mangrana.utils.yml;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.EasyLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Only applicable to the following format
 * key1: value1
 * key:2 value2
 */
public class FakeYmlLoader {

    private static final EasyLogger logger = new EasyLogger(FakeYmlLoader.class);

    private FakeYmlLoader(){}

    @SuppressWarnings("all")
    public static <E extends Enum<E>> EnumMap<E, String> getEnumMapFromFile(File ymlFile, Class<E> enumData, boolean silently) throws IncorrectWorkingReferencesException {
        EnumMap<E, String> enumMap = new EnumMap<>(enumData);
        Pattern simpleYmlKeyValuePattern = Pattern.compile(".+: .+");
        try (Stream<String> stream = Files.lines(ymlFile.toPath())) {
            Map<String, String> fileLines = stream
                    .filter(keyValueCandidate -> simpleYmlKeyValuePattern
                            .matcher(keyValueCandidate)
                            .matches())
                    .map(s -> s.split(":"))
                    .collect(Collectors.toMap(e -> e[0], e -> e[1].trim()));
            Arrays.stream(enumData.getEnumConstants())
                    .forEach(cons ->
                            enumMap.put(cons, fileLines.get(cons.name().toLowerCase()))
                    );
            if (!silently) {
                logger.nLog("Mapped <key: value> from file {0} to -{1}- EnumMap",
                        ymlFile.getAbsolutePath(), enumData.getSimpleName());
            }
            return enumMap;
        } catch (IOException e) {
            logger.nHLog("Some problem occurred trying to map file {0} to EnumMap {1}",
                    ymlFile.getAbsolutePath(), enumData.getName());
            e.printStackTrace();
        }
        return null;
    }

}
