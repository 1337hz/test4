package pl.hubert.geometry.strategy;

import org.springframework.stereotype.Component;
import pl.hubert.geometry.common.exception.MappingMismatchException;
import pl.hubert.geometry.common.exception.PropertyRequiredException;
import pl.hubert.geometry.model.Circle;
import pl.hubert.geometry.model.Shape;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.dto.CircleDto;

import java.util.List;

@Component("circle")
public class CircleMappingStrategy implements ShapeMappingStrategy<CircleDto> {

    @Override
    public Shape fromCommand(CreateShapeCommand command) {
        if (!command.getProperties().containsKey("radius")) {
            throw new PropertyRequiredException(List.of("radius"));
        }

        return Circle.builder()
                .radius(command.getProperties().get("radius"))
                .build();
    }

    @Override
    public CircleDto toDto(Shape shape) {
        if (shape instanceof Circle circle) {
            return new CircleDto(circle.getId(), circle.getType(), circle.getRadius());
        }
        throw new MappingMismatchException();
    }
}
