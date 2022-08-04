package com.bobocode.svydovets.beans.factory.example.postconstruct;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.PostConstruct;
import lombok.Getter;

@Component
@Getter
public class ComponentWithPostConstruct2 {
    private int intVar;
    private String strVar;

    @PostConstruct
    public void init() {
        intVar = 20;
        strVar = "test2";
    }
}
