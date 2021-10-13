package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MtsControlGroupService {

  public List<MtsControlGroup> getAllControlGroups() {
    return null;
  }

  public MtsControlGroup createControlGroup(String name, List<Long> mtsLightIds) {
    return null;
  }

  public void setModeToGroupById(long groupId, Long modeId, List<MtsValue> values) {

  }
}
