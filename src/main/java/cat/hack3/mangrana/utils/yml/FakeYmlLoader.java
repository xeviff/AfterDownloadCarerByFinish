package cat.hack3.mangrana.utils.yml;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cat.hack3.mangrana.utils.Output.log;

/**
 * Only applicable to the following format
 * key1: value1
 * key:2 value2
 */
public class FakeYmlLoader {

    private FakeYmlLoader(){}

    @SuppressWarnings("all")
    public static <E extends Enum<E>> EnumMap<E, String> getEnumMapFromFile(File ymlFile, Class<E> enumData) throws IncorrectWorkingReferencesException {
        EnumMap<E, String> enumMap = new EnumMap<>(enumData);
        log("Loading <key: value> values from the file "+ymlFile.getAbsolutePath());
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
            log("mapped values to EnumMap "+enumData.getCanonicalName());
            return enumMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
