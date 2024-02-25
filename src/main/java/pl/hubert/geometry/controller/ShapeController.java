package pl.hubert.geometry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.hubert.geometry.model.Shape;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.dto.ShapeDto;
import pl.hubert.geometry.service.ShapeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shapes")
public class ShapeController {

    private final ShapeService shapeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shape createShape(@Valid @RequestBody CreateShapeCommand command) {
        return shapeService.createShape(command);
    }

    @GetMapping
    public ResponseEntity<Page<? extends ShapeDto>> findAllByProperty(Pageable pageable,
                                                                      @RequestParam(required = false) String type,
                                                                      @RequestParam(required = false) String property,
                                                                      @RequestParam(required = false, defaultValue = "eq") String operator,
                                                                      @RequestParam(required = false) String value) {


        return ResponseEntity.ok(shapeService.findShapesByProperty(type, property, value, operator, pageable));
    }
}
