package com.bobocode.svydovets.beans.scanner.quoter.books;

import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

public class TheNameOfTheWindQuoter implements Quoter {
    @Override
    public void quote() {
        System.out.println("Bones mend. Regret stays with you forever.");
    }
}
