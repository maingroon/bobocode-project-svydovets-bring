package com.bobocode.svydovets.beans.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

public class BeanUtilsTest {

    @ParameterizedTest
    @NullSource
    void validatePackage_shouldThrowExceptionWhenPackageNameIsNull(String path) {
        assertThrows(NullPointerException.class, () -> BeanUtils.validatePackageName(path));
    }

    @ParameterizedTest
    @EmptySource
    void validatePackage_shouldThrowExceptionWhenPackageNameIsEmpty(String path) {
        assertThrows(IllegalArgumentException.class, () -> BeanUtils.validatePackageName(path));
    }

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
