package pl.hubert.geometry.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Circle extends Shape {

    private double radius;

    public Circle(double radius) {
        super("circle");
        this.radius = radius;
    }

    public Circle(Long id, String type, double radius) {
        super(id, type);
        this.radius = radius;
    }
}
