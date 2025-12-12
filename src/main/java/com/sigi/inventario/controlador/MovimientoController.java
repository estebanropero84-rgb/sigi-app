package com.sigi.inventario.controlador;

import com.sigi.inventario.model.Movimiento;
import com.sigi.inventario.model.Producto;
import com.sigi.inventario.repositorio.MovimientoRepository;
import com.sigi.inventario.repositorio.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @GetMapping
    public List<Movimiento> getAllMovimientos() {
        return movimientoRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> getMovimientoById(@PathVariable Long id) {
        Optional<Movimiento> movimiento = movimientoRepository.findById(id);
        return movimiento.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createMovimiento(@RequestBody Movimiento movimiento) {
        Optional<Producto> productoOpt = productoRepository.findById(movimiento.getProducto().getId());
        
        if (!productoOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Producto no encontrado");
            return ResponseEntity.badRequest().body(error);
        }
        
        Producto producto = productoOpt.get();
        
        if ("ENTRADA".equals(movimiento.getTipo())) {
            producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
        } else if ("SALIDA".equals(movimiento.getTipo())) {
            if (producto.getStockActual() < movimiento.getCantidad()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Stock insuficiente");
                return ResponseEntity.badRequest().body(error);
            }
            producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Tipo de movimiento inválido");
            return ResponseEntity.badRequest().body(error);
        }
        
        productoRepository.save(producto);
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        return ResponseEntity.ok(savedMovimiento);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovimiento(@PathVariable Long id, @RequestBody Movimiento movimientoDetails) {
        Optional<Movimiento> movimientoOptional = movimientoRepository.findById(id);
        
        if (movimientoOptional.isPresent()) {
            Movimiento movimiento = movimientoOptional.get();
            
         
            if (movimientoDetails.getProducto() != null && 
                movimientoDetails.getProducto().getId() != null &&
                !movimiento.getProducto().getId().equals(movimientoDetails.getProducto().getId())) {
                
                
                Producto productoAnterior = movimiento.getProducto();
                if ("ENTRADA".equals(movimiento.getTipo())) {
                    productoAnterior.setStockActual(productoAnterior.getStockActual() - movimiento.getCantidad());
                } else if ("SALIDA".equals(movimiento.getTipo())) {
                    productoAnterior.setStockActual(productoAnterior.getStockActual() + movimiento.getCantidad());
                }
                productoRepository.save(productoAnterior);
                
                
                Optional<Producto> nuevoProductoOpt = productoRepository.findById(movimientoDetails.getProducto().getId());
                if (nuevoProductoOpt.isPresent()) {
                    Producto nuevoProducto = nuevoProductoOpt.get();
                    if ("ENTRADA".equals(movimientoDetails.getTipo())) {
                        nuevoProducto.setStockActual(nuevoProducto.getStockActual() + movimientoDetails.getCantidad());
                    } else if ("SALIDA".equals(movimientoDetails.getTipo())) {
                        if (nuevoProducto.getStockActual() < movimientoDetails.getCantidad()) {
                            Map<String, String> error = new HashMap<>();
                            error.put("error", "Stock insuficiente para el nuevo producto");
                            return ResponseEntity.badRequest().body(error);
                        }
                        nuevoProducto.setStockActual(nuevoProducto.getStockActual() - movimientoDetails.getCantidad());
                    }
                    productoRepository.save(nuevoProducto);
                }
            } else {
                
                Producto producto = movimiento.getProducto();
                
                
                if ("ENTRADA".equals(movimiento.getTipo())) {
                    producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
                } else if ("SALIDA".equals(movimiento.getTipo())) {
                    producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
                }
                
              
                if ("ENTRADA".equals(movimientoDetails.getTipo())) {
                    producto.setStockActual(producto.getStockActual() + movimientoDetails.getCantidad());
                } else if ("SALIDA".equals(movimientoDetails.getTipo())) {
                    if (producto.getStockActual() < movimientoDetails.getCantidad()) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Stock insuficiente");
                        return ResponseEntity.badRequest().body(error);
                    }
                    producto.setStockActual(producto.getStockActual() - movimientoDetails.getCantidad());
                }
                
                productoRepository.save(producto);
            }
            
            
            if (movimientoDetails.getProducto() != null) {
                movimiento.setProducto(movimientoDetails.getProducto());
            }
            
            if (movimientoDetails.getTipo() != null) {
                movimiento.setTipo(movimientoDetails.getTipo());
            }
            
            if (movimientoDetails.getCantidad() != null) {
                movimiento.setCantidad(movimientoDetails.getCantidad());
            }
            
            movimiento.setPrecioUnitario(movimientoDetails.getPrecioUnitario());
            movimiento.setMotivo(movimientoDetails.getMotivo());
            movimiento.setUsuario(movimientoDetails.getUsuario());
            
            
            if (movimientoDetails.getFecha() != null) {
                movimiento.setFecha(movimientoDetails.getFecha());
            }
            
            Movimiento updatedMovimiento = movimientoRepository.save(movimiento);
            return ResponseEntity.ok(updatedMovimiento);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/producto/{productoId}")
    public List<Movimiento> getMovimientosByProducto(@PathVariable Long productoId) {
        return movimientoRepository.findByProductoId(productoId);
    }
    
    @GetMapping("/tipo/{tipo}")
    public List<Movimiento> getMovimientosByTipo(@PathVariable String tipo) {
        return movimientoRepository.findByTipo(tipo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        if (movimientoRepository.existsById(id)) {
           
            Optional<Movimiento> movimientoOpt = movimientoRepository.findById(id);
            if (movimientoOpt.isPresent()) {
                Movimiento movimiento = movimientoOpt.get();
                Producto producto = movimiento.getProducto();
                
                // Revertir el stock
                if ("ENTRADA".equals(movimiento.getTipo())) {
                    producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
                } else if ("SALIDA".equals(movimiento.getTipo())) {
                    producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
                }
                
                productoRepository.save(producto);
            }
            
            movimientoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/resumen")
    public Map<String, Object> getResumenMovimientos() {
        Map<String, Object> resumen = new HashMap<>();
        List<Movimiento> movimientos = movimientoRepository.findAll();
        
        int totalEntradas = 0;
        int totalSalidas = 0;
        BigDecimal valorEntradas = BigDecimal.ZERO;
        BigDecimal valorSalidas = BigDecimal.ZERO;
        
        for (Movimiento m : movimientos) {
            if ("ENTRADA".equals(m.getTipo())) {
                totalEntradas += m.getCantidad();
                if (m.getPrecioUnitario() != null) {
                    valorEntradas = valorEntradas.add(
                        m.getPrecioUnitario().multiply(BigDecimal.valueOf(m.getCantidad()))
                    );
                }
            } else if ("SALIDA".equals(m.getTipo())) {
                totalSalidas += m.getCantidad();
                if (m.getPrecioUnitario() != null) {
                    valorSalidas = valorSalidas.add(
                        m.getPrecioUnitario().multiply(BigDecimal.valueOf(m.getCantidad()))
                    );
                }
            }
        }
        
        resumen.put("totalMovimientos", movimientos.size());
        resumen.put("totalEntradas", totalEntradas);
        resumen.put("totalSalidas", totalSalidas);
        resumen.put("valorEntradas", valorEntradas);
        resumen.put("valorSalidas", valorSalidas);
        resumen.put("diferencia", valorEntradas.subtract(valorSalidas));
        
        return resumen;
    }
}