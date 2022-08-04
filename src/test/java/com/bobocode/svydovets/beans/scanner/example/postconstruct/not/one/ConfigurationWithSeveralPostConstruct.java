package com.bobocode.svydovets.beans.scanner.example.postconstruct.not.one;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.PostConstruct;

@Configuration
public class ConfigurationWithSeveralPostConstruct {

    @PostConstruct
    public void postConstructMethod1() {
    }

    @PostConstruct
    public void postConstructMethod2() {
    }

}
