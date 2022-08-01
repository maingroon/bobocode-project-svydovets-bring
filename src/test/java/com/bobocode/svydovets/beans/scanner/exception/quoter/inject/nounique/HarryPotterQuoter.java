package com.bobocode.svydovets.beans.scanner.exception.quoter.inject.nounique;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

@Component("hp")
public class HarryPotterQuoter implements Quoter {

    public void quote() {
        System.out.println("We’re with you whatever happens.");
    }
}
