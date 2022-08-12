package cat.hack3.mangrana.utils;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCaptor {

    public static String getMatchingSubstring(String fullText, String matchingRegex) {
        Pattern pattern = Pattern.compile(matchingRegex);
        Matcher matcher = pattern.matcher(fullText);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static String getSeasonFolderNameFromSeason(String seasonFolderName) throws IncorrectWorkingReferencesException {
        String season = Optional.ofNullable(
                        StringCaptor.getMatchingSubstring(seasonFolderName, "(S\\d{2})"))
                .orElseThrow(() ->
                        new IncorrectWorkingReferencesException("Couldn't determinate the season from: "+seasonFolderName));
        return season.replaceFirst("S", "Temporada ");
    }

    public static String getSeasonFolderNameFromEpisode(String episodeFileName) throws IncorrectWorkingReferencesException {
        String episodeInfo = Optional.ofNullable(
                        StringCaptor.getMatchingSubstring(episodeFileName, "(S\\d{2}E\\d{2})"))
                .orElseThrow(() ->
                        new IncorrectWorkingReferencesException("Couldn't determinate the episode from: "+episodeFileName));
        return "Temporada ".concat(episodeInfo.substring(1,3));
    }

}
