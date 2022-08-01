package com.bobocode.svydovets.beans.scanner.exception.quoter.inject.nounique;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

@Component
public class DiscworldQuoter implements Quoter {
    @Override
    public void quote() {
        System.out.println("A good bookshop is just a genteel Black Hole that knows how to read.");
    }
}
