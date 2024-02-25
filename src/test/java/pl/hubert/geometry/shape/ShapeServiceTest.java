package pl.hubert.geometry.shape;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.hubert.geometry.common.exception.PropertyRequiredException;
import pl.hubert.geometry.common.exception.TypeNotRecognizedException;
import pl.hubert.geometry.model.Circle;
import pl.hubert.geometry.model.Shape;
import pl.hubert.geometry.model.Square;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.dto.CircleDto;
import pl.hubert.geometry.model.dto.ShapeDto;
import pl.hubert.geometry.model.dto.SquareDto;
import pl.hubert.geometry.repository.ShapeRepository;
import pl.hubert.geometry.service.ShapeService;
import pl.hubert.geometry.strategy.CircleMappingStrategy;
import pl.hubert.geometry.strategy.ShapeMappingStrategy;
import pl.hubert.geometry.strategy.SquareMappingStrategy;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShapeServiceTest {

    @InjectMocks
    ShapeService shapeService;

    @Mock
    ShapeRepository shapeRepository;

    @Mock
    private Map<String, ShapeMappingStrategy> shapeMappingStrategyMapMock;


    @Test
    void save_HappyPath_ResultsInShapeBeingSaved() {
        CreateShapeCommand command = CreateShapeCommand.builder()
                .type("circle")
                .properties(Map.of("radius", 10.0))
                .build();

        Shape mockShape = Circle.builder()
                .radius(command.getProperties().get("radius"))
                .build();

        ShapeMappingStrategy mockStrategy = mock(ShapeMappingStrategy.class);
        when(shapeMappingStrategyMapMock.containsKey(command.getType())).thenReturn(true);
        when(shapeMappingStrategyMapMock.get(command.getType())).thenReturn(mockStrategy);
        when(mockStrategy.fromCommand(command)).thenReturn(mockShape);
        when(shapeRepository.save(mockShape)).thenReturn(mockShape);

        shapeService.createShape(command);

        verify(mockStrategy).fromCommand(command);
        verify(shapeRepository).save(mockShape);
        verifyNoMoreInteractions(shapeRepository);
    }


    @Test
    void save_ShapeTypeNotFound_ResultsInTypeNotRecognizedException() {
        CreateShapeCommand command = CreateShapeCommand.builder()
                .type("dupa")
                .build();
        String exceptionMsg = MessageFormat.format("Shape not recognized: {0}", "dupa");

        when(shapeMappingStrategyMapMock.containsKey(command.getType())).thenReturn(false);

        assertThatExceptionOfType(TypeNotRecognizedException.class)
                .isThrownBy(() -> shapeService.createShape(command))
                .withMessage(exceptionMsg);

        verifyNoInteractions(shapeRepository);
    }

    @Test
    void save_ShapePropertyNotFound_ResultsInPropertyRequiredException() {
        CreateShapeCommand command = CreateShapeCommand.builder()
                .type("circle")
                .properties(Map.of("gladius", 10.0))
                .build();

        when(shapeMappingStrategyMapMock.containsKey(command.getType())).thenReturn(true);
        when(shapeMappingStrategyMapMock.get(command.getType())).thenReturn(new CircleMappingStrategy());

        assertThatExceptionOfType(PropertyRequiredException.class)
                .isThrownBy(() -> shapeService.createShape(command))
                .withMessage("Property missing: [radius]");

        verifyNoInteractions(shapeRepository);
    }

    @Test
    public void testFindAll_HappyPath_ResultsInAllShapesBeingFound() {
        Circle circle1 = new Circle(1L, "circle", 10F);
        Circle circle2 = new Circle(2L, "circle", 10F);
        Square square1 = new Square(3L, "square", 5F);
        Square square2 = new Square(4L, "square", 5F);
        List<Shape> shapeList = List.of(circle1, circle2, square1, square2);
        Page<Shape> page = new PageImpl<>(shapeList);

        CircleDto circleDto1 = new CircleDto(1L, "circle", 10F);
        CircleDto circleDto2 = new CircleDto(2L, "circle", 10F);
        List<CircleDto> circleDtos = List.of(circleDto1, circleDto2);

        SquareDto squareDto1 = new SquareDto(3L, "square", 5F);
        SquareDto squareDto2 = new SquareDto(4L, "square", 5F);
        List<SquareDto> squareDtos = List.of(squareDto1, squareDto2);

        CircleMappingStrategy circleMappingStrategyMock = mock(CircleMappingStrategy.class);
        SquareMappingStrategy squareMappingStrategyMock = mock(SquareMappingStrategy.class);

        when(circleMappingStrategyMock.toDto(circle1)).thenReturn(circleDto1);
        when(circleMappingStrategyMock.toDto(circle2)).thenReturn(circleDto2);
        when(squareMappingStrategyMock.toDto(square1)).thenReturn(squareDto1);
        when(squareMappingStrategyMock.toDto(square2)).thenReturn(squareDto2);

        when(shapeMappingStrategyMapMock.containsKey("circle")).thenReturn(true);
        when(shapeMappingStrategyMapMock.get("circle")).thenReturn(circleMappingStrategyMock);

        when(shapeMappingStrategyMapMock.containsKey("square")).thenReturn(true);
        when(shapeMappingStrategyMapMock.get("square")).thenReturn(squareMappingStrategyMock);

        when(shapeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<? extends ShapeDto> result = shapeService.findShapesByProperty(null,null, null, null, Pageable.unpaged());

        List<? extends ShapeDto> resultContent = result.getContent();

        List<CircleDto> circleDtosReturned = new ArrayList<>();
        List<SquareDto> squareDtosReturned = new ArrayList<>();
        resultContent.forEach(resultEntry -> {
            if (resultEntry instanceof CircleDto) {
                circleDtosReturned.add((CircleDto) resultEntry);
            } else if (resultEntry instanceof SquareDto) {
                squareDtosReturned.add((SquareDto) resultEntry);
            }
        });

        circleDtosReturned.forEach(System.out::println);
        assertEquals(resultContent.size(), shapeList.size());
        assertTrue(circleDtos.containsAll(circleDtosReturned));
        assertTrue(squareDtos.containsAll(squareDtosReturned));

    }
}
