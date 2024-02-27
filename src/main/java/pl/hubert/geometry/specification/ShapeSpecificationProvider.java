package pl.hubert.geometry.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.hubert.geometry.model.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShapeSpecificationProvider {

    public static Specification<Shape> get(Map<String, String> requestParams) {
        List<Specification<Shape>> specs = new ArrayList<>();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (isTypeSpecification(key, value)) {
                specs.add(getSpecByOperator("type", value, "="));
            } else if (isValidDouble(value)) {
                specs.add(getSpecByOperator(key, value, "="));
            } else {
                if (key.contains(">")) {
                    String[] splitted = key.split(">");
                    if (splitted.length == 2 && isValidDouble(splitted[1]))
                        specs.add(getSpecByOperator(splitted[0], splitted[1], ">"));
                } else if (key.contains("<")) {
                    String[] splitted = key.split("<");
                    if (splitted.length == 2 && isValidDouble(splitted[1]))
                        specs.add(getSpecByOperator(splitted[0], splitted[1], "<"));
                }
            }
        }

        return mergeSpecs(specs);
    }

    private static Specification<Shape> mergeSpecs(List<Specification<Shape>> specs) {
        Specification<Shape> spec = Specification.where(null);
        for (Specification<Shape> s : specs) {
            spec = spec.and(s);
        }

        return spec;
    }

    public static Specification<Shape> getSpecByOperator(String property, String value, String operator) {
        return (root, query, criteriaBuilder) -> switch (operator) {
            case "=" -> criteriaBuilder.equal(root.get(property), value);
            case ">" -> criteriaBuilder.greaterThan(root.get(property), value);
            case "<" -> criteriaBuilder.lessThan(root.get(property), value);
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }


    private static boolean isTypeSpecification(String key, String value) {
        return key.equalsIgnoreCase("type") && value != null && !value.isEmpty();
    }


    public static boolean isValidDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException | NullPointerException e ) {
            return false;
        }
    }
}