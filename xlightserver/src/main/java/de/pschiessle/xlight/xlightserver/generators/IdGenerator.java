package de.pschiessle.xlight.xlightserver.generators;

import java.util.UUID;

public class IdGenerator {
  public static String generateUUID(){
    return UUID.randomUUID().toString();
  }
}
