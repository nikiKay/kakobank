package com.test.kakobank.exception;

import com.test.kakobank.constants.ResultMessage;
import com.test.kakobank.model.CommonResponse;
import lombok.Getter;

@Getter
public class BizException extends Exception {
    CommonResponse commonResponse;

    public BizException() {
        super();
        commonResponse = CommonResponse.builder().build();
    }

    public BizException(String code, String message) {
        super(message);
        commonResponse = CommonResponse.builder().success(false).code(code).message(message).build();
    }

    public BizException(ResultMessage resultMessage) {
        super(resultMessage.getMsg());
        commonResponse = CommonResponse.builder().success(false).code(resultMessage.getCode()).message(resultMessage.getMsg()).build();
    }

    public BizException(CommonResponse commonResponse) {
        super();
        this.commonResponse = commonResponse;
    }
}
