package de.pschiessle.xlight.xserver.controller;

import de.pschiessle.xlight.xserver.components.MtsControlGroup;
import de.pschiessle.xlight.xserver.components.MtsValue;
import de.pschiessle.xlight.xserver.controller.requests.CreateControlgroupRequest;
import de.pschiessle.xlight.xserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xserver.services.MtsControlGroupService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(originPatterns = "*")
@Controller
@RestController
@RequestMapping("/api")
public class MtsControlGroupController {

  final MtsControlGroupService groupService;

  public MtsControlGroupController(
      MtsControlGroupService groupService) {
    this.groupService = groupService;
  }

  @GetMapping(value = "/control/groups")
  public ResponseEntity<List<MtsControlGroup>> getAllControlGroups() {
    return new ResponseEntity<>(groupService.getAllControlGroups(), HttpStatus.OK);
  }

  @PutMapping(value = "/control/groups/create", produces = "application/json; charset=utf-8")
  public ResponseEntity<MtsControlGroup> createControlGroup(
      @RequestBody CreateControlgroupRequest request) {
    try {
      MtsControlGroup _controlGroup = groupService.createControlGroup(request.name,
          request.mtsLightIds);
      return new ResponseEntity<>(_controlGroup, HttpStatus.CREATED);
    } catch (Exception | NoSufficientDataException e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/control/groups/{{groupId}}/mode/{modeId}/set")
  public ResponseEntity<Void> setStateForControlGroup(@PathVariable long groupId,
      @PathVariable Long modeId, @RequestBody List<MtsValue> values){
    try {
      groupService.setModeToGroupById(groupId, modeId, values);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception | IndexMissmatchException e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
