package com.finpro.frontend.commands;

public class CommandInvoker {
    public void run(Command cmd) {
        cmd.execute();
    }
}
