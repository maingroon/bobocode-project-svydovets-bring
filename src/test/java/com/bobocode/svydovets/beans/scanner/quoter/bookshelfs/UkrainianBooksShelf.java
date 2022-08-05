package com.bobocode.svydovets.beans.scanner.quoter.bookshelfs;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.Kobzar;
import com.bobocode.svydovets.beans.scanner.quoter.books.KobzarQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.ZakharBerkut;
import com.bobocode.svydovets.beans.scanner.quoter.services.QuoterService;

@Configuration
public class UkrainianBooksShelf {

    @Bean
    public Kobzar kobzar(QuoterService quoterService) {
        return new Kobzar(quoterService);
    }

    @Bean
    public KobzarQuoter kobzarQuoter(DiscworldQuoter quoter) {
        return new KobzarQuoter(quoter);
    }

    @Bean
    public QuoterService quoterService(HarryPotterQuoter harryPotterQuoter, KobzarQuoter kobzarQuoter,
                                       ZakharBerkut zakharBerkut) {
        return new QuoterService(harryPotterQuoter, kobzarQuoter, zakharBerkut);
    }
}
