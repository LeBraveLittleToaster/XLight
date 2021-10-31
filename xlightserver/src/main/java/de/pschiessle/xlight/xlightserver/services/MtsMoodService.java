package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsMood;
import de.pschiessle.xlight.xlightserver.repositories.MtsMoodRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MtsMoodService {

  final MtsMoodRepository mtsMoodRepository;
  final MtsLightService mtsLightService;

  public MtsMoodService(
      MtsMoodRepository mtsMoodRepository,
      MtsLightService mtsLightService) {
    this.mtsMoodRepository = mtsMoodRepository;
    this.mtsLightService = mtsLightService;
  }


  public Mono<MtsMood> createMood(){
    //TODO
    return Mono.empty();
  }

  public Mono<List<Long>> setMood(String mtsMoodId){
    //TODO
    return Mono.empty();
  }
}
