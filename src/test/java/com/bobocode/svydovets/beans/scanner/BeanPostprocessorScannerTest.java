package com.bobocode.svydovets.beans.scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanPostprocessorScannerTest {
    private static final String EMPTY_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postprocessor.empty";
    private static final String PACKAGE_WITH_POSTPROCESSORS = "com.bobocode.svydovets.beans.scanner.example.postprocessor.valid";
    private static final BeanPostprocessorScanner beanPostProcessorScanner = new BeanPostprocessorScanner();

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenPackageNameIsNull(String packageName) {
        assertThrows(NullPointerException.class, () -> beanPostProcessorScanner.scan(packageName));
    }

    @ParameterizedTest
    @EmptySource
    void shouldThrowExceptionWhenPackageNameIsEmpty(String packageName) {
        assertThrows(IllegalArgumentException.class, () -> beanPostProcessorScanner.scan(packageName));
    }

    @Test
    void shouldReturnEmptySetIfInPackageNoPostprocessors() {
        assertEquals(Collections.EMPTY_SET, beanPostProcessorScanner.scan(EMPTY_PACKAGE));
    }

    @Test
    void shouldReturnSetWithSizeAsPostprocessorsCount() {
        assertEquals(2, beanPostProcessorScanner.scan(PACKAGE_WITH_POSTPROCESSORS).size());
    }

}
