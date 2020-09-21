# kakobank
카카오뱅키 202009 과제 제출

뱅킹계좌 개설 API 생성

규격 : JSON

공통 response
 - success : boolean
 - code : string(5)
 - message : string
 - body : object

{
  "success" : "true",
  "code" : "0000",
  "message" : "성공 했습니다.",
  "body" : {
    id : 1
  }
}

API
 - 신분증인증
   - path : /create/account/certificate/idcard
   - input
     * @param idcardCertTypeCode    - 신분증인증유형코드 (001:운전면허증 / 002:주민등록증)
     * @param registrationNumber    - 주민등록번호
     * @param driverLicenceNumber   - 운전면허증번호
     * @param name                  - 이름
     * @param issueDate             - 발급일자
     * @param driverLicenceCertCode - 운전면허증인증코드
    - output
      - 공통 response
 - 이체인증
   - path : /create/account/certificate/transfer
   - input
     * @param bankCode - 이체할 은행 코드
     * @param accountNumber - 이체할 계좌 번호
    - output
      - 공통 response
 - 인증단어인증
   - path : /create/account/certificate/cert-word
   - input
      * @param certWord - 입력한 인증 단어
   - output
     - 공통 response
