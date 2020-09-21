package com.test.kakobank.repository;

import com.test.kakobank.Entity.SequenceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceRepository extends JpaRepository<SequenceEntity, String> {
}
