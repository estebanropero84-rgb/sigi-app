package com.sigi.inventario.controlador;

import com.sigi.inventario.model.Usuario;
import com.sigi.inventario.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
   
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    
    @PostMapping
    public ResponseEntity<?> createUsuario(@Valid @RequestBody Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El nombre de usuario ya existe");
            return ResponseEntity.badRequest().body(error);
        }
        
        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El email ya está registrado");
            return ResponseEntity.badRequest().body(error);
        }
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioDetails) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
           
            if (!usuario.getUsername().equals(usuarioDetails.getUsername()) &&
                usuarioRepository.existsByUsername(usuarioDetails.getUsername())) {
                return ResponseEntity.badRequest().build();
            }
            
            
            if (usuarioDetails.getEmail() != null && 
                !usuario.getEmail().equals(usuarioDetails.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioDetails.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            
            usuario.setUsername(usuarioDetails.getUsername());
            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setEmail(usuarioDetails.getEmail());
            usuario.setRol(usuarioDetails.getRol());
            usuario.setActivo(usuarioDetails.getActivo());
            
           
            if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
                usuario.setPassword(usuarioDetails.getPassword());
            }
            
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return ResponseEntity.ok(updatedUsuario);
        }
        
        return ResponseEntity.notFound().build();
    }
    
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            if (usuario.getPassword().equals(password)) {
                if (Boolean.TRUE.equals(usuario.getActivo())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("usuario", usuario);
                    response.put("message", "Login exitoso");
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Usuario inactivo");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
                }
            }
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("error", "Credenciales incorrectas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
   
    @GetMapping("/count/activos")
    public Map<String, Long> countUsuariosActivos() {
        Map<String, Long> response = new HashMap<>();
        response.put("totalActivos", usuarioRepository.countByActivoTrue());
        return response;
    }
}