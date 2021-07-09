package de.pschiessle.xlight.xlightserver.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;


@Node("XDevice")
public class XDeviceEntity {
    @Id
    private final String id;
    @Property("name")
    private final String name;

    public XDeviceEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
