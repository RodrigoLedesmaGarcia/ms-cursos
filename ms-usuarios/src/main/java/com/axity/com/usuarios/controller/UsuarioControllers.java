package com.axity.com.usuarios.controller;

import com.axity.com.usuarios.models.entity.Usuario;
import com.axity.com.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// http://localhost:8081/

@RestController
public class UsuarioControllers {

    //inyeccion de dependencias
    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar () {

        return service.listar();
    } // fin del metodo listar


    @GetMapping("/{id}")
    public ResponseEntity<Object> detalle (@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    } // fin del metodo detalle


    @PostMapping
    public ResponseEntity<?> crear (@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    } // fin del metodo crear

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@RequestBody Usuario usuario, @PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()) {
            Usuario usuariodb = usuarioOptional.get();
            usuariodb.setNombre(usuario.getNombre());
            usuariodb.setEmail(usuario.getEmail());
            usuariodb.setPassword(usuario.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuariodb));
        }

        return ResponseEntity.notFound().build();
    } // fin del metodo editar

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    } // fin del metodo eliminar
}// fin de la clase controller
