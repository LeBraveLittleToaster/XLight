package de.pschiessle.xlight.xlightserver.controller.mqtt;

public class MqttTopicMessage {
  public final String topic;
  public final String message;

  public MqttTopicMessage(String topic, String message) {
    this.topic = topic;
    this.message = message;
  }
}
