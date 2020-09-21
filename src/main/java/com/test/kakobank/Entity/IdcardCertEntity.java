package com.test.kakobank.Entity;

import com.test.kakobank.convert.StringCryptoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 신분증인증
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "IDCARD_CERT")
public class IdcardCertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "idcard_cert_type_code", length = 1, nullable = false)  /* 신분증인증유형코드 */
    String idcardCertTypeCode;
    @Column(name = "registration_number", length = 200, nullable = false)  /* 주민등록번호 */
    @Convert(converter = StringCryptoConverter.class)
    String registrationNumber;
    @Column(name = "driver_licence_number", length = 200, nullable = false)  /* 운전면허증번호 */
    @Convert(converter = StringCryptoConverter.class)
    String driverLicenceNumber;
    @Column(name = "name", length = 50, nullable = false)  /* 이름 */
    String name;
    @Column(name = "issue_date", length = 8)  /* 발급일자 */
    String issueDate;
    @Column(name = "driver_licence_cert_code", length = 10)  /* 운전면허증인증코드 */
    String driverLicenceCertCode;
    @ColumnDefault("'N'")
    @Column(name = "success_yn", length = 1, nullable = false)  /* 성공여부 */
    String successYn;

    @Column(name = "send_date", nullable = false)  /* 발송일자 */
    @CreationTimestamp
    LocalDateTime sendDate;

    @Column(name = "receive_date")  /* 응답일자 */
    LocalDateTime receiveDate;

    @ColumnDefault("'99999'")
    @Column(name = "error_code", length = 5)  /* 에러코드 */
    String errorCode;
    @ColumnDefault("'Timeout'")
    @Column(name = "error_message", length = 1000)  /* 에러메시지 */
    String errorMessage;
}
