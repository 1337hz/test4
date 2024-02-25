package pl.hubert.geometry.model.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CreateShapeCommand {
    @NotEmpty(message = "Map must not be empty")
    private String type;
    @NotEmpty(message = "Map must not be empty")
    private Map<@NotEmpty String, @Positive Double> properties;
}
