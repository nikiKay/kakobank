package com.test.kakobank.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 인증단어인증
 */
@Entity
@DynamicUpdate
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CERT_WORD_CERT")
public class CertWordCertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    @ColumnDefault("'N'")
    @Column(name = "success_yn", length = 1, nullable = false)
    String successYn;
    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    LocalDateTime createdDate;
    @CreationTimestamp
    @Column(name = "expired_date", nullable = false)
    LocalDateTime expiredDate;
    @ColumnDefault("0")
    @Column(name = "failed_count", length = 1, nullable = false)
    int failedCount;
    @Column(name = "cert_date", length = 1)
    LocalDateTime certDate;

    @ManyToOne
    @JoinColumn(name = "cert_word_id")
    @JsonIgnore
    CertWordListEntity certWordListEntity;

    @ManyToOne
    @JoinColumn(name = "transfer_cert_id")
    @JsonIgnore
    TransferCertEntity transferCertEntity;
}
