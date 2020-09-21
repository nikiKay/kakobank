package com.test.kakobank.exception;

import lombok.Getter;

@Getter
public class RunStepException extends RuntimeException {
    int step;
    
    public RunStepException() {
        super();
    }

    public RunStepException(int step) {
        super();
        this.step = step;
    }
}
