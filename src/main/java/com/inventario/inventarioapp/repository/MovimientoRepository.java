package com.inventario.inventarioapp.repository;

import com.inventario.inventarioapp.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    List<Movimiento> findByFechaMovimientoBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Movimiento> findByProductoId(Integer idProducto);
}
