package pl.hubert.geometry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.hubert.geometry.exception.TypeNotRecognizedException;
import pl.hubert.geometry.model.Shape;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.dto.ShapeDto;
import pl.hubert.geometry.repository.ShapeRepository;
import pl.hubert.geometry.specification.ShapeSpecificationProvider;
import pl.hubert.geometry.strategy.ShapeMappingStrategy;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class ShapeService {

    private final ShapeRepository shapeRepository;
    private final Map<String, ShapeMappingStrategy<? extends ShapeDto>> shapeMappingStrategyMap;

    public ShapeDto createShape(CreateShapeCommand command) {
        if (!shapeMappingStrategyMap.containsKey(command.getType())) {
            throw new TypeNotRecognizedException(command.getType());
        }
        ShapeMappingStrategy<? extends ShapeDto> createStrategy = shapeMappingStrategyMap.get(command.getType());

        Shape shape = shapeRepository.save(createStrategy.fromCommand(command));
        if (!shapeMappingStrategyMap.containsKey(shape.getType())) {
            throw new TypeNotRecognizedException(shape.getType());
        }

        return shapeMappingStrategyMap.get(shape.getType()).toDto(shape);
    }

    public Page<? extends ShapeDto> findShapesByProperty(Pageable pageable, Map<String, String> requestParams) {
        Specification<Shape> spec = ShapeSpecificationProvider.get(requestParams);
        return shapeRepository.findAll(spec, pageable).map(shape -> {
            if (!shapeMappingStrategyMap.containsKey(shape.getType())) {
                throw new TypeNotRecognizedException(shape.getType());
            }
            return shapeMappingStrategyMap.get(shape.getType()).toDto(shape);
        });
    }
}

