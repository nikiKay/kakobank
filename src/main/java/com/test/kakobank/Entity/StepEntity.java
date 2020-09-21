package com.test.kakobank.Entity;

import com.test.kakobank.convert.StringCryptoConverter;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * 단계 TABLE
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "STEP")
public class StepEntity {
    @Id
    @Column(name = "id", length = 20)
    String id;

    @Column(name = "step")  /* 단계 */
    int step;
    @CreationTimestamp
    @Column(name = "created_date")  /* 생성일자 */
    LocalDateTime createdDate;
    @Column(name = "expired_date")  /* 만료일자 */
    LocalDateTime expiredDate;
    @Column(name = "name", length = 50)  /* 이름 */
    String name;
    @Column(name = "registration_number", length = 200)  /* 주민등록번호 */
    @Convert(converter = StringCryptoConverter.class)
    String registrationNumber;
    @Column(name = "cert_word_cert_id")  /* 인증단어ID */
    long certWordCertId;
    @Column(name = "etc1", length = 100)  /* 기타1 */
    String etc1;
    @Column(name = "etc2", length = 100)  /* 기타2 */
    String etc2;
    @Column(name = "etc3", length = 100)  /* 기타3 */
    String etc3;
    @Column(name = "etc4", length = 100)  /* 기타4 */
    String etc4;
    @Column(name = "etc5", length = 100)  /* 기타5 */
    String etc5;
    @Column(name = "etc6", length = 100)  /* 기타6 */
    String etc6;
    @Column(name = "etc7", length = 100)  /* 기타7 */
    String etc7;
}
