package cat.hack3.mangrana.utils.yml;


import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

import static cat.hack3.mangrana.utils.Output.log;

public class YmlFileLoader {

    private YmlFileLoader(){}

    @SuppressWarnings("all")
    public static <E extends Enum<E>> EnumMap<E, String> getEnumMapFromFile(File ymlFile, Class<E> enumData) throws IncorrectWorkingReferencesException {
        log("Loading yml values from the file...");
        try {
            EnumMap<E, String> valuesMap = new EnumMap<>(enumData);
            YamlMapping yamlMapping = Yaml.createYamlInput(ymlFile)
                    .readYamlMapping();

            for (E constant : enumData.getEnumConstants()) {
                String value = yamlMapping.string(constant.name().toLowerCase());
                if (StringUtils.isEmpty(value))
                    log("Couldn't retrieve the value from " + constant.name());
                else
                    valuesMap.put((E) constant, value);
            }

            return valuesMap;
        } catch (IOException e) {
            throw new IncorrectWorkingReferencesException("couldn't find the config file :(");
        }
    }
}
