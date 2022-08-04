package com.bobocode.svydovets.beans.scanner;

import java.util.Arrays;
import java.util.Map;

import com.bobocode.svydovets.beans.scanner.example.postconstruct.valid.ComponentWithPostConstruct1;
import com.bobocode.svydovets.beans.scanner.example.postconstruct.valid.ComponentWithPostConstruct2;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

import static org.junit.jupiter.api.Assertions.*;


public class ComponentBeanScannerTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    public static final String INVALID_PACKAGE_NAME = "com.bobocode.invalid.package";
    private static final String NO_UNIQUE_PACKAGE_NAME
            = "com.bobocode.svydovets.beans.scanner.exception.quoter.inject.nounique";
    private static final String NO_SUCH_BEAN_PACKAGE_NAME
            = "com.bobocode.svydovets.beans.scanner.exception.quoter.inject.nosuchbean";
    private static final String POST_CONSTRUCT_WITH_ARGS_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.args";
    private static final String POST_CONSTRUCT_RETURNS_VALUES_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.returns";
    private static final String NO_COMPONENTS_WITH_POST_CONSTRUCT_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.no";
    private static final String POST_CONSTRUCT_METHOD_STATIC_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.stat";
    private static final String SEVERAL_POST_CONSTRUCT_METHODS_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.not.one";
    private static final String VALID_COMPONENTS_WITH_POST_CONSTRUCT_METHODS_PACKAGE = "com.bobocode.svydovets.beans.scanner.example.postconstruct.valid";
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
        var beanName = StringUtils.uncapitalize(DiscworldQuoter.class.getName());

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

    @Test
    public void shouldThrowNoUniquePackageNameIfInjectHasMoreThanOneBeanToInject() {
        // WHEN
        Map<String, BeanDefinition> beans = scanner.scan(NO_UNIQUE_PACKAGE_NAME);

        // THEN
        assertThrows(NoUniqueBeanDefinitionException.class, () -> scanner.fillDependsOn(beans));
    }

    @Test
    public void shouldThrowNoSuchBeanIfThereIsNoBeanToInject() {
        // WHEN
        Map<String, BeanDefinition> beans = scanner.scan(NO_SUCH_BEAN_PACKAGE_NAME);

        // THEN
        assertThrows(NoSuchBeanDefinitionException.class, () -> scanner.fillDependsOn(beans));
    }

    @Test
    public void shouldFillDependsOn() {
        // WHEN
        Map<String, BeanDefinition> beans = scanner.scan(ROOT_MOCK_PACKAGE);
        scanner.fillDependsOn(beans);

        // THEN
        var randomBeanDependsOn = beans.get("random").getDependsOn();
        assertEquals(randomBeanDependsOn.length, 2);
        assertTrue(Arrays.asList(randomBeanDependsOn).contains("hp"));
        assertTrue(Arrays.asList(randomBeanDependsOn).contains(
                StringUtils.uncapitalize(DiscworldQuoter.class.getName())));

        var harryPotterBookBeanDependsOn = beans.get("harryPotterBook").getDependsOn();
        assertEquals(harryPotterBookBeanDependsOn.length, 1);
        assertTrue(Arrays.asList(randomBeanDependsOn).contains("hp"));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfPostConstructHasArgs() {
        assertThrows(IllegalArgumentException.class, () -> scanner.scan(POST_CONSTRUCT_WITH_ARGS_PACKAGE));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfPostConstructReturnsValue() {
        assertThrows(IllegalArgumentException.class, () -> scanner.scan(POST_CONSTRUCT_RETURNS_VALUES_PACKAGE));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfPostConstructStatic() {
        assertThrows(IllegalArgumentException.class, () -> scanner.scan(POST_CONSTRUCT_METHOD_STATIC_PACKAGE));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfBeanHasSeveralPostConstruct() {
        assertThrows(IllegalArgumentException.class, () -> scanner.scan(SEVERAL_POST_CONSTRUCT_METHODS_PACKAGE));
    }

    @Test
    void postConstructInBeanDefinitionShouldBeNullIfComponentHasNoPostConstruct() {
        Map<String, BeanDefinition> beanDefinitionMap = scanner.scan(NO_COMPONENTS_WITH_POST_CONSTRUCT_PACKAGE);
        assertTrue(beanDefinitionMap.values().stream().allMatch(bd -> bd.getPostConstructMethod() == null));
    }

    @Test
    void beansDefinitionsShouldHavePostConstructMethodsIfTypesHave() throws NoSuchMethodException {
        Map<String, BeanDefinition> beanDefinitionMap = scanner.scan(
          VALID_COMPONENTS_WITH_POST_CONSTRUCT_METHODS_PACKAGE);
        BeanDefinition beanDefinition1 = beanDefinitionMap.get("bean1");
        BeanDefinition beanDefinition2 = beanDefinitionMap.get(ComponentWithPostConstruct2.class.getName());

        assertEquals(beanDefinition1.getPostConstructMethod(), ComponentWithPostConstruct1.class.getMethod("postConstructMethod"));
        assertEquals(beanDefinition2.getPostConstructMethod(), ComponentWithPostConstruct2.class.getMethod("postConstructMethod"));
    }
}
