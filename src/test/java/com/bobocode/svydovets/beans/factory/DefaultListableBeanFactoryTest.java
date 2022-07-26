package com.bobocode.svydovets.beans.factory;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DefaultListableBeanFactoryTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    private Map<String, BeanDefinition> definitionMap;
    private BeanFactory factory;

    @BeforeAll
    public void setUp() {
        definitionMap = new ComponentBeanScanner().scan(ROOT_MOCK_PACKAGE);
        factory = new DefaultListableBeanFactory();
    }

    @Test
    void shouldCreateNewBeansMapFromBeanDefinitionsMaps() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // WHEN
        var beans = factory.createBeans(definitionMap);

        // THEN
        assertTrue(beans.containsKey("hp"));
        assertEquals(2, beans.size());
    }
}