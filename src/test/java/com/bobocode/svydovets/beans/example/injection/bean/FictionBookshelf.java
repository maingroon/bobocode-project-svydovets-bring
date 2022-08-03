package com.bobocode.svydovets.beans.example.injection.bean;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;

import java.io.PrintStream;

@Configuration
public class FictionBookshelf {

    @Bean("alice")
    public AliceInWonderlandQuoter getAliceInWonderlandQuoter(PrintStream printStream) {
        return new AliceInWonderlandQuoter(printStream);
    }

    @Bean("out")
    public PrintStream getPrintStream() {
        return System.out;
    }
}
