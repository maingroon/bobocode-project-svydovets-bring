package com.bobocode.svydovets.beans.scanner.example.postconstruct.stat;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.PostConstruct;

@Configuration
public class ConfigurationWithStaticPostConstruct {
    @PostConstruct
    public static void staticPostConstruct() {
    }
}
