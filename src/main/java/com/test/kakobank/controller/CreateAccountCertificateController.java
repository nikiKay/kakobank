package com.test.kakobank.controller;

import com.test.kakobank.Entity.CertWordCertEntity;
import com.test.kakobank.Entity.CertWordListEntity;
import com.test.kakobank.Entity.StepEntity;
import com.test.kakobank.Entity.TransferCertEntity;
import com.test.kakobank.constants.CommonConstants;
import com.test.kakobank.constants.ResultMessage;
import com.test.kakobank.exception.BizException;
import com.test.kakobank.exception.ControllerFailException;
import com.test.kakobank.model.CommonResponse;
import com.test.kakobank.service.CertWordCertService;
import com.test.kakobank.service.IdcardCertService;
import com.test.kakobank.service.StepService;
import com.test.kakobank.service.TransferCertService;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create/account/certificate")
public class CreateAccountCertificateController {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  StepService stepService;

  @Autowired
  IdcardCertService idcardCertService;

  @Autowired
  TransferCertService transferCertService;

  @Autowired
  CertWordCertService certWordCertService;

  /**
   * 신분증 인증 API
   * @param idcardCertTypeCode    - 신분증인증유형코드 (001:운전면허증 / 002:주민등록증)
   * @param registrationNumber    - 주민등록번호
   * @param driverLicenceNumber   - 운전면허증번호
   * @param name                  - 이름
   * @param issueDate             - 발급일자
   * @param driverLicenceCertCode - 운전면허증인증코드
   * @return
   */
  @PostMapping("/idcard")
  public ResponseEntity<CommonResponse> idCard(HttpServletRequest request,
      @RequestParam String idcardCertTypeCode,
      @RequestParam String registrationNumber,
      @RequestParam(defaultValue = "", required = false) String driverLicenceNumber,
      @RequestParam String name,
      @RequestParam String issueDate,
      @RequestParam(defaultValue = "", required = false) String driverLicenceCertCode) {

    // 1. 신분증인증 이력 관리 TABLE INSERT
    long id = idcardCertService.saveIdcardCert(idcardCertTypeCode,
        registrationNumber,
        driverLicenceNumber,
        name,
        issueDate,
        driverLicenceCertCode);

    // 2. 신분증 체크
    Map result = idcardCertService.checkIdcardCert(idcardCertTypeCode,
        registrationNumber,
        driverLicenceNumber,
        name,
        issueDate,
        driverLicenceCertCode);

    String resultCode = (String) Optional.ofNullable(result.get("resultCode")).orElseGet(() -> idcardCertService.UNKNOWN_RESULT_CODE);
    String resultMessage = (String) Optional.ofNullable(result.get("resultCode")).orElseGet(String::new);

    // 3. 신분인증 이력 관리 TABLE 결과 UPDATE
    idcardCertService.updateIdcardCert(id,
        idcardCertService.SUCCESS_RESULT_CODE.equals(resultCode) ? "Y" : "N",
        resultCode,
        resultMessage);

    if (!idcardCertService.SUCCESS_RESULT_CODE.equals(resultCode))
      throw new ControllerFailException(resultCode, resultMessage);

    StepEntity stepEntity = (StepEntity)request.getAttribute(CommonConstants.STEP_ENTITY);
    stepEntity.setName(name);
    stepEntity.setRegistrationNumber(registrationNumber);
    stepService.updateStepInfo(stepEntity);
    return ResponseEntity.ok(CommonResponse.builder().code(resultCode).build());
  }

  /**
   * 이체인증 API
   * @param bankCode - 이체할 은행 코드
   * @param accountNumber - 이체할 계좌 번호
   * @return
   */
  @PostMapping("/transfer")
  public ResponseEntity<CommonResponse> transfer(HttpServletRequest request, @RequestParam String bankCode, @RequestParam String accountNumber)
      throws BizException {
    StepEntity stepEntity = (StepEntity)request.getAttribute(CommonConstants.STEP_ENTITY);
    stepEntity.setRegistrationNumber("830114118282");
    stepEntity.setName("강성안");
    String brthDay = Optional.ofNullable(stepEntity.getRegistrationNumber()).orElseThrow(RuntimeException::new).substring(0,6);
    // 1. 이체인증 이력 관리 TABLE INSERT
    TransferCertEntity transferCertEntity = transferCertService.saveTransferCert(brthDay,
        stepEntity.getName(),
        bankCode,
        accountNumber);

    // 2. 본인계좌 여부 확인
    Map returnCheckAccountMap = transferCertService.checkAccount(brthDay, stepEntity.getName(), bankCode, accountNumber);

    // 3. 인증단어 RANDOM SELECT
    CertWordListEntity certWordListEntity = certWordCertService.getRandomCertWord();

    // 4. 이체인증이력 관리 TABLE UPDATE
    transferCertEntity.setSenderName(certWordListEntity.getCertWord()); /* 인증단어를 발신자로 설정 */
    transferCertService.updateAccountTransfer(transferCertEntity);

    // 5. 카카오뱅크 계좌 출금
    Map returnRecptionWithdraw = transferCertService.recptionWithdraw(bankCode, accountNumber);

    // 6. 대외 기관 계좌 이체
    transferCertService.transferAccount(bankCode, accountNumber, certWordListEntity.getCertWord(), (String)returnRecptionWithdraw.get("withdraw_key"));

    // 7. 인증단어 등록
    CertWordCertEntity certWordCertEntity = certWordCertService.saveCertWordCert(certWordListEntity, transferCertEntity);

    stepEntity.setCertWordCertId(certWordCertEntity.getId());
    request.setAttribute(CommonConstants.STEP_ENTITY, stepEntity);

    return ResponseEntity.ok(CommonResponse.ok());
  }

  /**
   * 인증단어인증 API
   * @param certWord - 입력한 인증 단어
   * @return
   */
  @PostMapping("/cert-word")
  public ResponseEntity<CommonResponse> certWord(HttpServletRequest request, @RequestParam String certWord)
      throws BizException {
    StepEntity stepEntity = (StepEntity)request.getAttribute(CommonConstants.STEP_ENTITY);

    long certWordCertId = Optional.ofNullable(stepEntity.getCertWordCertId()).orElseThrow(() -> new BizException(ResultMessage.WORNG_PATH_CONNECTION));
    certWordCertService.checkWord(certWordCertId, certWord);

    return ResponseEntity.ok(CommonResponse.ok());
  }
}
