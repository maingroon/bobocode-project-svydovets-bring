package com.bobocode.svydovets.beans.scanner.exception.bookshelves.incorrecttype;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;

@Configuration
public class FantasyBookShelfVoidReturnType {
    @Bean
    public DiscworldQuoter getDiscworldQuoter() {
        return new DiscworldQuoter();
    }

    @Bean("fantasy")
    public HarryPotterQuoter getHarryPotterQuoter(DiscworldQuoter discworldQuoter) {
        return new HarryPotterQuoter();
    }

    @Bean
    public void getSomeVoidExecution(){
        var testVar = "test void method";
        System.out.println(testVar);
    }
}
