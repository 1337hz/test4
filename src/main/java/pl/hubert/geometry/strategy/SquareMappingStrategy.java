package pl.hubert.geometry.strategy;

import org.springframework.stereotype.Component;
import pl.hubert.geometry.exception.MappingMismatchException;
import pl.hubert.geometry.exception.PropertyRequiredException;
import pl.hubert.geometry.model.Shape;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.Square;
import pl.hubert.geometry.model.dto.SquareDto;

import java.util.List;

@Component("square")
public class SquareMappingStrategy implements ShapeMappingStrategy<SquareDto> {
    @Override
    public Shape fromCommand(CreateShapeCommand command) {
        if(!command.getProperties().containsKey("side")){
            throw new PropertyRequiredException(List.of("side"));
        }
        return Square.builder()
                .side(command.getProperties().get("side"))
                .build();
    }

    @Override
    public SquareDto toDto(Shape shape) {
        if(shape instanceof Square square){
            return new SquareDto(square.getId(), square.getType(), square.getSide());
        }
        throw new MappingMismatchException();
    }
}
