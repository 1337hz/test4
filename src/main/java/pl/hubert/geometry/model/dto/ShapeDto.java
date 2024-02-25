package pl.hubert.geometry.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShapeDto {
    private Long id;
    private String type;

    public ShapeDto(Long id, String type) {
        this.id = id;
        this.type = type;
    }
}
