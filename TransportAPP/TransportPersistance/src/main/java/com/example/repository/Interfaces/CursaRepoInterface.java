package com.example.repository.Interfaces;

import com.example.domain.Cursa;
import com.example.repository.Repository;

import java.util.List;

public interface CursaRepoInterface extends Repository<Long, Cursa> {
    List<Cursa> findByDestinatie(String destinatie);
    List<Cursa> findByDataOraPlecare(String dateTime);
}
