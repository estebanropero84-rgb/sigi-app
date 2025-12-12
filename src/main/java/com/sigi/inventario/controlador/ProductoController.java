package com.sigi.inventario.controlador;

import com.sigi.inventario.model.Producto;
import com.sigi.inventario.repositorio.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createProducto(@RequestBody Producto producto) {
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Código ya existe");
            return ResponseEntity.badRequest().body(error);
        }
        Producto savedProducto = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            if (!producto.getCodigo().equals(productoDetails.getCodigo()) &&
                productoRepository.existsByCodigo(productoDetails.getCodigo())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Código ya existe");
                return ResponseEntity.badRequest().body(error);
            }
            producto.setCodigo(productoDetails.getCodigo());
            producto.setNombre(productoDetails.getNombre());
            producto.setDescripcion(productoDetails.getDescripcion());
            producto.setPrecio(productoDetails.getPrecio());
            producto.setStockActual(productoDetails.getStockActual());
            producto.setStockMinimo(productoDetails.getStockMinimo());
            producto.setCategoria(productoDetails.getCategoria());
            Producto updatedProducto = productoRepository.save(producto);
            return ResponseEntity.ok(updatedProducto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/buscar")
    public List<Producto> buscarProductos(@RequestParam String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @GetMapping("/categoria/{categoriaId}")
    public List<Producto> getProductosByCategoria(@PathVariable Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }
    
    @GetMapping("/bajo-stock")
    public List<Producto> getProductosBajoStock() {
        return productoRepository.findByStockActualLessThan(5);
    }
}