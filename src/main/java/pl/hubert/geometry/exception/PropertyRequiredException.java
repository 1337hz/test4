package pl.hubert.geometry.exception;

import java.util.List;

public class PropertyRequiredException extends RuntimeException {
    public PropertyRequiredException(List<String> fields) {
        super(String.format("Property missing: [%s]", String.join(", ", fields)));
    }
}
