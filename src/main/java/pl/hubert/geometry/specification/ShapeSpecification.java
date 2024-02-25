package pl.hubert.geometry.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.hubert.geometry.model.Shape;

public class ShapeSpecification {
    public static Specification<Shape> hasPropertyWithOperator(String property, String value, String operator) {

        return (root, query, criteriaBuilder) -> switch (operator) {
            case "eq" -> criteriaBuilder.equal(root.get(property), value);
            case "gt" -> criteriaBuilder.greaterThan(root.get(property), value);
            case "lt" -> criteriaBuilder.lessThan(root.get(property), value);
            case "ge" -> criteriaBuilder.greaterThanOrEqualTo(root.get(property), value);
            case "le" -> criteriaBuilder.lessThanOrEqualTo(root.get(property), value);
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }
}