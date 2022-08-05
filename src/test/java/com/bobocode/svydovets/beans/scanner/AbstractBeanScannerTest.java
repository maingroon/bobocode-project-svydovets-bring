package com.bobocode.svydovets.beans.scanner;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.DuneQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.RandomQuoter;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.bobocode.svydovets.beans.scanner.AbstractBeanScanner.findDependsOnByClass;
import static com.bobocode.svydovets.beans.scanner.AbstractBeanScanner.findDependsOnByName;

public class AbstractBeanScannerTest {

    @Test
    void shouldGetCorrectTypeName() {
        // WHEN
        String typeName = AbstractBeanScanner.getTypeName(AbstractBeanScanner.class);

        // THEN
        Assertions.assertEquals(StringUtils.uncapitalize(AbstractBeanScanner.class.getName()), typeName) ;
    }

    @Test
    void shouldFindDependsOnByName() {
        // GIVEN
        var map = new HashMap<String, BeanDefinition>();
        String fieldName = "hp";
        map.put(fieldName,
          BeanDefinition.builder()
            .name(fieldName)
            .beanClass(HarryPotterQuoter.class)
            .build());

        // WHEN
        String dependsOnByName = findDependsOnByName(map, fieldName);

        // THEN
        Assertions.assertEquals(fieldName, dependsOnByName);
    }

    @Test
    void shouldFindDependsOnByNameShould_throwNoSuchBean() {
        // GIVEN
        var map = new HashMap<String, BeanDefinition>();
        String fieldName = "hp";

        // WHEN-THEN
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> findDependsOnByName(map, fieldName));
    }

    @Test
    void shouldFindDependsOnByClass_whenBeanHasCustomName() {
        // GIVEN
        var map = new HashMap<String, BeanDefinition>();
        String fieldName = "hp";
        map.put(fieldName, BeanDefinition.builder()
          .name(fieldName)
          .beanClass(HarryPotterQuoter.class)
          .build());

        // WHEN
        String dependsOnByClass = findDependsOnByClass(map, RandomQuoter.class, HarryPotterQuoter.class);

        // THEN
        Assertions.assertEquals(fieldName, dependsOnByClass);
    }

    @Test
    void shouldFindDependsOnByClass_whenBeanHasDefaultName() {
        // GIVEN
        var map = new HashMap<String, BeanDefinition>();
        String fieldName = HarryPotterQuoter.class.getName();
        map.put(fieldName, BeanDefinition.builder()
          .name(fieldName)
          .beanClass(HarryPotterQuoter.class)
          .build());

        // WHEN
        String dependsOnByClass = findDependsOnByClass(map, RandomQuoter.class, HarryPotterQuoter.class);

        // THEN
        Assertions.assertEquals(fieldName, dependsOnByClass);
    }

    @Test
    void shouldFindDependsOnByClass_throwNoSuchBeen() {
        // GIVEN
        var map = new HashMap<String, BeanDefinition>();

        // WHEN-THEN
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> findDependsOnByClass(map, RandomQuoter.class, HarryPotterQuoter.class));
    }

    @Test
    void shouldFindDependsOnByClass_throwNoUniqueBeen() {
        // GIVEN
        var map = new HashMap<String, BeanDefinition>();
        map.put("hp", BeanDefinition.builder().name("hp").beanClass(HarryPotterQuoter.class).build());
        map.put("dune", BeanDefinition.builder().name("dune").beanClass(DuneQuoter.class).build());


        // WHEN-THEN
        Assertions.assertThrows(NoUniqueBeanDefinitionException.class,
                () -> findDependsOnByClass(map, RandomQuoter.class, Quoter.class));
    }
}
