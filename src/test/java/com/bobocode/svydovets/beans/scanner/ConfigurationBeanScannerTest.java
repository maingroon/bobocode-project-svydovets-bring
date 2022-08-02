package com.bobocode.svydovets.beans.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import com.bobocode.svydovets.beans.scanner.quoter.books.DuneQuoter;
import org.junit.jupiter.api.Test;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.quoter.bookshelfs.FantasyBookshelf;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;
import com.bobocode.svydovets.exception.UnsupportedBeanTypeException;

class ConfigurationBeanScannerTest {

    private static final String PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";

    private static final String PACKAGE_WITH_DUPLICATE_EXCEPTION =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.duplicate";
    private static final String PACKAGE_WITH_VOID_BEAN_EXCEPTION =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.incorrecttype";
    private static final String INVALID_PACKAGE = "com.bobocode.svydovets.beans.scanner.quotermark";
    private static final ConfigurationBeanScanner SCANNER = new ConfigurationBeanScanner();

    @Test
    void scanSuccess() {
        var beanDefinitions = SCANNER.scan(PACKAGE);
        var beanName = "discworld";
        var discworldQuoterBeanName = "discworldQuoter";
        var duneQuoterBeanName = DuneQuoter.class.getName();
        var duneQuoter = beanDefinitions.get(duneQuoterBeanName);

        assertEquals(4, beanDefinitions.size());
        assertNotNull(beanDefinitions.get(beanName));
        assertNull(beanDefinitions.get(discworldQuoterBeanName));
        assertNotNull(beanDefinitions.get(duneQuoterBeanName));
        assertInstanceOf(BeanDefinition.class, beanDefinitions.get(beanName));
        assertNotNull(duneQuoter.getBeanMethod());
        assertEquals(FantasyBookshelf.class, duneQuoter.getBeanClass());
        assertEquals(DuneQuoter.class, duneQuoter.getBeanMethod().getReturnType());
    }

    @Test
    void scanDuplicateBenFail() {
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
}
