package com.bobocode.svydovets.beans.scanner.example.postconstruct.args;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.PostConstruct;

@Configuration
public class ConfigurationWithPostConstructWithArgs {

    @PostConstruct
    public void postConstructWithArgsMethod(String arg1, String arg2) {
    }

}
