package com.bobocode.svydovets.beans.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bobocode.svydovets.beans.factory.example.postconstruct.ComponentWithPostConstruct1;
import com.bobocode.svydovets.beans.factory.example.postconstruct.ComponentWithPostConstruct2;
import com.bobocode.svydovets.beans.scanner.quoter.bookshelfs.FantasyBookshelf;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.BeanPostprocessorScanner;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.ConfigurationBeanScanner;
import com.bobocode.svydovets.beans.scanner.quoter.books.BeanPostprocessor1;
import com.bobocode.svydovets.beans.scanner.quoter.books.BeanPostprocessor2;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultListableBeanFactoryTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    public static final String PACKAGE_WITH_POSTPROCESSORS = "com.bobocode.svydovets.beans.scanner.quoter.books";
    public static final String PACKAGE_WITH_POST_CONSTRUCTS = "com.bobocode.svydovets.beans.factory.example.postconstruct";
    public static final String EXCEPTION_INJECT_MOCK_PACKAGE =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.inject";
    private Map<String, BeanDefinition> definitionMap;
    private BeanFactory factory;
    private BeanPostprocessorScanner postprocessorScanner;

    @BeforeAll
    public void setUp() {
        var componentsScanResult = new ComponentBeanScanner().scan(ROOT_MOCK_PACKAGE);
        var configScanResult = new ConfigurationBeanScanner().scan(ROOT_MOCK_PACKAGE);
        definitionMap = Stream.of(componentsScanResult, configScanResult)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        postprocessorScanner = new BeanPostprocessorScanner();
    }

    @Test
    void shouldCreateNewBeansMapFromBeanDefinitionsMaps() {
        // WHEN

        factory = new DefaultListableBeanFactory(postprocessorScanner.scan(ROOT_MOCK_PACKAGE));
        var beans = factory.createBeans(definitionMap);

        // THEN
        assertTrue(beans.containsKey("hp"));
        assertTrue(beans.get("hp").getClass().isAssignableFrom(HarryPotterQuoter.class));
        assertTrue(beans.containsKey("discworld"));
        assertTrue(beans.containsKey(FantasyBookshelf.class.getName()));
    }

    @Test
    void shouldCallPostProcessBeforeInitializationAfterCreationBeans() {
        BeanPostprocessor1 beanPostprocessor1 = Mockito.spy(BeanPostprocessor1.class);
        BeanPostprocessor2 beanPostprocessor2 = Mockito.spy(BeanPostprocessor2.class);

        factory = new DefaultListableBeanFactory(Set.of(beanPostprocessor1, beanPostprocessor2));
        Map<String, Object> beans = factory.createBeans(new ComponentBeanScanner().scan(PACKAGE_WITH_POSTPROCESSORS));

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Mockito.verify(beanPostprocessor1, Mockito.times(1)).postProcessBeforeInitialization(entry.getValue(), entry.getKey());
            Mockito.verify(beanPostprocessor2, Mockito.times(1)).postProcessBeforeInitialization(entry.getValue(), entry.getKey());
        }
    }

    @Test
    void postConstructMethodShouldInitializeValues() {

        factory = new DefaultListableBeanFactory(Collections.EMPTY_SET);
        Map<String, Object> beans = factory.createBeans(new ComponentBeanScanner().scan(PACKAGE_WITH_POST_CONSTRUCTS));

        ComponentWithPostConstruct1 bean1 = ((ComponentWithPostConstruct1) beans.get("bean1"));
        assertEquals(10, bean1.getIntVar());
        assertEquals("test", bean1.getStrVar());

        ComponentWithPostConstruct2 bean2 = ((ComponentWithPostConstruct2) beans.get(ComponentWithPostConstruct2.class.getName()));
        assertEquals(20, bean2.getIntVar());
        assertEquals("test2", bean2.getStrVar());
    }

    @Test
    void createBean_shouldReturnEmptyMapWhenDefinitionMapIsEmpty() {
        factory = new DefaultListableBeanFactory(Collections.EMPTY_SET);
        Map<String, Object> beans = factory.createBeans(Map.of());
        assertTrue(beans.isEmpty());
    }
}
