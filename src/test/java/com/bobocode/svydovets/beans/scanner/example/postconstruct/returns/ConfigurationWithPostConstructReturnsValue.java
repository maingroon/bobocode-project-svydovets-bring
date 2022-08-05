package com.bobocode.svydovets.beans.scanner.example.postconstruct.returns;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.PostConstruct;

@Configuration
public class ConfigurationWithPostConstructReturnsValue {

    @PostConstruct
    public String postConstructMethodWithReturnType() {
        return "Some value";
    }

}
