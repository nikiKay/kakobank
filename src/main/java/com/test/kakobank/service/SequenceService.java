package com.test.kakobank.service;

import com.google.common.base.Strings;
import com.test.kakobank.Entity.SequenceEntity;
import com.test.kakobank.repository.SequenceRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {
  public enum SequenceEnum {
    STEP_SEQ("STEP_TABLE_SEQ", 5),
    ;

    String name;
    int digit;


    SequenceEnum(String s, int i) {
      this.name = s;
      this.digit = i;
    }

    public String getName() {
      return name;
    }

    public int getDigit() {
      return digit;
    }
  }

  @Autowired
  SequenceRepository sequenceRepository;

  public String getSequenceForToday(SequenceEnum sequenceEnum) {
    return this.getSequenceForToday(sequenceEnum.name, sequenceEnum.digit);
  }

  public String getSequenceForToday(String sequenceName, int digit) {
    String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    String sequence = this.getSequence(sequenceName + today, digit);
    return today + sequence;
  }

  public String getSequenceForToday(String sequenceName) {
    String todaySequence = sequenceName + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    String sequence = this.getSequence(sequenceName + todaySequence, 0);
    return todaySequence + sequence;
  }

  public String getSequence(String sequenceName, int digit) {
    int nextVal = this.getSequence(sequenceName);
    return Strings.padStart(String.valueOf(nextVal), digit, '0');
  }

  public int getSequence(String sequenceName) {
    SequenceEntity sequenceEntity = sequenceRepository.findById(sequenceName).orElseGet(() -> SequenceEntity.builder().sequenceName(sequenceName).build());
    int nextVal = Optional.ofNullable(sequenceEntity.getNextVal()).orElseGet(() -> 0) + 1;
    sequenceEntity.setNextVal(nextVal);
    sequenceRepository.save(sequenceEntity);
    return nextVal;
  }
}
