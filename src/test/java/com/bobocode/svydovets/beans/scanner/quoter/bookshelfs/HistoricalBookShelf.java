package com.bobocode.svydovets.beans.scanner.quoter.bookshelfs;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.scanner.quoter.books.ZakharBerkut;

@Configuration
public class HistoricalBookShelf {

    @Bean("zakhar_b")
    public ZakharBerkut zakharBerkut(){
        return new ZakharBerkut();
    }

}
