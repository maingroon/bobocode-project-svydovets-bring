package com.bobocode.svydovets.beans.scanner;

import java.util.Map;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public interface BeanScanner {

    Map<String, BeanDefinition> scan(String packageName);
}
