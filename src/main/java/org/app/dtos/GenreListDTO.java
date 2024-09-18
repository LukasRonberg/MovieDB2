package org.app.dtos;

import lombok.Data;
import java.util.List;

@Data
public class GenreListDTO {
    private List<GenreDTO> genres;
}
