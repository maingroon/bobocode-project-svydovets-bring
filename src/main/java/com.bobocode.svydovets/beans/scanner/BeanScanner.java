package com.bobocode.svydovets.beans.scanner;

import java.util.Map;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

public interface BeanScanner {

    Map<String, BeanDefinition> scan(String packageName);
}
