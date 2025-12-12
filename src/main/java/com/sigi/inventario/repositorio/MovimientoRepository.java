package com.sigi.inventario.repositorio;

import com.sigi.inventario.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByProductoId(Long productoId);
    List<Movimiento> findByTipo(String tipo);
}