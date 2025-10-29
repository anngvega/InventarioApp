package com.inventario.inventarioapp.controller;

import com.inventario.inventarioapp.model.Movimiento;
import com.inventario.inventarioapp.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public List<Movimiento> listar() {
        return movimientoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> obtenerPorId(@PathVariable Integer id) {
        Optional<Movimiento> movimiento = movimientoService.obtenerPorId(id);
        return movimiento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Movimiento>> listarPorProducto(@PathVariable Integer idProducto) {
        List<Movimiento> movimientos = movimientoService.listarPorProducto(idProducto);
        if (movimientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<Movimiento>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Movimiento> movimientos = movimientoService.listarPorRangoFechas(inicio, fin);
        if (movimientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Movimiento movimiento) {
        try {
            Movimiento nuevo = movimientoService.registrarMovimiento(movimiento);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            movimientoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
