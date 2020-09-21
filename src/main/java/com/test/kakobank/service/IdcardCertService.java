package com.test.kakobank.service;

import com.google.common.collect.ImmutableMap;
import com.test.kakobank.Entity.IdcardCertEntity;
import com.test.kakobank.repository.IdcardCertRepository;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IdcardCertService {
  public final String KEY_RESULT_CODE = "resultCode";
  public final String KEY_RESULT_MESSAGE = "resultMessage";
  public final String SUCCESS_RESULT_CODE = "00000";
  public final String UNKNOWN_RESULT_CODE = "99999";

  @Value("${server.msa-ap.internal.urls.idcard-cert-dev}")
  String IdcardCertUrl;

  @Autowired
  IdcardCertRepository idcardCertRepository;

  @Autowired
  ApiService apiService;

  public long saveIdcardCert(String idcardCertTypeCode,
      String registrationNumber,
      String driverLicenceNumber,
      String name,
      String issueDate,
      String driverLicenceCertCode) {
    return idcardCertRepository.save(IdcardCertEntity.builder()
        .idcardCertTypeCode(idcardCertTypeCode)
        .registrationNumber(registrationNumber)
        .driverLicenceNumber(driverLicenceNumber)
        .name(name)
        .issueDate(issueDate)
        .driverLicenceCertCode(driverLicenceCertCode)
        .build()
    ).getId();
  }

  public void updateIdcardCert(long IdcardCertId,
      String successYn,
      String errorCode,
      String errorMessage) throws RuntimeException{
    IdcardCertEntity idcardCertEntity = idcardCertRepository.findById(IdcardCertId).orElseThrow(RuntimeException::new);
    idcardCertEntity.setSuccessYn(successYn);
    idcardCertEntity.setSuccessYn(errorCode);
    idcardCertEntity.setSuccessYn(errorMessage);

    idcardCertRepository.save(idcardCertEntity);
  }

  public Map checkIdcardCert(String idcardCertTypeCode,
      String registrationNumber,
      String driverLicenceNumber,
      String name,
      String issueDate,
      String driverLicenceCertCode) throws RuntimeException {
    Map params = ImmutableMap
        .of("type", idcardCertTypeCode, "driver_license_number", driverLicenceNumber, "name", name);
    params.put("registration_number", registrationNumber);
    params.put("issue_date", issueDate);
    params.put("driver_licence_cert_code", driverLicenceCertCode);

    ResponseEntity<Map> responseEntity = apiService.sendByPost(IdcardCertUrl, params);
    return responseEntity.getBody();
  }
}
