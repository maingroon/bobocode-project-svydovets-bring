package com.bobocode.svydovets.beans.factory;

import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DefaultListableBeanFactoryTest {

    public static final String ROOT_MOCK_PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";
    private final ComponentBeanScanner scanner = new ComponentBeanScanner();
    private BeanFactory factory;

    @BeforeAll
    public void setUp() {
         factory = new DefaultListableBeanFactory(scanner.scan(ROOT_MOCK_PACKAGE));
    }

    @Test
    void shouldCreateNewHarryPotterQuoterBeanBySpecifiedName() {
        // GIVEN
        var beanType = HarryPotterQuoter.class;

        // WHEN
        var bean = factory.createBean(beanType);

        // THEN
        assertEquals(HarryPotterQuoter.class, beanType);
    }

    @Test
    void shouldCreateNewDiscworldQuoterBeanByItsTypeName() {
        // GIVEN
        var beanType = DiscworldQuoter.class;

        // WHEN
        var bean = factory.createBean(beanType);

        // THEN
        assertEquals(DiscworldQuoter.class, beanType);
    }

    @Test
    void shouldThrowNoSuchBeanDefinitionExceptionWhenBeanNotInTheDefinitionsMap() {
        // GIVEN
        var beanType = NoSuchBean.class;

        // THEN
        assertThrows(NoSuchBeanDefinitionException.class, () -> factory.createBean(beanType));
    }

    @Test
    void shouldCreateMapOfBeansByTheirTypes() {
        // GIVEN
        var hpType = HarryPotterQuoter.class;
        var discworlType = DiscworldQuoter.class;
        var exampleType = NoSuchBean.class;
        var typeSet = Set.of(hpType, discworlType, exampleType);

        // WHEN
        var map = factory.create(typeSet);

        // THEN
        assertNotNull(map);
        assertEquals(3, map.size());
        assertInstanceOf(Quoter.class, map.get(hpType.getName()));
    }
}