package com.example.quejapp.model.repositories;

import com.example.quejapp.DTOs.ReportDTO;
import com.example.quejapp.model.Queja;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuejaRepository extends MongoRepository<Queja, String> {
    List<Queja> findByUsuarioIdOrderByFechaDesc(String usuarioId);

    List<Queja> findAllByOrderByFechaDesc(); // Para admin

}
