package com.bobocode.svydovets.beans.scanner.example.postconstruct.not.one;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;

@Component
public class ComponentWithSeveralPostConstruct {

    @PostConstruct
    public void postConstructMethod1() {
    }

    @PostConstruct
    public void postConstructMethod2() {
    }
}
