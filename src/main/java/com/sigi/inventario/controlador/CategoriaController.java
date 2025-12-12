package com.sigi.inventario.controlador;

import com.sigi.inventario.model.Categoria;
import com.sigi.inventario.repositorio.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta .validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @GetMapping
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
 
    @PostMapping
    public ResponseEntity<?> createCategoria(@Valid @RequestBody Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ya existe una categoría con ese nombre");
            return ResponseEntity.badRequest().body(error);
        }
        
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoria);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoriaDetails) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            
            
            if (!categoria.getNombre().equals(categoriaDetails.getNombre()) &&
                categoriaRepository.existsByNombre(categoriaDetails.getNombre())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Ya existe una categoría con ese nombre");
                return ResponseEntity.badRequest().body(error);
            }
            
            categoria.setNombre(categoriaDetails.getNombre());
            categoria.setDescripcion(categoriaDetails.getDescripcion());
            
            Categoria updatedCategoria = categoriaRepository.save(categoria);
            return ResponseEntity.ok(updatedCategoria);
        }
        
        return ResponseEntity.notFound().build();
    }
    
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
   
    @GetMapping("/buscar")
    public List<Categoria> buscarCategorias(@RequestParam String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    
    @GetMapping("/count")
    public Map<String, Long> countCategorias() {
        Map<String, Long> response = new HashMap<>();
        response.put("total", categoriaRepository.count());
        return response;
    }
}