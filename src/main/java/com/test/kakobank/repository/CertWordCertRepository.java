package com.test.kakobank.repository;

import com.test.kakobank.Entity.CertWordCertEntity;
import com.test.kakobank.Entity.TransferCertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertWordCertRepository extends JpaRepository<CertWordCertEntity, Long> {

}
