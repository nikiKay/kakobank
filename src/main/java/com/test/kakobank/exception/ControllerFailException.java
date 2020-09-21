package com.test.kakobank.exception;

import com.test.kakobank.constants.ResultMessage;
import com.test.kakobank.model.CommonResponse;
import lombok.Getter;

@Getter
public class ControllerFailException extends RuntimeException {
    CommonResponse commonResponse;

    public ControllerFailException() {
        super();
        commonResponse = CommonResponse.builder().build();
    }

    public ControllerFailException(String code, String message) {
        super(message);
        commonResponse = CommonResponse.builder().success(false).code(code).message(message).build();
    }

    public ControllerFailException(ResultMessage resultMessage) {
        super(resultMessage.getMsg());
        commonResponse = CommonResponse.builder().success(false).code(resultMessage.getCode()).message(resultMessage.getMsg()).build();
    }

    public ControllerFailException(CommonResponse commonResponse) {
        super();
        this.commonResponse = commonResponse;
    }
}
