package de.pschiessle.xlight.xlightserver.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@Node("XControlGroup")
public class XControlGroupEntity {

    @Id
    private final String id;

    @Property("name")
    private final String name;
    @Property("description")
    private final String description;
    @Relationship(type="DIRECTED", direction = INCOMING)
    private Set<XDeviceEntity> assignedDevices = new HashSet<>();

    public XControlGroupEntity(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<XDeviceEntity> getAssignedDevices() {
        return assignedDevices;
    }

    public void setAssignedDevices(Set<XDeviceEntity> assignedDevices) {
        this.assignedDevices = assignedDevices;
    }
}
