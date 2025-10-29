package com.inventario.inventarioapp.service;

import com.inventario.inventarioapp.model.Proveedor;
import com.inventario.inventarioapp.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizar(Integer id, Proveedor proveedorActualizado) {
        Optional<Proveedor> existente = proveedorRepository.findById(id);
        if (existente.isPresent()) {
            Proveedor proveedor = existente.get();
            proveedor.setNombre(proveedorActualizado.getNombre());
            proveedor.setTelefono(proveedorActualizado.getTelefono());
            proveedor.setCorreo(proveedorActualizado.getCorreo());
            proveedor.setDireccion(proveedorActualizado.getDireccion());
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }

    public void eliminar(Integer id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar: proveedor no encontrado con ID " + id);
        }
    }
}
