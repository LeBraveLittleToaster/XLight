package de.pschiessle.xlight.xlightserver.controller.mqtt;

import com.google.gson.Gson;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;

public class MqttMessageHelper {

  private static final Gson gson = new Gson();

  public static MqttTopicMessage buildLightStateMsg(String macAddress, MtsLightState lightState) {
    return new MqttTopicMessage("light/" + macAddress, gson.toJson(lightState));
  }
}
