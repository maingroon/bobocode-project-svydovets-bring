package com.bobocode.svydovets.beans.scanner.example.postconstruct.args;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;

@Component
public class ComponentWithPostConstructWithArgs {

    @PostConstruct
    public void postConstructWithArgsMethod(String arg1, String arg2) {
    }
}
