package com.test.kakobank.service;

import com.google.common.base.Strings;
import com.test.kakobank.Entity.SequenceEntity;
import com.test.kakobank.Entity.StepEntity;
import com.test.kakobank.repository.SequenceRepository;
import com.test.kakobank.repository.StepRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StepService {

  @Autowired
  StepRepository stepRepository;

  public StepEntity saveStep(StepEntity stepEntity) {
    return stepRepository.save(stepEntity);
  }

  public StepEntity findStep(String stepEntityId) {
    return stepRepository.findById(stepEntityId).orElseGet(() -> StepEntity.builder().id(stepEntityId).step(0).build());
  }

  public StepEntity updatePlusStep(StepEntity stepEntity) {
    stepEntity.setStep(stepEntity.getStep() + 1);
    return stepRepository.save(stepEntity);
  }

  public StepEntity updateStepInfo(String stepEntityId, String name, String registrationNumber) {
    return this.updateStepInfo(StepEntity.builder().id(stepEntityId).name(name).registrationNumber(registrationNumber).build());
  }

  public StepEntity updateStepInfo(StepEntity stepEntity) throws RuntimeException{
    stepRepository.findById(stepEntity.getId()).orElseThrow(RuntimeException::new);
    return stepRepository.save(stepEntity);
  }
}
