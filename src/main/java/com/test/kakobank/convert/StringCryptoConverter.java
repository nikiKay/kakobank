package com.test.kakobank.convert;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class StringCryptoConverter implements AttributeConverter<String, String> {
  private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
  private static final byte[] KEY = "2020!@#!09AEFKEB".getBytes();

  @Override
  public String convertToDatabaseColumn(String s) {
    if (s == null)
      return null;

    Key key = new SecretKeySpec(KEY, "AES");
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return new String(Base64.getEncoder().encode(cipher.doFinal(s.getBytes())));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String convertToEntityAttribute(String s) {
    if (s == null)
      return null;

    Key key = new SecretKeySpec(KEY, "AES");
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
