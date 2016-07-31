package com.github.marschall.springjdbccall.spi;

import java.util.Locale;

final class UpperCase implements NamingStrategy {

  static final NamingStrategy INSTANCE = new UpperCase();

  @Override
  public String translateToDatabase(String javaName) {
    return javaName.toUpperCase(Locale.US);
  }

}
