package com.example.repository.database;

import com.example.domain.Rezervare;
import com.example.repository.Interfaces.RezervareRepoInterface;
import com.example.repository.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RezervareDataBase implements RezervareRepoInterface {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RezervareDataBase (Properties properties){
        logger.info("Initializing RezervareDataBase with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }
    @Override
    public Rezervare findOne(Long id) {
        Connection connection = dbUtils.getConnection();
        Rezervare rezervare = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from rezervare where id_rezervare = ?")){
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                String numeClient = resultSet.getString("nume_client");
                Integer nrLocuri = resultSet.getInt("nr_locuri");
                Long idCursa = resultSet.getLong("id_cursa");
                rezervare = new Rezervare(numeClient, nrLocuri, idCursa);
                rezervare.setId(id);
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return rezervare;
    }

    @Override
    public Iterable<Rezervare> findAll() {
        List<Rezervare> rezervari = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from rezervare")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()) {
                    String numeClient = resultSet.getString("nume_client");
                    Integer nrLocuri = resultSet.getInt("nr_locuri");
                    Long idCursa = resultSet.getLong("id_cursa");
                    Long id = resultSet.getLong("id_rezervare");
                    Rezervare rezervare = new Rezervare(numeClient, nrLocuri, idCursa);
                    rezervare.setId(id);
                    rezervari.add(rezervare);
                }
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return rezervari;
    }

    @Override
    public Rezervare save(Rezervare entity) {
        logger.traceEntry("Task: save", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("insert into rezervare (nume_client, nr_locuri, id_cursa) values (?,?,?)")){
            preStmt.setString(1, entity.getNumeClient());
            preStmt.setInt(2, entity.getNrLocuri());
            preStmt.setLong(3, entity.getIdCursa());
            int result = preStmt.executeUpdate();
            logger.trace("Saved{}", result);
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB " + exception);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Rezervare delete(Long id) {
        logger.traceEntry("Task: delete", findOne(id));
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("delete from rezervare where id_rezervare = ?")){
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {}", result);
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB " + exception);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Rezervare update(Rezervare entity) {
        logger.traceEntry("Task: update", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("update rezervare set nume_client = ?, nr_locuri = ?, id_cursa = ? where id_rezervare = ?")){
            preStmt.setString(1, entity.getNumeClient());
            preStmt.setInt(2, entity.getNrLocuri());
            preStmt.setLong(3, entity.getIdCursa());
            preStmt.setLong(4, entity.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Updated{}", result);
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB " + exception);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public List<Rezervare> findByIdCursa(Long idCursa) {
        List<Rezervare> rezervari = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from rezervare where id_cursa = ?")){
            preparedStatement.setLong(1, idCursa);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()) {
                    Long id = resultSet.getLong("id_rezervare");
                    String numeClient = resultSet.getString("nume_client");
                    Integer nrLocuri = resultSet.getInt("nr_locuri");
                    Rezervare rezervare = new Rezervare(numeClient, nrLocuri, idCursa);
                    rezervare.setId(id);
                    rezervari.add(rezervare);
                }
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return rezervari;
    }
}
