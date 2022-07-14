package com.bobocode.svydovets.beans;

import java.util.Map;

public interface BeanScanner {

    Map<String, BeanDefinition> scan(String packageName);
}
