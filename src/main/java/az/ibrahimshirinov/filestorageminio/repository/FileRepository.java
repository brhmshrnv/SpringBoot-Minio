package az.ibrahimshirinov.filestorageminio.repository;

import az.ibrahimshirinov.filestorageminio.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FileRepository extends JpaRepository<File,Long> {

    List<File> findAllByProductId(Long productId);
    Optional<File> findByProductId(Long productId);
}
