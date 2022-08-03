package com.bobocode.svydovets.beans.example.injection.bean;

import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

import java.io.PrintStream;

public class AliceInWonderlandQuoter implements Quoter {

    private final PrintStream out;

    public AliceInWonderlandQuoter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void quote() {
        out.println("The best way to explain it is to do it.");
    }
}
