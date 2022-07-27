package com.bobocode.svydovets.beans.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.bookshelfs.FantasyBookshelf;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;
import com.bobocode.svydovets.exception.UnsupportedBeanTypeException;

class DefaultBeanScannerTest {

    private static final String PACKAGE = "com.bobocode.svydovets.beans.scanner.quoter";

    private static final String PACKAGE_WITH_DUPLICATE_EXCEPTION =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.duplicate";
    private static final String PACKAGE_WITH_VOID_BEAN_EXCEPTION =
      "com.bobocode.svydovets.beans.scanner.exception.bookshelves.incorrecttype";
    private static final String INVALID_PACKAGE = "com.bobocode.svydovets.beans.scanner.quotermark";
    private static final DefaultBeanScanner SCANNER = new DefaultBeanScanner();

    @Test
    void scanSuccess() {
        var beanDefinitions = SCANNER.scan(PACKAGE);
        var beanName = "discworld";
        assertEquals(4, beanDefinitions.size());
        assertNotNull(beanDefinitions.get(beanName));
        assertNull(beanDefinitions.get(DiscworldQuoter.class.getSimpleName()));
        assertNotNull(beanDefinitions.get(HarryPotterQuoter.class.getSimpleName()));
        assertInstanceOf(BeanDefinition.class, beanDefinitions.get(beanName));
        var hpBeanDef = beanDefinitions.get(HarryPotterQuoter.class.getSimpleName());
        assertNotNull(hpBeanDef.getBeanMethod());
        assertEquals(FantasyBookshelf.class, hpBeanDef.getBeanClass());
        assertEquals(HarryPotterQuoter.class, hpBeanDef.getBeanMethod().getReturnType());
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
