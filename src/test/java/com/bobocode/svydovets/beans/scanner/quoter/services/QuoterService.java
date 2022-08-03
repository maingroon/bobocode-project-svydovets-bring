package com.bobocode.svydovets.beans.scanner.quoter.services;

import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.KobzarQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.ZakharBerkut;

public class QuoterService {

    private final HarryPotterQuoter harryPotterQuoter;

    private final KobzarQuoter kobzarQuoter;

    private final ZakharBerkut zakharBerkut;

    public QuoterService(HarryPotterQuoter harryPotterQuoter, KobzarQuoter kobzarQuoter, ZakharBerkut zakharBerkut) {
        this.harryPotterQuoter = harryPotterQuoter;
        this.kobzarQuoter = kobzarQuoter;
        this.zakharBerkut = zakharBerkut;
    }
}
