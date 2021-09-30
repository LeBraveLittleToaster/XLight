package de.pschiessle.xlight.xserver;

import de.pschiessle.xlight.xserver.controller.mqtt.MqttService;
import de.pschiessle.xlight.xserver.controller.mqtt.MqttTopicMessage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MqttTest {


  @Autowired
  MqttService mqttService;

  @Test
  public void sendTest() throws MqttException {
    for (int i = 0; i < 1000; i++) {
      mqttService.sendStr(new MqttTopicMessage("test", "Hello World" + i));
    }
  }

}
