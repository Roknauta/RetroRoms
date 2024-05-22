package com.roknauta.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AppFileUtils {

    public static final String ACCEPTED_REGIONS = "USA,Brazil,Europe,World";

    public static boolean isValid(String fullName) {
        List<String> invalidWords = List.of("Beta", "Proto", "Sample");
        List<String> keywords = getKeywords(fullName);
        for (String invalidWord : invalidWords) {
            for (String keyword : keywords) {
                if (StringUtils.containsIgnoreCase(keyword, invalidWord)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getRegions(String fullName) {
        List<String> regions = Arrays.stream(ACCEPTED_REGIONS.split(",")).toList();
        List<String> subStrings = getKeywords(fullName);
        String region = null;
        for (String subString : subStrings) {
            if (Arrays.stream(subString.split(",")).anyMatch(regions::contains)) {
                region = subString;
                break;
            }
        }
        return region;
    }

    public static List<String> getRegionsList(String fullName){
        return Arrays.stream(getRegions(fullName).split(",")).map(String::trim).collect(Collectors.toList());
    }

    public static List<String> getKeywords(String fullName) {
        String[] subStrings = StringUtils.substringsBetween(fullName, "(", ")");
        return subStrings == null ? Collections.emptyList() : Arrays.stream(subStrings).toList();
    }

    public static String getBaseName(String fullName) {
        String regions = getRegions(fullName);
        return StringUtils.substringBefore(fullName,"(");
    }

}
