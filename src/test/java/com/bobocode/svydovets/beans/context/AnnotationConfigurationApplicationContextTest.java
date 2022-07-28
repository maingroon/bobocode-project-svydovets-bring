package com.bobocode.svydovets.beans.context;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.bobocode.svydovets.beans.factory.BeanFactory;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.context.AnnotationConfigurationApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnnotationConfigurationApplicationContextTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    public static final String EMPTY_PACKAGE_NAME = "";

    @Test
    void shouldCallScanWhenContextisCreated() {
        var beanFactory = mock(BeanFactory.class);
        var componentBeanScanner = mock(ComponentBeanScanner.class);

        new AnnotationConfigurationApplicationContext(beanFactory, componentBeanScanner, ROOT_MOCK_PACKAGE);

        verify(componentBeanScanner, Mockito.times(1)).scan(ROOT_MOCK_PACKAGE);
    }

    @Test
    void shouldThrowExceptionWhenPackageIsEmpty() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new AnnotationConfigurationApplicationContext(EMPTY_PACKAGE_NAME);
        });

        var expectedMessage = "Package is empty! Please specify the package name.";
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
