package pl.hubert.geometry.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Square extends Shape {
    private double side;
    public Square(double side){
        super("square");
        this.side = side;
    }

    public Square(Long id, String type, double side) {
        super(id, type);
        this.side = side;
    }
}
