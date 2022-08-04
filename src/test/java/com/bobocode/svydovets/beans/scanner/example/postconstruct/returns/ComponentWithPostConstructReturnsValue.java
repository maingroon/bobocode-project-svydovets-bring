package com.bobocode.svydovets.beans.scanner.example.postconstruct.returns;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;

@Component
public class ComponentWithPostConstructReturnsValue {

    @PostConstruct
    public String postConstructMethodWithReturnType() {
        return "Some value";
    }
}