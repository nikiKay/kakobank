package com.test.kakobank.repository;

import com.test.kakobank.Entity.CertWordCertEntity;
import com.test.kakobank.Entity.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<StepEntity, String> {

}
