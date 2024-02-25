package pl.hubert.geometry.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.hubert.geometry.common.exception.TypeNotRecognizedException;
import pl.hubert.geometry.model.Shape;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.dto.ShapeDto;
import pl.hubert.geometry.repository.ShapeRepository;
import pl.hubert.geometry.specification.ShapeSpecification;
import pl.hubert.geometry.strategy.ShapeMappingStrategy;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShapeService {

    private final ShapeRepository shapeRepository;
    private final Map<String, ShapeMappingStrategy<? extends ShapeDto>> shapeMappingStrategyMap;

    public Shape createShape(CreateShapeCommand command) {
        if (!shapeMappingStrategyMap.containsKey(command.getType())) {
            throw new TypeNotRecognizedException(command.getType());
        }
        ShapeMappingStrategy<? extends ShapeDto> createStrategy = shapeMappingStrategyMap.get(command.getType());

        return shapeRepository.save(createStrategy.fromCommand(command));
    }

    public Page<? extends ShapeDto> findShapesByProperty(String type, String property, String value, String operator, Pageable pageable) {
        Specification<Shape> spec = Specification.where(null);

        if (!StringUtils.isEmpty(type)) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("type"), type));
        }

        if (!StringUtils.isEmpty(property) && !StringUtils.isEmpty(value)) {
            spec = ShapeSpecification.hasPropertyWithOperator(property, value, operator);
        }

        return shapeRepository.findAll(spec, pageable).map(shape -> {
            if (!shapeMappingStrategyMap.containsKey(shape.getType())) {
                throw new TypeNotRecognizedException(shape.getType());
            }
            return shapeMappingStrategyMap.get(shape.getType()).toDto(shape);
        });
    }
}

