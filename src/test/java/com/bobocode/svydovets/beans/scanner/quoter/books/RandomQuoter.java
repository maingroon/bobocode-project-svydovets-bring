package com.bobocode.svydovets.beans.scanner.quoter.books;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

@Component("random")
public class RandomQuoter implements Quoter {

    @Inject
    HarryPotterQuoter harryPotterQuoter;

    @Inject
    DiscworldQuoter discworldQuoter;

    @Override
    public void quote() {
        if (Math.random() < 0.5) {
            discworldQuoter.quote();
        } else {
            harryPotterQuoter.quote();
        }
    }
}
