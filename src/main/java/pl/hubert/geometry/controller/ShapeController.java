package pl.hubert.geometry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.hubert.geometry.model.command.CreateShapeCommand;
import pl.hubert.geometry.model.dto.ShapeDto;
import pl.hubert.geometry.service.ShapeService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shapes")
public class ShapeController {

    private final ShapeService shapeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<? extends ShapeDto> createShape(@Valid @RequestBody CreateShapeCommand command) {
        return ResponseEntity.ok(shapeService.createShape(command));
    }

    @GetMapping
    public ResponseEntity<Page<? extends ShapeDto>> findAllByProperty(Pageable pageable, @RequestParam Map<String, String> requestParams) {
        return ResponseEntity.ok(shapeService.findShapesByProperty(pageable, requestParams));
    }
}
