package de.pschiessle.xlight.xlightserver.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Generated;
import lombok.Getter;
import org.springframework.data.annotation.Id;


@Getter
public abstract class BaseEntity {

  @Id
  @Generated
  @JsonProperty(access = Access.WRITE_ONLY)
  private String _id;
}
