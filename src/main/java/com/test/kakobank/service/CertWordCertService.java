package com.test.kakobank.service;

import com.test.kakobank.Entity.CertWordCertEntity;
import com.test.kakobank.Entity.CertWordListEntity;
import com.test.kakobank.Entity.TransferCertEntity;
import com.test.kakobank.constants.ResultMessage;
import com.test.kakobank.exception.BizException;
import com.test.kakobank.exception.ControllerFailException;
import com.test.kakobank.repository.CertWordCertRepository;
import com.test.kakobank.repository.CertWordListRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertWordCertService {
  public final int LIMIT_FAIL_COUNT = 5;
  public final int LIMIT_CERT_SECOUND = 60 * 60;

  @Autowired
  CertWordCertRepository certWordCertRepository;

  @Autowired
  CertWordListRepository certWordListRepository;

  public CertWordListEntity getRandomCertWord() {
    List<CertWordListEntity> certWordListEntities = certWordListRepository.findAll();
    int randomIndex = new Random().ints(0, certWordListEntities.size()).findFirst().getAsInt();
    return certWordListEntities.get(randomIndex);
  }

  public CertWordCertEntity saveCertWordCert(CertWordListEntity certWordListEntity, TransferCertEntity transferCertEntity) {
    return certWordCertRepository.save(CertWordCertEntity.builder()
        .certWordListEntity(certWordListEntity)
        .transferCertEntity(transferCertEntity)
        .expiredDate(LocalDateTime.now().plusSeconds(LIMIT_CERT_SECOUND))
        .build()
    );
  }

  public void checkWord(long certWordCertId, String certWord) throws BizException {
    CertWordCertEntity certWordCertEntity = certWordCertRepository.findById(certWordCertId).orElseThrow(RuntimeException::new);
    if (certWordCertEntity.getExpiredDate().isAfter(LocalDateTime.now()))
      throw new BizException(ResultMessage.EXPIRED_CERT_WORD);

    if (certWordCertEntity.getFailedCount() >= LIMIT_FAIL_COUNT)
      throw new BizException(ResultMessage.OVER_FAIL_COUNT_CERT_WORD);

    if (!certWord.equals(certWordCertEntity.getCertWordListEntity().getCertWord())) {
      certWordCertEntity.setFailedCount(certWordCertEntity.getFailedCount() + 1);
      certWordCertRepository.save(certWordCertEntity);
      throw new BizException(ResultMessage.OVER_FAIL_COUNT_CERT_WORD);
    }

    certWordCertEntity.setSuccessYn("Y");
    certWordCertEntity.setCertDate(LocalDateTime.now());
    certWordCertRepository.save(certWordCertEntity);
  }
}
