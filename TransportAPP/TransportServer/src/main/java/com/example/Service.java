package com.example;

import com.example.domain.Cursa;
import com.example.domain.Rezervare;
import com.example.domain.User;
import com.example.repository.Interfaces.CursaRepoInterface;
import com.example.repository.Interfaces.RezervareRepoInterface;
import com.example.repository.Interfaces.UserRepoInterface;
import com.example.service.TransportObserverInterface;
import com.example.service.TransportServiceInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements TransportServiceInterface {

    private final UserRepoInterface repoUser;
    private final CursaRepoInterface repoCursa;
    private final RezervareRepoInterface repoRezervare;

    private Map<Long, TransportObserverInterface> loggedClients;

    public Service(UserRepoInterface repoUser, CursaRepoInterface repoCursa, RezervareRepoInterface repoRezervare) {
        this.repoUser = repoUser;
        this.repoCursa = repoCursa;
        this.repoRezervare = repoRezervare;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    //USER SERVICE
    public User findOneUser(Long id){
        return repoUser.findOne(id);
    }

    public Iterable<User> findAllUsers(){
        return repoUser.findAll();
    }
    public User saveUser(User user) {
        return repoUser.save(user);
    }

    public User updateUser(User user) {
        return repoUser.update(user);
    }

    public User deleteUser(Long id) {
        return repoUser.delete(id);
    }

    public User findUserByUsername(String username) {
        return repoUser.findByUsername(username);
    }

    public synchronized void login(User user, TransportObserverInterface client) throws Exception{
        User userR = repoUser.findByUsername(user.getUsername());
        if(user.getPassword().equals(userR.getPassword())){
            if(loggedClients.get(userR.getId()) != null)
                throw new Exception("User already logged in.");
            loggedClients.put(userR.getId(), client);
        }
        else
            throw new Exception("Authentication failed.");
        System.out.println(loggedClients);
    }

    public synchronized void logout(User user, TransportObserverInterface client) throws Exception{
        User userR = repoUser.findByUsername(user.getUsername());
        if(loggedClients.get(userR.getId()) != null){
            loggedClients.remove(userR.getId());
        }
        else
            throw new Exception("User not logged in.");
    }


    //CURSA SERVICE
    public Cursa findOneCursa(Long id){
        return repoCursa.findOne(id);
    }

    public Iterable<Cursa> findAllCursa(){
        return repoCursa.findAll();
    }

    public synchronized Cursa saveCursa(Cursa cursa) {
        return repoCursa.save(cursa);
    }

    public Cursa updateCursa(Cursa cursa) {
        return repoCursa.update(cursa);
    }

    public Cursa deleteCursa(Long id) {
        return repoCursa.delete(id);
    }

    public List<Cursa> findCursaByDestinatie(String destinatie) {
        return repoCursa.findByDestinatie(destinatie);
    }

    public List<Cursa> findCursaByDataOraPlecare(String dataOraPlecare) {
        return repoCursa.findByDataOraPlecare(dataOraPlecare);
    }


    //REZERVARE SERVICE
    public Rezervare findOneRezervare(Long id){
        return repoRezervare.findOne(id);
    }

    public Iterable<Rezervare> findAllRezervare(){
        return repoRezervare.findAll();
    }

    public Rezervare saveRezervare(Rezervare rezervare){
        Cursa cursa = repoCursa.findOne(rezervare.getIdCursa());
        cursa.setNrLocuriDisponibile(cursa.getNrLocuriDisponibile() - rezervare.getNrLocuri());
        repoCursa.update(cursa);
        notifyRezervareAdded(rezervare);
        return repoRezervare.save(rezervare);
    }
    public Rezervare notifyRezervareAdded(Rezervare rezervare) {
        for(TransportObserverInterface client : loggedClients.values()){
            try{
                client.rezervareReceived(rezervare);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return rezervare;
    }

    public Rezervare updateRezervare(Rezervare rezervare) {
        return repoRezervare.update(rezervare);
    }

    public Rezervare deleteRezervare(Long id) {
        return repoRezervare.delete(id);
    }

    public List<Rezervare> findRezervareByIdCursa(Long idCursa) {
        return repoRezervare.findByIdCursa(idCursa);
    }

    public synchronized void sendRezervare(Rezervare rezervare) {
        saveRezervare(rezervare);
    }
}
