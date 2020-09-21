package com.test.kakobank.interceptor;

import com.test.kakobank.exception.DoubleRequestException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class DoubleRequestCheckInterceptor extends HandlerInterceptorAdapter {

  Logger logger = LoggerFactory.getLogger(this.getClass());
  private final static String REQUEST_LOCK_COOKIE_NAME = "request-lock-yn";
  private final int MAX_ARRIVE_LOCK_TIME = 10;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Cookie[] cookies = request.getCookies();
    Cookie lockCookie = null;
    int Step = 0;
    String milli = String.valueOf(LocalDateTime.now().plusSeconds(MAX_ARRIVE_LOCK_TIME + 1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    if (cookies != null) {
      lockCookie = Arrays.stream(cookies).filter(cookie -> REQUEST_LOCK_COOKIE_NAME.equals(cookie.getName())).findFirst()
          .orElseGet(() -> new Cookie(REQUEST_LOCK_COOKIE_NAME, milli));
    } else {
      lockCookie = new Cookie(REQUEST_LOCK_COOKIE_NAME, milli);
    }

    if (!LocalDateTime.now().isBefore(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(lockCookie.getValue())), ZoneId.systemDefault())))
      throw new DoubleRequestException();

    lockCookie.setHttpOnly(true);
    lockCookie.setMaxAge(MAX_ARRIVE_LOCK_TIME);
    lockCookie.setPath(request.getRequestURI());
    response.addCookie(lockCookie);
    return super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    super.postHandle(request, response, handler, modelAndView);
  }
}
