package com.test.kakobank.constants;

import java.util.Arrays;
import org.springframework.http.HttpStatus;

public enum StepEnum {
    IDCARD_CERT(1, "/create/account/certificate/idcard"),
    TRANSFER_CERT(2, "/create/account/certificate/transfer"),
    CERT_WORD_CERT(3, "/create/account/certificate/cert-word"),
    ;

    private int step;
    private String uri;
    StepEnum(int i, String s) {
        this.step = i;
        this.uri = s;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public static StepEnum valueOf(int step) {
        return Arrays.stream(values()).filter(v -> v.step == step).findFirst().orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + step + "]"));
    }
}
