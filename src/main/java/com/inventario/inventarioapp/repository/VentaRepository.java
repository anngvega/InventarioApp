package com.inventario.inventarioapp.repository;

import com.inventario.inventarioapp.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
    @Query("SELECT SUM(dv.cantidad * p.precioVenta) FROM DetalleVenta dv JOIN dv.producto p")
    BigDecimal calcularTotalIngresos();

    @Query("SELECT SUM(dv.cantidad * p.precioCosto) FROM DetalleVenta dv JOIN dv.producto p")
    BigDecimal calcularTotalCostos();
}
