package com.inventario.inventarioapp.service;

import com.inventario.inventarioapp.model.Movimiento;
import com.inventario.inventarioapp.model.Producto;
import com.inventario.inventarioapp.model.Usuario;
import com.inventario.inventarioapp.repository.MovimientoRepository;
import com.inventario.inventarioapp.repository.ProductoRepository;
import com.inventario.inventarioapp.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public Movimiento registrarMovimiento(Movimiento movimiento) {
        Producto producto = productoRepository.findById(movimiento.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (movimiento.getTipo() == Movimiento.TipoMovimiento.ENTRADA) {
            producto.setStock(producto.getStock() + movimiento.getCantidad());
        } else if (movimiento.getTipo() == Movimiento.TipoMovimiento.SALIDA) {
            if (producto.getStock() < movimiento.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para realizar la salida");
            }
            producto.setStock(producto.getStock() - movimiento.getCantidad());
        }

        productoRepository.save(producto);
        return movimientoRepository.save(movimiento);
    }

    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }

    public List<Movimiento> listarPorProducto(Integer idProducto) {
        return movimientoRepository.findByProductoId(idProducto);
    }

    public List<Movimiento> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaMovimientoBetween(inicio, fin);
    }

    public Optional<Movimiento> obtenerPorId(Integer id) {
        return movimientoRepository.findById(id);
    }

    public void eliminar(Integer id) {
        if (movimientoRepository.existsById(id)) {
            movimientoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Movimiento no encontrado con ID: " + id);
        }
    }
}
