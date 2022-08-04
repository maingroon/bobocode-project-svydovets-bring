package com.bobocode.svydovets.beans.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BeanUtilsTest {

    @ParameterizedTest
    @MethodSource("provideSlashBasePaths")
    void validatePackage_shouldConvertNotContainsSlashesAfterValidation(String path) {
        assertTrue(StringUtils.containsNone(BeanUtils.validatePackageName(path), "/"));
    }

    @ParameterizedTest
    @MethodSource("provideSlashBasePaths")
    void validatePackage_shouldConvertNotContainsBackSlashesAfterValidation(String path) {
        assertTrue(StringUtils.containsNone(BeanUtils.validatePackageName(path), "\\"));
    }

    @ParameterizedTest
    @MethodSource("provideSlashBasePaths")
    void validatePackage_shouldConvertContainsDotsAfterValidation(String path) {
        assertTrue(StringUtils.contains(BeanUtils.validatePackageName(path), "."));
    }

    private static Stream<Arguments> provideSlashBasePaths() {
        return Stream.of(Arguments.of("com/bobocode/svydovets/beans/"),
          Arguments.of("example/injection/failure/no/bean"),
          Arguments.of("example\\injection\\failure\\not\\unique"));
    }

}
