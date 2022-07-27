package com.bobocode.svydovets.beans.scanner;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.bookshelfs.FantasyBookshelf;
import com.bobocode.svydovets.beans.scanner.quoter.bookshelfs.RandomBookshelf;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationBeanScannerTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    public static final String INVALID_PACKAGE_NAME = "com.bobocode.invalid.package";
    private final ConfigurationBeanScanner scanner = new ConfigurationBeanScanner();

    @Test
    void shouldScanPackageAndCreateBeanDefinitionWithProvidedName() {
        // GIVEN
        var beanName = "fantasy";

        // WHEN
        var beanDefinitions = scanner.scan(ROOT_MOCK_PACKAGE);

        // THEN
        var fantasy = beanDefinitions.get(beanName);
        assertEquals(beanName, "fantasy");
    }

    @Test
    void shouldScanPackageAndCreateBeanDefinitionWithDefaultName() {
        // GIVEN
        var beanName = RandomBookshelf.class.getName();

        // WHEN
        var beanDefinitions = scanner.scan(ROOT_MOCK_PACKAGE);

        // THEN
        var random = beanDefinitions.get(beanName);
        assertEquals(RandomBookshelf.class, random.getBeanClass());
        assertEquals(beanName, random.getName());
    }

    @Test
    void shouldReturnEmptyMapIfPackageIsInvalid() {
        // WHEN
        Map<String, BeanDefinition> beans = scanner.scan(INVALID_PACKAGE_NAME);

        // THEN
        assertEquals(0, beans.size());
    }
}
