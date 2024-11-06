package com.axity.com.usuarios.repositories;

import com.axity.com.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

}
