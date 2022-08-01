package com.bobocode.svydovets.beans.scanner.exception.quoter.inject.nosuchbean;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.scanner.exception.quoter.inject.nounique.HarryPotterQuoter;

@Component
public class UncertainQuoter {

    @Inject
    HarryPotterQuoter quoter;
}
