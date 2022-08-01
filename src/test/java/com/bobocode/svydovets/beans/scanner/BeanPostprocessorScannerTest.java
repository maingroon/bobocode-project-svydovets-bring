package com.bobocode.svydovets.beans.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Set;

import com.bobocode.svydovets.beans.bpp.BeanPostProcessor;
import com.bobocode.svydovets.beans.scanner.example.postprocessor.valid.BeanPostprocessor1;
import com.bobocode.svydovets.beans.scanner.example.postprocessor.valid.BeanPostprocessor2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

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

    @Test
    void shouldReturnSetWithRequiredPostprocessors() {
        Set<BeanPostProcessor> postprocessors = beanPostProcessorScanner.scan(PACKAGE_WITH_POSTPROCESSORS);
        assertTrue(postprocessors.stream()
          .anyMatch(postprocessor -> postprocessor.getClass().isAssignableFrom(BeanPostprocessor1.class)));
        assertTrue(postprocessors.stream()
          .anyMatch(postprocessor -> postprocessor.getClass().isAssignableFrom(BeanPostprocessor2.class)));
    }

}
