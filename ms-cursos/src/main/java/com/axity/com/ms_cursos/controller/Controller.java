package com.axity.com.ms_cursos.controller;

import com.axity.com.ms_cursos.entity.Curso;
import com.axity.com.ms_cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.ResourceBundle;

@RestController
public class Controller {

    @Autowired
    private CursoService service;


    // http://localhost:8082/
    @GetMapping
    public ResponseEntity<?>  listar() {
        return ResponseEntity.ok(service.listar());
    }

    // http://localhost:8082/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> optional = service.porId(id);

        if(optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }

        return ResponseEntity.notFound().build();
    }

    // http://localhost:8082/
    @PostMapping("/")
    public ResponseEntity<?> crear (@RequestBody Curso curso) {
        Curso cursodb = service.guardar(curso);

        return ResponseEntity.status(HttpStatus.CREATED).body(cursodb);
    }

    // http://localhost:8082/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@RequestBody Curso curso, @PathVariable Long id){
        Optional<Curso> optional = service.porId(id);
        if(optional.isPresent()) {
            Curso cursodb = optional.get();
            cursodb.setNombre(curso.getNombre());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursodb));
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> optional = service.porId(id);

        if(optional.isPresent()) {
            service.eliminar(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
