package com.inventario.inventarioapp.service;

import com.inventario.inventarioapp.model.DetalleVenta;
import com.inventario.inventarioapp.model.Producto;
import com.inventario.inventarioapp.model.Venta;
import com.inventario.inventarioapp.repository.ProductoRepository;
import com.inventario.inventarioapp.repository.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public Venta registrarVenta(Venta venta) {
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProducto().getId()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            detalle.setPrecioUnitario(producto.getPrecioVenta());

            BigDecimal subtotal = producto.getPrecioVenta().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setSubtotal(subtotal);

            detalle.setVenta(venta);

            total = total.add(subtotal);
        }

        venta.setTotal(total);
        venta.setFechaVenta(LocalDateTime.now());

        return ventaRepository.save(venta);
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> obtenerPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    public List<Venta> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    public BigDecimal calcularTotalIngresos() {
        BigDecimal total = ventaRepository.calcularTotalIngresos();
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal calcularTotalCostos() {
        BigDecimal total = ventaRepository.calcularTotalCostos();
        return total != null ? total : BigDecimal.ZERO;
    }

    public Map<String, BigDecimal> obtenerResumenFinanciero() {
        BigDecimal ingresos = calcularTotalIngresos();
        BigDecimal costos = calcularTotalCostos();
        BigDecimal ganancia = ingresos.subtract(costos);

        Map<String, BigDecimal> resumen = new HashMap<>();
        resumen.put("total_ingresos", ingresos);
        resumen.put("total_costos", costos);
        resumen.put("ganancia_total", ganancia);
        return resumen;
    }
}
