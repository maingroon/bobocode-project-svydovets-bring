package com.bobocode.svydovets.beans.factory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultListableBeanFactoryTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    private Map<String, BeanDefinition> definitionMap;
    private BeanFactory factory;

    @BeforeAll
    public void setUp() {
        definitionMap = new ComponentBeanScanner().scan(ROOT_MOCK_PACKAGE);
        factory = new DefaultListableBeanFactory();
    }

    @Test
    void shouldCreateNewBeansMapFromBeanDefinitionsMaps() {
        // WHEN
        var beans = factory.createBeans(definitionMap);

        // THEN
        assertTrue(beans.containsKey("hp"));
        assertTrue(beans.get("hp").getClass().isAssignableFrom(HarryPotterQuoter.class));
    }

}