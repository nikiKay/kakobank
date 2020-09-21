package com.test.kakobank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
  boolean success = true;
  String code = "";
  String message = "";
  T body;

  public static <T> CommonResponse ok() {
    return CommonResponse.<T>builder().success(true).code("00000").build();
  }

  public static <T> CommonResponse ok(T body) {
    return CommonResponse.<T>builder().success(true).code("00000").body(body).build();
  }

  public static <T> CommonResponse fail() {
    return CommonResponse.<T>builder().success(false).build();
  }

  public static <T> CommonResponse fail(String code, String message) {
    return CommonResponse.<T>builder().success(false).code(code).message(message).build();
  }

  public static <T> CommonResponse fail(String code, String message, T body) {
    return CommonResponse.<T>builder().success(false).code(code).message(message).body(body).build();
  }
}
