package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.validator.MtsLightValidator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MtsLightService {

  final MtsLightRepository mtsLightRepository;

  public MtsLightService(
      MtsLightRepository mtsLightRepository) {
    this.mtsLightRepository = mtsLightRepository;
  }

  public Mono<MtsLight> getLightByLightId(String lightId){
    return mtsLightRepository.findMtsLightByLightId(lightId);
  }


  public Flux<MtsLight> getLights() {
    return mtsLightRepository.findAll();
  }

  public Mono<MtsLight> createLight(String name, String location, String mac,
      List<Long> supportedModes) {

    try {
      MtsLight mtsLightValidated = MtsLightValidator.validateAddLightObj(name, location, mac,
          supportedModes);
      mtsLightValidated.setLightId(IdGenerator.generateUUID());
      return mtsLightRepository.findMtsLightByMac(mtsLightValidated.getMac())
          .hasElement()
          .flatMap(
              hasElement -> hasElement ? Mono.error(
                  new DuplicateKeyException("Mac adress already present"))
                  : mtsLightRepository.save(mtsLightValidated)
          );
    } catch (NoSufficientDataException e) {
      return Mono.error(e);
    }
  }


  public Mono<MtsLight> setLightIsOn(String lightId, boolean isOn) {

    Mono<MtsLight> mtsLight = mtsLightRepository.findMtsLightByLightId(lightId);
    return mtsLight
        .flatMap(light -> {
          light.setOn(isOn);
          return mtsLightRepository.save(light);
        });
  }

  public Mono<Void> deleteLightByLightId(String lightId){
    return mtsLightRepository.deleteByLightId(lightId);
  }

  public void setLightPicture(long lightId, byte[] bytes) {

  }
}
