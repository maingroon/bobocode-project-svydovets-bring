package com.bobocode.svydovets.beans.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.exception.BeanInstantiationException;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.DefaultBeanScanner;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultListableBeanFactoryTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    public static final String EXCEPTION_INJECT_MOCK_PACKAGE =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.inject";
    private Map<String, BeanDefinition> definitionMap;
    private BeanFactory factory;

    @BeforeAll
    public void setUp() {
        var componentsScanResult = new ComponentBeanScanner().scan(ROOT_MOCK_PACKAGE);
        var configScanResult = new DefaultBeanScanner().scan(ROOT_MOCK_PACKAGE);
        definitionMap = Stream.of(componentsScanResult, configScanResult)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        factory = new DefaultListableBeanFactory();
    }

    @Test
    void shouldCreateNewBeansMapFromBeanDefinitionsMaps() {
        // WHEN
        var beans = factory.createBeans(definitionMap);

        // THEN
        assertTrue(beans.containsKey("hp"));
        assertTrue(beans.get("hp").getClass().isAssignableFrom(HarryPotterQuoter.class));
        assertTrue(beans.containsKey("discworld"));
        assertTrue(beans.containsKey("fantasyBookshelf"));
        assertEquals(6, beans.size());
    }

    @Test
    void configDeclaredBeanWithConstructorInjectionFail() {
        var configScanResult = new DefaultBeanScanner().scan(EXCEPTION_INJECT_MOCK_PACKAGE);
        assertThrowsExactly(UnsupportedOperationException.class, () -> factory.createBeans(configScanResult),
          "Creating bean instance with other injected beans is not yet supported");
    }

}
