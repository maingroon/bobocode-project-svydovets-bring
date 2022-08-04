package com.bobocode.svydovets.beans.utils;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class BeanUtils {

    private BeanUtils() {
    }

    public static String validatePackageName(String packageName) {
        Objects.requireNonNull(packageName, "The packageName cannot be null! Please specify the packageName.");
        if (StringUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("The packageName is empty! Please specify the packageName.");
        }

        var result = packageName;
        if (packageName.contains("/") || packageName.contains("\\")) {
            result = StringUtils.replaceEachRepeatedly(packageName, new String[] {"/", "\\"}, new String[] {"."});
        }
        return result;
    }
}
