package de.pschiessle.xlight.xlightserver.controller;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.controller.requests.CreateControlgroupRequest;
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

  @PostMapping(value = "/control/groups/{groupId}/mode/{modeId}/set")
  public ResponseEntity<Void> setStateForControlGroup(@PathVariable long groupId,
      @PathVariable Long modeId, @RequestBody List<MtsValue> values) {
    try {
      groupService.setModeToGroupById(groupId, modeId, values);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
