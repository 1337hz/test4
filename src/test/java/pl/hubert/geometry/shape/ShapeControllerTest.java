package pl.hubert.geometry.shape;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.hubert.geometry.model.Circle;
import pl.hubert.geometry.model.Square;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.repository.ShapeRepository;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShapeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ShapeRepository shapeRepository;


    @Test
    void testFindAll_ResultsInAllShapesBeingReturned() throws Exception {
        Circle circle = new Circle(15F);
        circle.setType("circle");
        circle.setId(1L);
        Square square = new Square(10F);
        square.setId(2L);
        square.setType("square");
        shapeRepository.save(circle);
        shapeRepository.save(square);

        mockMvc.perform(get("/api/v1/shapes"))
                .andDo(print())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].type").value("circle"))
                .andExpect(jsonPath("$.content[0].radius").value(15.0))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].type").value("square"))
                .andExpect(jsonPath("$.content[1].side").value(10.0));

    }

    @Test
    void testFindAllWithTypeParam_ResultsInCorrectShapesBeingReturned() throws Exception {
        Circle circle = new Circle(15F);
        circle.setType("circle");
        circle.setId(1L);
        Square square = new Square(10F);
        square.setId(2L);
        square.setType("square");
        shapeRepository.save(circle);
        shapeRepository.save(square);

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", "circle"))
                .andDo(print())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].type").value("circle"));
    }

    @Test
    void testFindAllWithPropertyAndValueParams_ResultsInCorrectShapesBeingReturned() throws Exception {
        Circle circle1 = new Circle(15F);
        circle1.setType("circle");
        circle1.setId(1L);
        Circle circle2 = new Circle(10F);
        circle2.setType("circle");
        circle2.setId(2L);
        Square square = new Square(10F);
        square.setId(3L);
        square.setType("square");

        shapeRepository.save(circle1);
        shapeRepository.save(circle2);
        shapeRepository.save(square);

        mockMvc.perform(get("/api/v1/shapes")
                        .param("radius", "10"))
                .andDo(print())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].type").value("circle"))
                .andExpect(jsonPath("$.content[0].radius").value(10.0));
    }

    @Test
    void testFindAllWithPropertyAndValueAndOperatorParams_ResultsInCorrectShapesBeingReturned() throws Exception {
        Circle circle1 = new Circle(15F);
        circle1.setType("circle");
        circle1.setId(1L);
        Circle circle2 = new Circle(10F);
        circle2.setType("circle");
        circle2.setId(2L);
        Square square = new Square(10F);
        square.setId(3L);
        square.setType("square");

        shapeRepository.save(circle1);
        shapeRepository.save(circle2);
        shapeRepository.save(square);

        mockMvc.perform(get("/api/v1/shapes?radius>10"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].type").value("circle"))
                .andExpect(jsonPath("$.content[0].radius").value(15.0));
    }

    @Test
    void testFindAllWithTypeAndPropertyAndValueParams_ResultsInCorrectShapesBeingReturned() throws Exception {
        Circle circle1 = new Circle(15F);
        circle1.setType("circle");
        circle1.setId(1L);
        Circle circle2 = new Circle(10F);
        circle2.setType("circle");
        circle2.setId(2L);
        Square square = new Square(10F);
        square.setId(3L);
        square.setType("square");

        shapeRepository.save(circle1);
        shapeRepository.save(circle2);
        shapeRepository.save(square);

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", "circle")
                        .param("radius", "10"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].type").value("circle"))
                .andExpect(jsonPath("$.content[0].radius").value(10.0));
    }

    @Test
    void testFindAllWithIncorrectTypeParam_ResultsInNoShapesBeingReturned() throws Exception {
        Circle circle1 = new Circle(15F);
        circle1.setType("circle");
        circle1.setId(1L);
        Circle circle2 = new Circle(10F);
        circle2.setType("circle");
        circle2.setId(2L);
        Square square = new Square(10F);
        square.setId(3L);
        square.setType("square");

        shapeRepository.save(circle1);
        shapeRepository.save(circle2);
        shapeRepository.save(square);

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", "incorrectType"))
                .andDo(print())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void testFindAllWithIncorrectParam_ReturnsShapesWithCorrectType() throws Exception {
        Circle circle1 = new Circle(15F);
        circle1.setType("circle");
        circle1.setId(1L);
        Circle circle2 = new Circle(10F);
        circle2.setType("circle");
        circle2.setId(2L);
        Square square = new Square(10F);
        square.setId(3L);
        square.setType("square");

        shapeRepository.save(circle1);
        shapeRepository.save(circle2);
        shapeRepository.save(square);

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", "circle")
                        .param("radius", "incorrectRadius"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].type").value("circle"))
                .andExpect(jsonPath("$.content[1].type").value("circle"));
    }

    @Test
    void testCreateWithUnknownType_ResultsInValidationMessageBeingReturned() throws Exception {
        CreateShapeCommand command = CreateShapeCommand.builder()
                .type("aaa")
                .properties(Map.of("radius", 10.0))
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Shape not recognized: aaa")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testCreateWithIncorrectProperty_ResultsInValidationMessageBeingReturned() throws Exception {
        CreateShapeCommand command = CreateShapeCommand.builder()
                .type("circle")
                .properties(Map.of("baba", 10.0))
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Property missing: [radius]")))
                .andExpect(jsonPath("$.timestamp").exists());
    }


    @Test
    void testCreate_ResultsInShapeBeingCreated() throws Exception {
        CreateShapeCommand command = CreateShapeCommand.builder()
                .type("circle")
                .properties(Map.of("radius", 10.0))
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("circle")))
                .andExpect(jsonPath("$.radius", is(10.0)));

    }
}
