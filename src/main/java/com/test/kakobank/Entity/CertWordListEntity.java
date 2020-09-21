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
import org.hibernate.annotations.CreationTimestamp;

/**
 * 인증단어 목록
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CERT_WORD_LIST")
public class CertWordListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "cert_word", length = 10, nullable = false)
    String certWord;

    @OneToMany(mappedBy = "certWordListEntity", cascade = CascadeType.ALL)
    @Builder.Default
    List<CertWordCertEntity> certWordCertEntities = new ArrayList<>();
}
