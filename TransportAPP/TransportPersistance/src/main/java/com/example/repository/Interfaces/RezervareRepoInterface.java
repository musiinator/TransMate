package com.example.repository.Interfaces;

import com.example.domain.Rezervare;
import com.example.repository.Repository;

import java.util.List;

public interface RezervareRepoInterface extends Repository<Long, Rezervare> {
    List<Rezervare> findByIdCursa(Long id);
}
