package com.test.kakobank.exception;

import com.test.kakobank.constants.ResultMessage;
import com.test.kakobank.model.CommonResponse;
import lombok.Getter;

@Getter
public class DoubleRequestException extends RuntimeException {
    CommonResponse commonResponse;

    public DoubleRequestException() {
        super();
        commonResponse = CommonResponse.builder()
            .success(false)
            .code(ResultMessage.DOUBLE_REQUEST_FAIL.getCode())
            .message(ResultMessage.DOUBLE_REQUEST_FAIL.getCode())
            .build();
    }
}
