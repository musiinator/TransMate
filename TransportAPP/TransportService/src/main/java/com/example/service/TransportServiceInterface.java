package com.example.service;

import com.example.domain.Cursa;
import com.example.domain.Rezervare;
import com.example.domain.User;

import java.util.List;

public interface TransportServiceInterface {

    //USER
    User findOneUser(Long id);
    Iterable<User> findAllUsers() throws Exception;
    User saveUser(User user);
    User updateUser(User user);
    User deleteUser(Long id);
    User findUserByUsername(String username) throws Exception;

    //CURSA
    Cursa findOneCursa(Long id);
    Iterable<Cursa> findAllCursa() throws Exception;
    Cursa saveCursa(Cursa cursa);
    Cursa updateCursa(Cursa cursa) throws Exception;
    Cursa deleteCursa(Long id);
    List<Cursa> findCursaByDestinatie(String destinatie);
    List<Cursa> findCursaByDataOraPlecare(String dateTime);
    void login(User user, TransportObserverInterface client) throws Exception;
    void logout(User user, TransportObserverInterface client) throws Exception;

    //REZERVARE
    Rezervare findOneRezervare(Long id);
    Iterable<Rezervare> findAllRezervare() throws Exception;
    Rezervare saveRezervare(Rezervare rezervare) throws Exception;
    Rezervare updateRezervare(Rezervare rezervare);
    Rezervare deleteRezervare(Long id);
    List<Rezervare> findRezervareByIdCursa(Long idCursa) throws Exception;
}
