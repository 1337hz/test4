package pl.hubert.geometry.strategy;

import pl.hubert.geometry.common.exception.PropertyRequiredException;
import pl.hubert.geometry.model.Shape;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.dto.ShapeDto;

public interface ShapeMappingStrategy<U extends ShapeDto> {
    Shape fromCommand(CreateShapeCommand command) throws PropertyRequiredException;

    U toDto(Shape shape);
}
