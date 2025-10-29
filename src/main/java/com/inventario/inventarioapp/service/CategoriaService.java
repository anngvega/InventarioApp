package com.inventario.inventarioapp.service;

import com.inventario.inventarioapp.model.Categoria;
import com.inventario.inventarioapp.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Integer id, Categoria categoriaActualizada) {
        Optional<Categoria> existente = categoriaRepository.findById(id);
        if (existente.isPresent()) {
            Categoria categoria = existente.get();
            categoria.setNombre(categoriaActualizada.getNombre());
            categoria.setDescripcion(categoriaActualizada.getDescripcion());
            return categoriaRepository.save(categoria);
        } else {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
    }

    public void eliminar(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar: categoría no encontrada con ID " + id);
        }
    }
}
