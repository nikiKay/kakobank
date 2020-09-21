package com.test.kakobank.repository;

import com.test.kakobank.Entity.CertWordCertEntity;
import com.test.kakobank.Entity.CertWordListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertWordListRepository extends JpaRepository<CertWordListEntity, Long> {

}
