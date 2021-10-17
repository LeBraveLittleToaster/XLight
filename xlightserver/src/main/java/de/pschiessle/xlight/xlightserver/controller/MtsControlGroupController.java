package de.pschiessle.xlight.xlightserver.controller;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.controller.requests.CreateControlgroupRequest;
import de.pschiessle.xlight.xlightserver.controller.requests.SetLightModeRequest;
import de.pschiessle.xlight.xlightserver.services.MtsControlGroupService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(originPatterns = "*")
@Controller
@RestController
public class MtsControlGroupController {

  final MtsControlGroupService groupService;

  public MtsControlGroupController(
      MtsControlGroupService groupService) {
    this.groupService = groupService;
  }

  @GetMapping(value = "/control/groups")
  public Flux<MtsControlGroup> getAllControlGroups() {
    return groupService.getAllControlGroups();
  }

  @PutMapping(value = "/control/groups/create", produces = "application/json; charset=utf-8")
  public Mono<ResponseEntity<MtsControlGroup>> createControlGroup(
      @RequestBody CreateControlgroupRequest request) {
    return groupService
        .createControlGroup(request.name(), request.mtsLightIds())
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.badRequest().build());
  }

  @GetMapping(value = "/control/groups/{groupId}/add/{lightId}")
  public Mono<ResponseEntity<MtsControlGroup>> addLightIdToControlGroup(
      @PathVariable String groupId,
      @PathVariable String lightId) {
    return groupService
        .addLightIdToControlGroup(groupId, lightId)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
  }

  @PostMapping(value = "/control/groups/{groupId}/mode/{modeId}/set")
  public Mono<ResponseEntity<List<String>>> setStateForControlGroup(@PathVariable String groupId,
      @PathVariable String modeId, @RequestBody SetLightModeRequest lightModeRequest) {
    return groupService
        .setModeToGroupById(groupId, modeId, lightModeRequest.values())
        .map(ResponseEntity::ok)
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

  }
}
