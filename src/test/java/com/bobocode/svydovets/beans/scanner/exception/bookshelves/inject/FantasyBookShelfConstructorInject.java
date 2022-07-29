package com.bobocode.svydovets.beans.scanner.exception.bookshelves.inject;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

@Configuration
public class FantasyBookShelfConstructorInject {
    @Bean
    public DiscworldQuoter getDiscworldQuoter() {
        return new DiscworldQuoter();
    }

    @Bean("fantasy")
    public HarryPotterQuoter getHarryPotterQuoter(DiscworldQuoter discworldQuoter) {
        return new HarryPotterQuoter();
    }
}
