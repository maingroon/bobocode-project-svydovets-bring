package com.bobocode.svydovets.beans.scanner.example.postconstruct.valid;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;

@Component("bean1")
public class ComponentWithPostConstruct1 {

    @PostConstruct
    public void postConstructMethod() {
    }

}
