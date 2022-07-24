package com.bobocode.svydovets.beans.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

public class ComponentBeanScannerTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    public static final String INVALID_PACKAGE_NAME = "com.bobocode.invalid.package";
    private final ComponentBeanScanner scanner = new ComponentBeanScanner();

    @Test
    public void shouldScanPackageAndCreateBeanDefinitionWithProvidedName() {
        // GIVEN
        var beanName = "hp";

        // WHEN
        var beanDefinitions = scanner.scan(ROOT_MOCK_PACKAGE);

        // THEN
        var hp = beanDefinitions.get(beanName);
        assertEquals(HarryPotterQuoter.class, hp.getBeanClass());
        assertEquals(beanName, hp.getName());
    }

    @Test
    public void shouldScanPackageAndCreateBeanDefinitionWithDefaultName() {
        // GIVEN
        var beanName = DiscworldQuoter.class.getName();

        // WHEN
        var beanDefinitions = scanner.scan(ROOT_MOCK_PACKAGE);

        // THEN
        var hp = beanDefinitions.get(beanName);
        assertEquals(DiscworldQuoter.class, hp.getBeanClass());
        assertEquals(beanName, hp.getName());
    }

    @Test
    public void shouldReturnEmptyMapIfPackageIsInvalid() {
        // WHEN
        Map<String, BeanDefinition> beans = scanner.scan(INVALID_PACKAGE_NAME);

        // THEN
        assertEquals(0, beans.size());
    }
}
