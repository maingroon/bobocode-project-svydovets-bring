package com.bobocode.svydovets.beans.scanner.example.postconstruct.stat;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;

@Component
public class ComponentWithStaticPostConstruct {

    @PostConstruct
    public static void staticPostConstruct() {
    }
}
