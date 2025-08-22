package com.example.quejapp.model.repositories;

import com.example.quejapp.model.Usuario;

public interface UsuarioRepository extends org.springframework.data.mongodb.repository.MongoRepository<Usuario,String> {
    Usuario findByNickname(String nickname);
}
