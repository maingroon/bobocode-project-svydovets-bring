package com.bobocode.svydovets.beans.scanner.quoter.books;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

@Component("harryPotterBook")
public class HarryPotter {
    @Inject("hp")
    private Quoter quoter;

    public Quoter getQuoter() {
        return quoter;
    }
}
