package com.bobocode.svydovets.beans.example.injection.failure.not.unique;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;

@Component
public class ExampleWithNotUnique {
    @Inject
    private Quoter quoter;
}
