package com.bobocode.svydovets.beans.scanner;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

import java.util.Map;

public interface BeanScanner {

    Map<String, BeanDefinition> scan(String packageName);
}
