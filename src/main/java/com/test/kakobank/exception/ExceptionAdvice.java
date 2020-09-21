package com.test.kakobank.exception;

import com.google.common.collect.ImmutableMap;
import com.test.kakobank.constants.StepEnum;
import com.test.kakobank.model.CommonResponse;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler(RunStepException.class)
  @ResponseStatus(HttpStatus.OK)
  private CommonResponse runStepException(HttpServletRequest request, RunStepException e) {
    return CommonResponse.builder().body(ImmutableMap.of("step", e.getStep(), "uri", StepEnum.valueOf(e.getStep()).getUri())).build();
  }

  @ExceptionHandler(DoubleRequestException.class)
  @ResponseStatus(HttpStatus.OK)
  private CommonResponse doubleRequestException(HttpServletRequest request, DoubleRequestException e) {
    return e.getCommonResponse();
  }

  @ExceptionHandler(ControllerFailException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  private CommonResponse controllerFailException(HttpServletRequest request, ControllerFailException e) {
    return e.getCommonResponse();
  }

  @ExceptionHandler(BizException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  private CommonResponse bizException(HttpServletRequest request, BizException e) {
    return Optional.ofNullable(e.getCommonResponse()).orElseGet(() -> CommonResponse.fail());
  }
}
