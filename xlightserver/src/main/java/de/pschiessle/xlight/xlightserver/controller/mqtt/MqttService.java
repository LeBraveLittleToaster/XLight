package de.pschiessle.xlight.xlightserver.controller.mqtt;

import static java.util.logging.Level.INFO;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttService implements InitializingBean, DisposableBean {

  MqttClient client;

  @Override
  public void afterPropertiesSet() throws Exception {
    client = new MqttClient("tcp://192.168.0.103:1883", "Spring Server");
    try {
      client.connect();
    }catch (Exception e){
      log.error("Failed to connect to MQTT server", e);
    }
  }

  public void sendStr(MqttTopicMessage topicMessage) throws MqttException {
    MqttMessage mqttMessage = new MqttMessage(topicMessage.message.getBytes(StandardCharsets.UTF_8));
    if (client != null) {
      client.publish(topicMessage.topic, mqttMessage);
    }
  }

  @Override
  public void destroy() throws Exception {
    log.info("Destroying MQTT connector");
    if (client != null) {
      client.close(true);
    }
  }
}
