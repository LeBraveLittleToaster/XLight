package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsInput;
import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.MtsModeRepository;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
public class MtsModeService {

  final MtsModeRepository mtsModeRepository;

  public MtsModeService(
      MtsModeRepository mtsModeRepository) {
    this.mtsModeRepository = mtsModeRepository;
  }

  public Mono<MtsMode> getModeByMtsModeId(String mtsModeId) {
    return mtsModeRepository.findByMtsModeId(mtsModeId);
  }

  public Flux<MtsMode> getModes() {
    return mtsModeRepository.findAll();
  }

  public Mono<MtsMode> createMode(long modeId, String name, List<MtsInput> inputs) {

    return Mono.just(MtsMode.builder()
            .modeId(modeId)
            .name(name)
            .inputs(inputs)
            .mtsModeId(IdGenerator.generateUUID())
            .changeDateUTC(Instant.now().toEpochMilli())
            .build())
        .flatMap(mtsModeValidated -> {
          mtsModeValidated.setMtsModeId(IdGenerator.generateUUID());
          mtsModeValidated.setChangeDateUTC(Instant.now().getEpochSecond());
          return Mono.just(mtsModeValidated);
        })
        .flatMap(mtsModeValidated ->
            mtsModeRepository.findByModeId(modeId)
                .hasElement()
                .flatMap(hasElement -> hasElement ?
                    Mono.error(new DuplicateKeyException("Mode ID already present"))
                    : mtsModeRepository.save(mtsModeValidated)))
        .switchIfEmpty(Mono.error(new Throwable("Not able to insert mode, cause unknown")))
        .onErrorResume(Mono::error);
  }

  public Mono<Void> deleteByMtsModeId(String mtsModeId) {
    return mtsModeRepository.deleteMtsModeByMtsModeId(mtsModeId);
  }
}
