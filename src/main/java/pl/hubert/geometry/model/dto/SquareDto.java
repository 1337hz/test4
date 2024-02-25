package pl.hubert.geometry.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquareDto extends ShapeDto {
    private double side;

    public SquareDto(long id, String type, double side) {
        super(id, type);
        this.side = side;
    }
}
