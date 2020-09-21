package com.test.kakobank.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * 일련번호 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SEQUENCE")
public class SequenceEntity {

  @Column(name = "sequence_name", length = 100)
  @Id
  String sequenceName;

  @ColumnDefault("0")
  @Column(name = "next_val", nullable = false)  /* 다음키 */
  int nextVal;

}
