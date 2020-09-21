package com.test.kakobank.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 이체인증 TABLE
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSFER_CERT")
public class TransferCertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name ="birth_day", length = 8, nullable = false)  /* 생년월일 */
    String birthDay;
    @Column(name ="depositor_name", length = 50, nullable = false)  /* 예금주명 */
    String depositorName;
    @Column(name ="bank_code", length = 10, nullable = false)  /* 은행코드 */
    String bankCode;
    @Column(name ="account_number", length = 50, nullable = false)  /* 계좌번호 */
    String accountNumber;
    @Column(name ="sender_name", length = 50)  /* 발송인 */
    String senderName;
    @ColumnDefault("'000001'") /* 발송은행 - 여기서는 카카오뱅크에서 출금 후 발송 하기에 기본 카카오뱅크 은행코드('000001') */
    @Column(name ="sender_bank_code", length = 10)  /* 발송은행코드 */
    String senderBankCode = "000001";
    @ColumnDefault("'1231233333'") /* 발송계좌 - 여기서는 카카오뱅크에서 출금 후 발송 하기에 기본 카카오뱅크 계좌인증 전용 계좌 */
    @Column(name ="sender_account_number", length = 50)  /* 발송계좌번호 */
    String senderAccountNumber = "1231233333";
    @ColumnDefault("1")  /* 이체인증시 기본 1원 */
    @Column(name ="send_price")  /* 발송금액 */
    long sendPrice = 1;
    @ColumnDefault("'N'")
    @Column(name ="success_yn", length = 1, nullable = false)  /* 성공여부 */
    String successYn = "N";
    @CreationTimestamp
    @Column(name ="send_date", nullable = false)  /* 발송일자 */
    LocalDateTime sendDate;
    @Column(name ="receive_date")  /* 응답일자 */
    LocalDateTime receiveDate;
    @Column(name ="error_code", length = 5)  /* 에러코드 */
    String errorCode;
    @Column(name ="error_message", length = 1000)  /* 에러메시지 */
    String errorMessage;
    @Column(name ="transfer_cert_result_key", length = 10)  /* 이체인증결과키 */
    String transferCertResultKey;

    @OneToMany(mappedBy = "transferCertEntity", cascade = CascadeType.ALL)
    @Builder.Default
    List<CertWordCertEntity> certWordCertEntities = new ArrayList<>();
}
