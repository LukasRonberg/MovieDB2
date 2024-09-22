package org.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrewListDTO {

    @JsonProperty("cast")
    private List<CrewDTO> cast;

    @JsonProperty("crew")
    private List<CrewDTO> crew;
}
