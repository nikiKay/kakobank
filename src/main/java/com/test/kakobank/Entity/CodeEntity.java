package com.test.kakobank.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.One;
import org.hibernate.annotations.CreationTimestamp;

/**
 * 코드 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CODE")
public class CodeEntity {
    @Column(name = "code", length = 50)
    @Id
    String code;

    @Column(name = "code_name", length = 50)
    String codeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code")
    @JsonIgnore
    CodeEntity codeEntity;

    @OneToMany(mappedBy = "codeEntity", cascade = CascadeType.ALL)
    @Builder.Default
    List<CodeEntity> codeEntities = new ArrayList<>();
}
