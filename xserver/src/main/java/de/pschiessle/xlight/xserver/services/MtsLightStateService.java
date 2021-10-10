package de.pschiessle.xlight.xserver.services;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsLightState;
import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.components.MtsValue;
import de.pschiessle.xlight.xserver.controller.mqtt.MqttMessageHelper;
import de.pschiessle.xlight.xserver.controller.mqtt.MqttService;
import de.pschiessle.xlight.xserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xserver.repositories.MtsLightStateRepository;
import de.pschiessle.xlight.xserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xserver.validator.MtsLightStateValidator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MtsLightStateService {

  final MtsLightStateRepository mtsLightStateRepository;
  final MtsModeRepository mtsModeRepository;
  final MtsLightRepository mtsLightRepository;
  final MqttService mqttService;

  public MtsLightStateService(MtsLightStateRepository mtsLightStateRepository,
      MtsModeRepository mtsModeRepository,
      MtsLightRepository mtsLightRepository,
      MqttService mqttService) {
    this.mtsLightStateRepository = mtsLightStateRepository;
    this.mtsModeRepository = mtsModeRepository;
    this.mtsLightRepository = mtsLightRepository;
    this.mqttService = mqttService;
  }

  @Transactional
  public MtsLightState updateMtsLightState(long lightId, long modeId, List<MtsValue> values)
      throws IndexMissmatchException, NotFoundException {
    MtsMode mtsMode = mtsModeRepository.findMtsModeByModeId(modeId);
    if (mtsMode == null) {
      throw new NullPointerException("MtsMode not found for modeId=" + modeId);
    }
    MtsLightState state = MtsLightStateValidator.validateInsertLightState(mtsMode, values);
    MtsLight light = mtsLightRepository.findById(lightId).orElseThrow(NotFoundException::new);
    light.setState(state);
    MtsLight savedLight = this.mtsLightRepository.save(light);
    try {
      mqttService.sendStr(MqttMessageHelper.buildLightStateMsg(light.getMac(), state));
    } catch (MqttException e) {
      log.error("MQTT Service: " + e.getMessage());
    }
    return savedLight.getState();
  }

  @Transactional
  public List<MtsLightState> updateMtsLightStates(List<Long> lightIds, long modeId,
      List<MtsValue> values)
      throws IndexMissmatchException, NotFoundException {
    MtsMode mtsMode = mtsModeRepository.findMtsModeByModeId(modeId);
    if (mtsMode == null) {
      throw new NullPointerException("MtsMode not found for modeId=" + modeId);
    }
    MtsLightState state = MtsLightStateValidator.validateInsertLightState(mtsMode, values);

    List<MtsLight> lights = mtsLightRepository.findAllById(lightIds);
    lights.forEach(l -> l.setState(state));
    List<MtsLight> savedLight = this.mtsLightRepository.saveAll(lights);
    return savedLight.stream().map(MtsLight::getState).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<MtsLightState> getMtsLightStateByDbId(long stateId) {
    return Optional.of(mtsLightStateRepository.findById(stateId));
  }

}
