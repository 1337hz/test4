package pl.hubert.geometry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.hubert.geometry.model.Shape;

@Repository
public interface ShapeRepository extends JpaRepository<Shape, Long>, JpaSpecificationExecutor<Shape> {
}
