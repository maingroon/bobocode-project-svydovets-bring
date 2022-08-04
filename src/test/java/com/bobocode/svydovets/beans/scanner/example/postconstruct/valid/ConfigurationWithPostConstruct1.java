package com.bobocode.svydovets.beans.scanner.example.postconstruct.valid;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.PostConstruct;

@Configuration("config1")
public class ConfigurationWithPostConstruct1 {

    @PostConstruct
    public void postConstructMethod() {
    }

    public void notPostConstructMethod() {

    }
}
