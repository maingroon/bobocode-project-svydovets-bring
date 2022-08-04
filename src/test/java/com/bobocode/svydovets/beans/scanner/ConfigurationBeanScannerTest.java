package com.bobocode.svydovets.beans.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import com.bobocode.svydovets.beans.scanner.example.postconstruct.valid.ConfigurationWithPostConstruct1;
import com.bobocode.svydovets.beans.scanner.example.postconstruct.valid.ConfigurationWithPostConstruct2;
import org.junit.jupiter.api.Test;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.quoter.books.DuneQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.bookshelfs.FantasyBookshelf;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;
import com.bobocode.svydovets.exception.UnsupportedBeanTypeException;

class ConfigurationBeanScannerTest {

    private static final String PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    private static final String PACKAGE_WITH_INJECTION = "com.bobocode.svydovets.beans.example.injection.bean";

    private static final String PACKAGE_WITH_DUPLICATE_EXCEPTION =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.duplicate";
    private static final String PACKAGE_WITH_VOID_BEAN_EXCEPTION =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.incorrecttype";
    private static final String INVALID_PACKAGE = "com.bobocode.svydovets.beans.scanner.quotermark";
    private static final String POST_CONSTRUCT_WITH_ARGS_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.args";
    private static final String POST_CONSTRUCT_RETURNS_VALUES_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.returns";
    private static final String STATIC_POST_CONSTRUCT_METHOD_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.stat";
    private static final String SEVERAL_POST_CONSTRUCT_METHODS_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.not.one";
    private static final String NO_CONFIGS_WITH_POST_CONSTRUCT_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.no";
    private static final String VALID_CONFIGS_WITH_POST_CONSTRUCT_METHODS_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.valid";
    private static final ConfigurationBeanScanner SCANNER = new ConfigurationBeanScanner();

    @Test
    void scanSuccess() {
        var beanDefinitions = SCANNER.scan(PACKAGE);
        var beanName = "discworld";
        var discworldQuoterBeanName = "discworldQuoter";
        var duneQuoterBeanName = DuneQuoter.class.getName();
        var duneQuoter = beanDefinitions.get(duneQuoterBeanName);

        assertEquals(3, beanDefinitions.size());
        assertNotNull(beanDefinitions.get(beanName));
        assertNull(beanDefinitions.get(discworldQuoterBeanName));
        assertNotNull(beanDefinitions.get(duneQuoterBeanName));
        assertInstanceOf(BeanDefinition.class, beanDefinitions.get(beanName));
        assertNotNull(duneQuoter.getFactoryMethod());
        assertEquals(FantasyBookshelf.class, duneQuoter.getConfigurationClass());
        assertEquals(DuneQuoter.class, duneQuoter.getBeanClass());
    }

    @Test
    void scanDuplicateBeanFail() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> SCANNER.scan(PACKAGE_WITH_DUPLICATE_EXCEPTION));
    }

    @Test
    void scanInvalidPackage() {
        var beanDefinitions = SCANNER.scan(INVALID_PACKAGE);
        assertEquals(Collections.emptyMap(), beanDefinitions);
    }

    @Test
    void scanVoidBeanFail() {
        assertThrows(UnsupportedBeanTypeException.class, () -> SCANNER.scan(PACKAGE_WITH_VOID_BEAN_EXCEPTION));
    }

    @Test
    void findDependsOn() {
        // GIVEN
        String aliceBeanName = "alice";
        String outBeanName = "out";

        // WHEN
        var beanDefinitions = SCANNER.scan(PACKAGE_WITH_INJECTION);
        SCANNER.fillDependsOn(beanDefinitions);

        // THEN
        assertEquals(3, beanDefinitions.size());

        String[] aliceDependsOn = beanDefinitions.get(aliceBeanName).getDependsOn();
        assertEquals(1, aliceDependsOn.length);
        assertEquals(outBeanName, aliceDependsOn[0]);

        assertEquals(0, beanDefinitions.get(outBeanName).getDependsOn().length);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfPostConstructHasArgs() {
        assertThrows(IllegalArgumentException.class, () -> SCANNER.scan(POST_CONSTRUCT_WITH_ARGS_PACKAGE));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfPostConstructReturnsValue() {

        assertThrows(IllegalArgumentException.class, () -> SCANNER.scan(POST_CONSTRUCT_RETURNS_VALUES_PACKAGE));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfPostConstructStatic() {
        assertThrows(IllegalArgumentException.class, () -> SCANNER.scan(STATIC_POST_CONSTRUCT_METHOD_PACKAGE));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfConfigHasSeveralPostConstruct() {
        assertThrows(IllegalArgumentException.class, () -> SCANNER.scan(SEVERAL_POST_CONSTRUCT_METHODS_PACKAGE));
    }

    @Test
    void postConstructInBeanDefinitionShouldBeNullIfConfigHasNoPostConstruct() {
        Map<String, BeanDefinition> beanDefinitionMap = SCANNER.scan(NO_CONFIGS_WITH_POST_CONSTRUCT_PACKAGE);
        assertTrue(beanDefinitionMap.values().stream().allMatch(bd -> bd.getPostConstructMethod() == null));
    }

    @Test
    void beansDefinitionsShouldHavePostConstructMethodsIfTypesHave() throws NoSuchMethodException {
        Map<String, BeanDefinition> beanDefinitionMap = SCANNER.scan(
          VALID_CONFIGS_WITH_POST_CONSTRUCT_METHODS_PACKAGE);
        BeanDefinition beanDefinition1 = beanDefinitionMap.get("config1");
        BeanDefinition beanDefinition2 = beanDefinitionMap.get(ConfigurationWithPostConstruct2.class.getName());

        assertEquals(beanDefinition1.getPostConstructMethod(), ConfigurationWithPostConstruct1.class.getMethod("postConstructMethod"));
        assertEquals(beanDefinition2.getPostConstructMethod(), ConfigurationWithPostConstruct2.class.getMethod("postConstructMethod"));
    }
}
