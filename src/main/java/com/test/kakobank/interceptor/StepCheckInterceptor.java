package com.test.kakobank.interceptor;

import com.test.kakobank.Entity.StepEntity;
import com.test.kakobank.component.ApplicationContextProvider;
import com.test.kakobank.constants.CommonConstants;
import com.test.kakobank.constants.StepEnum;
import com.test.kakobank.exception.RunStepException;
import com.test.kakobank.service.SequenceService;
import com.test.kakobank.service.SequenceService.SequenceEnum;
import com.test.kakobank.service.StepService;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.CookieGenerator;

public class StepCheckInterceptor extends HandlerInterceptorAdapter {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  SequenceService sequenceService = (SequenceService) ApplicationContextProvider.getApplicationContext().getBean("sequenceService");
  StepService stepService = (StepService) ApplicationContextProvider.getApplicationContext().getBean("stepService");

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Cookie[] cookies = request.getCookies();
    Cookie stepCookie = null;
    int Step = 0;
    if (cookies != null) {
      stepCookie = Arrays.stream(cookies).filter(cookie -> CommonConstants.STEP_COOKIE_NAME.equals(cookie.getName())).findFirst()
          .orElseGet(() -> createStepCookie());
    } else {
      stepCookie = createStepCookie();
    }

    String stepId = stepCookie.getValue();
    StepEntity stepEntity = stepService.findStep(stepId);
    if (stepEntity.getStep() == 0) {/* 최초진입 이다 */
      stepEntity.setExpiredDate(LocalDateTime.now().plusSeconds(CommonConstants.MAX_ARRIVE_STEP_TIME));
      stepEntity.setStep(StepEnum.IDCARD_CERT.getStep());
    } else {
      if (LocalDateTime.now().isAfter(stepEntity.getExpiredDate())) {// 만료시 신규 생성
        stepCookie = createStepCookie();
        stepId = stepCookie.getValue();
        stepEntity = stepService.findStep(stepId);
        stepEntity.setExpiredDate(LocalDateTime.now().plusSeconds(CommonConstants.MAX_ARRIVE_STEP_TIME));
        stepEntity.setStep(StepEnum.IDCARD_CERT.getStep());
      }
    }

    stepService.saveStep(stepEntity);

    CookieGenerator cg = new CookieGenerator();
    cg.setCookieName(stepCookie.getName());
    cg.setCookieMaxAge(CommonConstants.MAX_ARRIVE_STEP_TIME);
    cg.setCookieHttpOnly(true);
    cg.addCookie(response, stepCookie.getValue());
    if (!request.getRequestURI().equals(StepEnum.valueOf(stepEntity.getStep()).getUri())) {
      throw new RunStepException(stepEntity.getStep());
    }

    request.setAttribute(CommonConstants.STEP_ENTITY, stepEntity);
    return super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    StepEntity stepEntity = (StepEntity)request.getAttribute(CommonConstants.STEP_ENTITY);
    if (response.getStatus() == HttpStatus.OK.value()) {
      stepService.updatePlusStep(stepEntity);
    }

    super.postHandle(request, response, handler, modelAndView);
  }

  private Cookie createStepCookie() {
    String stepId = sequenceService.getSequenceForToday(SequenceEnum.STEP_SEQ);
    return new Cookie(CommonConstants.STEP_COOKIE_NAME, stepId);
  }
}
