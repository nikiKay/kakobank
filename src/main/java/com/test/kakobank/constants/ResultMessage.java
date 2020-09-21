package com.test.kakobank.constants;

public enum ResultMessage {
    SUCCESS("00000", "성공했습니다."),
    DOUBLE_REQUEST_FAIL("00001", "중복 거래 요청"),
    WRONG("20000", "경고"),
    ERROR("30000", "에러"),
    EXPIRED_CERT_WORD("30001", "인증시간이 초과 되었습니다."),
    OVER_FAIL_COUNT_CERT_WORD("30002", "실패 횟수를 초과 하였습니다."),
    MISMATCH_CERT_WORD("30003", "인증단어가 맞지 않습니다."),
    WORNG_PATH_CONNECTION("50000", "정상적이지 않은 경로로 접근 하였습니다."),
    UNKNOWN_FAIL("99999", "알 수 없는 오류가 발생 했습니다. 잠시 후 다시 시도 해 주십시오.\n지속적인 오류 발생 시 고객센터로 연락 바랍니다."),

    ;

    private String code;
    private String msg;
    ResultMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }


}
