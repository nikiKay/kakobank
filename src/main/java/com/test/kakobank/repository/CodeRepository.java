package com.test.kakobank.repository;

import com.test.kakobank.Entity.CertWordCertEntity;
import com.test.kakobank.Entity.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<CodeEntity, String> {

}
