package pl.hubert.geometry.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CircleDto extends ShapeDto {
    private double radius;

    public CircleDto(Long id, String type, double radius) {
        super(id, type);
        this.radius = radius;
    }
}
