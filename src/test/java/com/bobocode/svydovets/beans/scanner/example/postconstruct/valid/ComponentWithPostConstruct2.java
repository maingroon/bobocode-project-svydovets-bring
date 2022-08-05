package com.bobocode.svydovets.beans.scanner.example.postconstruct.valid;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;

@Component
public class ComponentWithPostConstruct2 {

    @PostConstruct
    public void postConstructMethod() {
    }
}
