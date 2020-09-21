package com.test.kakobank.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SequenceServiceTest {

	@Autowired
	SequenceService sequenceService;

	@Test
	void fistSeq() {
		String strOut = sequenceService.getSequenceForToday("STEP_TB_ID", 5);
		Assertions.assertThat(strOut).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "00001");
	}

}
