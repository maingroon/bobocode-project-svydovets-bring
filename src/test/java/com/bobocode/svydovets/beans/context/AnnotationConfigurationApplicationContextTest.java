package com.bobocode.svydovets.beans.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.bobocode.svydovets.beans.example.injection.failure.no.bean.NotImplemented;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.DuneQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.Kobzar;
import com.bobocode.svydovets.beans.scanner.quoter.books.KobzarQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.TheNameOfTheWindQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.ZakharBerkut;
import com.bobocode.svydovets.beans.scanner.quoter.services.QuoterService;
import com.bobocode.svydovets.context.AnnotationConfigurationApplicationContext;
import com.bobocode.svydovets.context.ApplicationContext;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public class AnnotationConfigurationApplicationContextTest {

    public static final String START_PACKAGE = "com.bobocode.svydovets.beans.";
    public static final String MOCK_PACKAGE = START_PACKAGE + "scanner.quoter";
    public static final String NO_BEAN_FOR_INJECTION_PACKAGE_NAME = START_PACKAGE + "example.injection.failure.no.bean";
    public static final String NOT_UNIQUE_BEAN_FOR_INJECTION_PACKAGE_NAME =
      START_PACKAGE + "example.injection.failure.not.unique";

    private static ApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        context = new AnnotationConfigurationApplicationContext(MOCK_PACKAGE);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldContainsInitializedMapWithBeans() throws NoSuchFieldException, IllegalAccessException {
        Field beanContainerField = context.getClass().getDeclaredField("beanContainer");
        assertNotNull(beanContainerField);

        beanContainerField.setAccessible(true);
        Map<String, Object> beanContainer = (Map<String, Object>) beanContainerField.get(context);
        assertNotNull(beanContainer.get("hp"));
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenPackageNameIsNull(String packageName) {
        assertThrows(NullPointerException .class, () -> new AnnotationConfigurationApplicationContext(packageName));
    }

    @ParameterizedTest
    @EmptySource
    void shouldThrowExceptionWhenPackageNameIsEmpty(String packageName) {
        assertThrows(IllegalArgumentException.class, () -> new AnnotationConfigurationApplicationContext(packageName));
    }

    @Test
    void shouldThrowExceptionWhenBeanContainerDoesNotHaveBeanForInjection() {
        assertThrows(NoSuchBeanDefinitionException.class, () ->
          new AnnotationConfigurationApplicationContext(NO_BEAN_FOR_INJECTION_PACKAGE_NAME));
    }

    @Test
    void shouldThrowExceptionWhenBeanContainerContainsTwoBeansForInjectionWithoutSpecifyingName() {
        assertThrows(NoUniqueBeanDefinitionException.class, () ->
          new AnnotationConfigurationApplicationContext(NOT_UNIQUE_BEAN_FOR_INJECTION_PACKAGE_NAME));
    }

    @Test
    void shouldInjectBean() {
        HarryPotter harryPotterBook = (HarryPotter) (context.getBean("harryPotterBook"));

        assertNotNull(harryPotterBook);
        assertNotNull(harryPotterBook.getQuoter());
        assertEquals(HarryPotterQuoter.class, harryPotterBook.getQuoter().getClass());
    }

    @ParameterizedTest
    @NullSource
    void getBeanByName_shouldThrowExceptionWhenBeanNameIsNull(String beanName) {
        assertThrows(NullPointerException.class, () -> context.getBean(beanName));
    }

    @ParameterizedTest
    @EmptySource
    void getBeanByName_shouldThrowExceptionWhenBeanNameIsEmpty(String beanName) {
        assertThrows(IllegalArgumentException.class, () -> context.getBean(beanName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"beanNameThatNotContainsInTheBeanContainer"})
    void getBeanByName_shouldThrowExceptionWhenBeanContainerDoesNotHaveRequiredBean(String beanName) {
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(beanName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"hp", "harryPotterBook"})
    void getBeanByName_shouldReturnBeanWhenBeanContainerHaveRequiredBean(String beanName) {
        assertNotNull(context.getBean(beanName));
    }

    @ParameterizedTest
    @NullSource
    void getBeanByType_shouldThrowExceptionWhenBeanNameIsNull(Class<?> beanType) {
        assertThrows(NullPointerException.class, () -> context.getBean(beanType));
    }

    @ParameterizedTest
    @ValueSource(classes = {NotImplemented.class})
    void getBeanByType_shouldThrowExceptionWhenBeanContainerDoesNotHaveRequiredBean(Class<?> beanType) {
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(beanType));
    }

    @ParameterizedTest
    @ValueSource(classes = {Quoter.class})
    void getBeanByType_shouldThrowExceptionWhenBeanContainerHaveMoreNameOneRequiredBean(Class<?> beanType) {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> context.getBean(beanType));
    }

    @ParameterizedTest
    @ValueSource(classes = {HarryPotter.class, HarryPotterQuoter.class})
    void getBeanByType_shouldReturnBeanWhenBeanContainerHaveRequiredBean(Class<?> beanType) {
        assertNotNull(context.getBean(beanType));
    }

    @ParameterizedTest
    @MethodSource("provideNullNamesWithBeenClasses")
    void getBeanByNameAndType_shouldThrowExceptionWhenBeanNameIsNull(String beanName, Class<?> beanType) {
        assertThrows(NullPointerException.class, () -> context.getBean(beanName, beanType));
    }

    private static Stream<Arguments> provideNullNamesWithBeenClasses() {
        return Stream.of(
          Arguments.of(null, HarryPotter.class),
          Arguments.of(null, HarryPotterQuoter.class));
    }

    @ParameterizedTest
    @MethodSource("provideNamesWithNullBeenClasses")
    void getBeanByNameAndType_shouldThrowExceptionWhenBeanContainerDoesNotHaveRequiredBean(String beanName,
                                                                                           Class<?> beanType) {
        assertThrows(NullPointerException.class, () -> context.getBean(beanName, beanType));
    }

    private static Stream<Arguments> provideNamesWithNullBeenClasses() {
        return Stream.of(
          Arguments.of("harryPotterBook", null),
          Arguments.of("hp", null));
    }

    @ParameterizedTest
    @MethodSource("provideEmptyNamesWithBeenClasses")
    void getBeanByNameAndType_shouldThrowExceptionWhenBeanNameIsEmpty(String beanName, Class<?> beanType) {
        assertThrows(IllegalArgumentException.class, () -> context.getBean(beanName, beanType));
    }

    private static Stream<Arguments> provideEmptyNamesWithBeenClasses() {
        return Stream.of(
          Arguments.of("", HarryPotter.class),
          Arguments.of("", HarryPotterQuoter.class));
    }

    @ParameterizedTest
    @MethodSource("provideNotRealNamesWithBeenClasses")
    void getBeanByNameAndType_shouldThrowExceptionWhenBeanContainerDoesNotHaveBean(String beanName, Class<?> beanType) {
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(beanName, beanType));
    }

    private static Stream<Arguments> provideNotRealNamesWithBeenClasses() {
        return Stream.of(
          Arguments.of("bla-bla-bla", HarryPotter.class),
          Arguments.of("beanNameThatNotContainsInTheBeanContainer", HarryPotterQuoter.class));
    }

    @ParameterizedTest
    @MethodSource("provideNamesWithWrongBeenClasses")
    void getBeanByNameAndType_shouldThrowExceptionWhenBeanContainerHaveBeanNameButTypeIsWrong(String beanName,
                                                                                              Class<?> beanType) {
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(beanName, beanType));
    }

    private static Stream<Arguments> provideNamesWithWrongBeenClasses() {
        return Stream.of(
          Arguments.of("harryPotterBook", HarryPotterQuoter.class),
          Arguments.of("hp", DiscworldQuoter.class));
    }

    @ParameterizedTest
    @MethodSource("provideNamesWithBeenClasses")
    void getBeanByNameAndType_shouldReturnBeanWhenBeanContainerHaveBeanNameAndTypeIsAssignable(String beanName,
                                                                                               Class<?> beanType) {
        assertNotNull(context.getBean(beanName, beanType));
    }

    private static Stream<Arguments> provideNamesWithBeenClasses() {
        return Stream.of(
          Arguments.of("harryPotterBook", HarryPotter.class),
          Arguments.of("hp", Quoter.class),
          Arguments.of("hp", HarryPotterQuoter.class));
    }

    @ParameterizedTest
    @NullSource
    void getBeans_shouldThrowExceptionWhenPassedBeanTypeIsNull(Class<?> beanType) {
        assertThrows(NullPointerException.class, () -> context.getBeans(beanType));
    }

    @ParameterizedTest
    @ValueSource(classes = {TheNameOfTheWindQuoter.class})
    void getBeans_shouldReturnEmptyBeanMapWhenBeanContainerDoesNotHaveRequiredBean(Class<?> beanType) {
        Map<String, Object> beans = context.getBeans(beanType);

        assertNotNull(beans);
        assertEquals(0, beans.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {Quoter.class, HarryPotter.class, DuneQuoter.class})
    void getBeans_shouldReturnBeanMapWhenBeanContainerHaveRequiredBean(Class<?> beanType) {
        Map<String, Object> beans = context.getBeans(beanType);

        assertNotNull(beans);
        assertTrue(beans.size() > 0);
    }

    private void checkInjectedFields(Field field, Object bean) {
        try {
            field.setAccessible(true);
            assertNotNull(field.get(bean));
            assertTrue(Arrays.stream(bean.getClass().getDeclaredFields())
              .anyMatch(beanField -> beanField.getType().equals(field.getType())));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Arguments> provideBeanNamesWithBeenClasses() {
        return Stream.of(
          Arguments.of(null, QuoterService.class),
          Arguments.of(null, Kobzar.class),
          Arguments.of(null, KobzarQuoter.class),
          Arguments.of("zakhar_b", ZakharBerkut.class));
    }

    @ParameterizedTest
    @MethodSource("provideBeanNamesWithBeenClasses")
    void configurationBeanInjectionSuccess(String beanName, Class<?> beanType) {
        Object bean = getBean(beanName, beanType);
        assertInstanceOf(beanType, bean);
        Arrays.stream(bean.getClass().getDeclaredFields())
          .forEach(field -> checkInjectedFields(field, bean));
    }

    private Object getBean(String beanName, Class<?> beanType) {
        if (Objects.nonNull(beanName)) {
            return context.getBean(beanName, beanType);
        } else {
            Map<String, Object> beans = context.getBeans(beanType);
            assertNotNull(beans);
            return beans.get(beanType.getName());
        }
    }
}
