package com.test.kakobank.config;

import com.test.kakobank.interceptor.StepCheckInterceptor;
import com.test.kakobank.interceptor.DoubleRequestCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new StepCheckInterceptor())
        .addPathPatterns("/**");
    registry.addInterceptor(new DoubleRequestCheckInterceptor())
        .addPathPatterns("/create/account/certificate/transfer/**");
//        .excludePathPatterns("/**");
    ;
  }
}
