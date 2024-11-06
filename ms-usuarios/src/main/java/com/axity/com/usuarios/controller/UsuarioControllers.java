package com.axity.com.usuarios.controller;

import com.axity.com.usuarios.models.entity.Usuario;
import com.axity.com.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<?> crear (@Valid @RequestBody Usuario usuario, BindingResult result) {
        if(service.porEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ese correo electronico ya esta registrado"));
        }
        if(result.hasErrors()){
            return validar(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    } // fin del metodo crear

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@Valid @RequestBody Usuario usuario, BindingResult result ,@PathVariable Long id) {

        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()) {
            Usuario usuariodb = usuarioOptional.get();
            if(!usuario.getEmail().equalsIgnoreCase(usuariodb.getEmail()) && service.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ese correo electronico ya esta registrado"));
            }
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


    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(errr -> {
            errores.put(errr.getField(), "el campo " + errr.getField()+ " " + errr.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errores);
    }// fin del metodo validar

}// fin de la clase controller
