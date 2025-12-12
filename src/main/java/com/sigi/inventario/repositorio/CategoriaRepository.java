package com.sigi.inventario.repositorio;

import com.sigi.inventario.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombre(String nombre);
    long count();
}