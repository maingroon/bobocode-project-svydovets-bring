package com.bobocode.svydovets.beans.scanner.quoter.bookshelfs;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

@Configuration()
public class FantasyBookshelf implements Bookshelf {

    @Bean("discworld")
    public DiscworldQuoter getDiscworldQuoter() {
        return new DiscworldQuoter();
    }

    @Bean
    public HarryPotterQuoter getHarryPotterQuoter(DiscworldQuoter discworldQuoter) {
        return new HarryPotterQuoter();
    }

}
