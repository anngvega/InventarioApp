package com.inventario.inventarioapp.service;

import com.inventario.inventarioapp.model.Producto;
import com.inventario.inventarioapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto productoActualizado) {
        Optional<Producto> existente = productoRepository.findById(id);
        if (existente.isPresent()) {
            Producto producto = existente.get();
            producto.setCodigo(productoActualizado.getCodigo());
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecioCosto(productoActualizado.getPrecioCosto());
            producto.setPrecioVenta(productoActualizado.getPrecioVenta());
            producto.setStock(productoActualizado.getStock());
            producto.setStockMinimo(productoActualizado.getStockMinimo());
            producto.setActivo(productoActualizado.getActivo());
            producto.setCategoria(productoActualizado.getCategoria());
            producto.setProveedor(productoActualizado.getProveedor());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
    }

    public void eliminar(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar: producto no encontrado con ID " + id);
        }
    }

    public List<Producto> listarStockBajo() {
        return productoRepository.findAll()
                .stream()
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .toList();
    }
}
