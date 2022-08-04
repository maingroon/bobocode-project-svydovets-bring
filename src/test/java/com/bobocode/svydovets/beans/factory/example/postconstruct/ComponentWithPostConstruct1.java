package com.bobocode.svydovets.beans.factory.example.postconstruct;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;
import lombok.Getter;

@Component("bean1")
@Getter
public class ComponentWithPostConstruct1 {
    private int intVar;
    private String strVar;

    @PostConstruct
    public void init() {
        intVar = 10;
        strVar = "test";
    }
}
