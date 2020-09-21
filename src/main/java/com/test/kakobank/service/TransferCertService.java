package com.test.kakobank.service;

import com.google.common.collect.ImmutableMap;
import com.test.kakobank.Entity.TransferCertEntity;
import com.test.kakobank.exception.BizException;
import com.test.kakobank.repository.TransferCertRepository;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TransferCertService {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  public final String KEY_RESULT_CODE = "resCode";
  public final String KEY_RESULT_MESSAGE = "resMsg";
  public final String SUCCESS_RESULT_CODE = "00000";
  public final String UNKNOWN_RESULT_CODE = "99999";
  public final String CHECK_ACCOUNT_TIMEOUT_RESULT_CODE = "80000";
  public final String TRANSFER_TIMEOUT_RESULT_CODE = "70000";

  /* 기본발송은행 - 여기서는 카카오뱅크에서 출금 후 발송 하기에 기본 카카오뱅크 은행코드('000001') */
  public final String DEFAULT_SENDER_BANK_CODE = "000001";
  /* 기본발송계좌 - 여기서는 카카오뱅크에서 출금 후 발송 하기에 기본 카카오뱅크 계좌인증 전용 계좌 */
  public final String DEFAULT_SENDER_ACCOUNT_NUMBER = "1231233333";
  /* 인증을 위한 송금 금액 */
  public final int TRANSFER_CERT_PRICE = 1;

  @Value("${server.msa-ap.external.urls.account-dev-baseurl}")
  String baseUrl;
  @Value("${server.msa-ap.external.urls.account-check-uri}")
  String accountCheckuri;
  @Value("${server.msa-ap.external.urls.account-transfer-uri}")
  String accountTransferUri;


  @Value("${server.msa-ap.internal.urls.reception-withdraw-dev}")
  String receptionWithdrawDevUrl;
  @Value("${server.msa-ap.internal.urls.reception-cancle-withdraw-dev}")
  String receptionCancleWithdrawDevUrl;

  @Autowired
  TransferCertRepository transferCertRepository;

  @Autowired
  ApiService apiService;

  public TransferCertEntity saveTransferCert(String birthDay,
      String depositorName,
      String bankCode,
      String accountNumber) {
    return transferCertRepository.save(TransferCertEntity.builder()
        .birthDay(birthDay)
        .depositorName(depositorName)
        .bankCode(bankCode)
        .accountNumber(accountNumber)
        .errorCode(CHECK_ACCOUNT_TIMEOUT_RESULT_CODE)
        .errorMessage("check account timeout")
        .successYn("N")
        .build()
    );
  }

  public void updateAccountTransfer(TransferCertEntity transferCertEntity) throws RuntimeException {
    transferCertEntity.setSenderAccountNumber(Optional.ofNullable(transferCertEntity.getSenderAccountNumber())
        .orElseGet(() ->DEFAULT_SENDER_ACCOUNT_NUMBER));
    transferCertEntity.setSenderBankCode(Optional.ofNullable(transferCertEntity.getSenderBankCode())
        .orElseGet(() ->DEFAULT_SENDER_BANK_CODE));
    transferCertEntity.setSendPrice(Optional.ofNullable(transferCertEntity.getSendPrice())
        .orElseGet(() -> Long.valueOf(TRANSFER_CERT_PRICE)));
    transferCertEntity.setErrorCode(TRANSFER_TIMEOUT_RESULT_CODE);
    transferCertEntity.setErrorMessage("account transfer timeout");
    transferCertRepository.save(transferCertEntity);
  }
  public void updateAccountTransfer(long transferCertId, String senderName, String senderBankCode, String senderAccountNumber, int sendPrice) throws RuntimeException {
    TransferCertEntity transferCertEntity = transferCertRepository.findById(transferCertId)
        .orElseThrow(RuntimeException::new);
    transferCertEntity.setSenderName(senderName);
    transferCertEntity.setSenderBankCode(senderBankCode);
    transferCertEntity.setSenderAccountNumber(senderAccountNumber);
    transferCertEntity.setSendPrice(sendPrice);
    transferCertEntity.setErrorCode(TRANSFER_TIMEOUT_RESULT_CODE);
    transferCertEntity.setErrorMessage("account transfer timeout");
    transferCertRepository.save(transferCertEntity);
  }

  public void updateResult(long transferCertId,
      String errorCode,
      @Nullable String errorMessage) throws RuntimeException {
    TransferCertEntity transferCertEntity = transferCertRepository.findById(transferCertId)
        .orElseThrow(RuntimeException::new);
    transferCertEntity.setSuccessYn(SUCCESS_RESULT_CODE.equals(errorCode) ? "Y" : "N");
    transferCertEntity.setErrorCode(errorCode);
    transferCertEntity.setErrorMessage(errorMessage);
    transferCertRepository.save(transferCertEntity);
  }

  public Map checkAccount(String birthDay,
      String depositorName,
      String bankCode,
      String accountNumber) throws BizException {
    Map params = ImmutableMap
        .of("birth_day", birthDay, "depositor_name", depositorName, "bank_code", bankCode, "account_number",
            accountNumber);

    Map resultMap = null;
    try {
      Flux<String> responseFlux = (Flux<String>) apiService.sendByPostWebClient(baseUrl, accountCheckuri, params);
      String returnData = responseFlux
          .doOnError(e -> {
            logger.error(e.getMessage(), e);
          })
          .toStream()
          .findFirst()
          .orElseThrow(BizException::new);

      resultMap = new JSONObject(Optional.ofNullable(returnData).orElseGet(String::new)).toMap();

      String resultCode = (String) Optional.ofNullable(resultMap.get(KEY_RESULT_CODE)).orElseThrow(BizException::new);
      if (!SUCCESS_RESULT_CODE.equals(resultCode))
        throw new BizException((String) resultMap.get(KEY_RESULT_CODE), (String) resultMap.get(KEY_RESULT_MESSAGE));
    } catch (BizException e) {
      throw e;
    }
    return resultMap;
  }

  public Map transferAccount(String bankCode, String accountNumber, String sender, String withdrawKey) throws BizException {
    return this.transferAccount(bankCode, accountNumber, sender, DEFAULT_SENDER_BANK_CODE, DEFAULT_SENDER_ACCOUNT_NUMBER, TRANSFER_CERT_PRICE, withdrawKey);
  }

  public Map transferAccount(String bankCode,
      String accountNumber,
      String sender,
      String senderBankCode,
      String senderAccountNumber,
      long sendPrice,
      String withdrawKey) throws BizException {
    Map params = ImmutableMap
        .of("bank_code", bankCode, "account_number", accountNumber, "sender_bank_code", senderBankCode,
            "sender_account_number", senderAccountNumber,
            "send_price", sendPrice);
    Map resultMap = null;
    try {
      Flux<String> responseFlux = (Flux<String>) apiService.sendByPostWebClient(baseUrl, accountTransferUri, params);
      String returnData = responseFlux
          .doOnError(e -> {
            logger.error(e.getMessage(), e);
            this.cancleRecptionWithdraw(senderAccountNumber, bankCode, accountNumber, sendPrice, withdrawKey);
          })
          .toStream()
          .findFirst()
          .orElseThrow(BizException::new);

      resultMap = new JSONObject(Optional.ofNullable(returnData).orElseGet(String::new)).toMap();

      String resultCode = (String) Optional.ofNullable(resultMap.get(KEY_RESULT_CODE)).orElseThrow(BizException::new);
      if (!SUCCESS_RESULT_CODE.equals(resultCode))
        throw new BizException((String) resultMap.get(KEY_RESULT_CODE), (String) resultMap.get(KEY_RESULT_MESSAGE));
    } catch (BizException e) {
      this.cancleRecptionWithdraw(senderAccountNumber, bankCode, accountNumber, sendPrice, withdrawKey);
      throw e;
    }
    return resultMap;
  }

  public Map recptionWithdraw(String sendBankCode, String sendAccountNumber) throws RuntimeException {
    return recptionWithdraw(DEFAULT_SENDER_ACCOUNT_NUMBER, sendBankCode, sendAccountNumber, TRANSFER_CERT_PRICE);
  }

  public Map recptionWithdraw(String sendBankCode, String sendAccountNumber, long sendPrice) throws RuntimeException {
    return recptionWithdraw(DEFAULT_SENDER_ACCOUNT_NUMBER, sendBankCode, sendAccountNumber, sendPrice);
  }

  public Map recptionWithdraw(String accountNumber,
      String sendBankCode,
      String sendAccountNumber,
      long sendPrice) throws RuntimeException {
    Map params = ImmutableMap
        .of("account_number", accountNumber, "send_bank_code", sendBankCode, "send_account_number", sendAccountNumber,
            "send_price", sendPrice);

    ResponseEntity<Map> responseEntity = apiService.sendByPost(receptionWithdrawDevUrl, params);
    return responseEntity.getBody();
  }

  public Map cancleRecptionWithdraw(String accountNumber,
      String sendBankCode,
      String sendAccountNumber,
      long sendPrice,
      String withdrawKey) throws RuntimeException {
    Map params = ImmutableMap
        .of("account_number", accountNumber, "send_bank_code", sendBankCode, "send_account_number", sendAccountNumber,
            "send_price", sendPrice,
            "withdraw_key", withdrawKey);

    ResponseEntity<Map> responseEntity = apiService.sendByPost(receptionCancleWithdrawDevUrl, params);
    return responseEntity.getBody();
  }
}
