package com.inventario.inventarioapp.controller;

import com.inventario.inventarioapp.model.Venta;
import com.inventario.inventarioapp.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listarVentas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Integer id) {
        Optional<Venta> venta = ventaService.obtenerPorId(id);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<Venta>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Venta> ventas = ventaService.listarPorRangoFechas(inicio, fin);
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.registrarVenta(venta);
            return ResponseEntity.ok(nuevaVenta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, BigDecimal>> obtenerResumenFinanciero() {
        Map<String, BigDecimal> resumen = ventaService.obtenerResumenFinanciero();
        return ResponseEntity.ok(resumen);
    }

}
