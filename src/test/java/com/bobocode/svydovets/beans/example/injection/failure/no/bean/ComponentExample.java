package com.bobocode.svydovets.beans.example.injection.failure.no.bean;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;

@Component
public class ComponentExample {

    @Inject
    private NotImplemented notImplemented;
}
