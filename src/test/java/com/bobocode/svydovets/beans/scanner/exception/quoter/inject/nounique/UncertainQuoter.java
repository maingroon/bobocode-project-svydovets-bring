package com.bobocode.svydovets.beans.scanner.exception.quoter.inject.nounique;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

@Component
public class UncertainQuoter {

    @Inject
    Quoter quoter;
}
