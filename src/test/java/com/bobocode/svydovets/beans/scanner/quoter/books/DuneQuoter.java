package com.bobocode.svydovets.beans.scanner.quoter.books;

import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

public class DuneQuoter implements Quoter {
    @Override
    public void quote() {
        System.out.println("It is so shocking to find out how many people do not believe that they can learn, " +
                "and how many more believe learning to be difficult.");
    }
}
