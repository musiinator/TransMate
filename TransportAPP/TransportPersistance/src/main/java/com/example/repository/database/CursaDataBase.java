package com.example.repository.database;

import com.example.domain.Cursa;
import com.example.repository.Interfaces.CursaRepoInterface;
import com.example.repository.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CursaDataBase implements CursaRepoInterface {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

   public CursaDataBase (Properties properties){
       logger.info("Initializing CursaDataBase with properties: {} ", properties);
       dbUtils = new JdbcUtils(properties);
   }

    @Override
    public Cursa findOne(Long id) {
        Connection connection = dbUtils.getConnection();
        Cursa cursa = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from cursa where id_cursa = ?")){
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                String destinatie = resultSet.getString("destinatie");
                String dataOraPlecareString = resultSet.getString("data_ora_plecare");
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dataOraPlecare = LocalDateTime.parse(dataOraPlecareString, formatter);
                Integer nrLocuriDisponibile = resultSet.getInt("nr_locuri_disponibile");
                cursa = new Cursa(destinatie, dataOraPlecare, nrLocuriDisponibile);
                cursa.setId(id);
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return cursa;
    }

    @Override
    public Iterable<Cursa> findAll() {
       List<Cursa> curse = new ArrayList<>();
       Connection connection = dbUtils.getConnection();
       try(PreparedStatement preparedStatement = connection.prepareStatement("select * from cursa")){
           try(ResultSet resultSet = preparedStatement.executeQuery()){
               while(resultSet.next()) {
                   String destinatie = resultSet.getString("destinatie");
                   String dataOraPlecareString = resultSet.getString("data_ora_plecare");
                   DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                   LocalDateTime dataOraPlecare = LocalDateTime.parse(dataOraPlecareString, formatter);
                   Integer nrLocuriDisponibile = resultSet.getInt("nr_locuri_disponibile");
                   Long id = resultSet.getLong("id_cursa");
                   Cursa cursa = new Cursa(destinatie, dataOraPlecare, nrLocuriDisponibile);
                   cursa.setId(id);
                   curse.add(cursa);
               }
           }
       }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
       }
       logger.traceExit();
       return curse;
    }

    @Override
    public Cursa save(Cursa entity) {
        logger.traceEntry("Task: save", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt= connection.prepareStatement("insert into cursa (destinatie, data_ora_plecare, nr_locuri_disponibile) values (?,?,?)")){
            preStmt.setString(1, entity.getDestinatie());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String dataOraPlecare = entity.getDataOraPlecare().format(formatter);
            preStmt.setString(2, dataOraPlecare);
            preStmt.setInt(3, entity.getNrLocuriDisponibile());
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
    public Cursa delete(Long id) {
        logger.traceEntry("Task: delete", findOne(id));
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("delete from cursa where id_cursa = ?")){
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
    public Cursa update(Cursa entity) {
        logger.traceEntry("Task: update", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("update cursa set destinatie = ?, data_ora_plecare = ?, nr_locuri_disponibile = ? where id_cursa = ?")){
            preStmt.setString(1, entity.getDestinatie());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String dataOraPlecare = entity.getDataOraPlecare().format(formatter);
            preStmt.setString(2, dataOraPlecare);
            preStmt.setInt(3, entity.getNrLocuriDisponibile());
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
    public List<Cursa> findByDestinatie(String destinatie) {
        List<Cursa> curse = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from cursa where destinatie = ?")){
            preparedStatement.setString(1, destinatie);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()) {
                    Long id = resultSet.getLong("id_cursa");
                    String dataOraPlecareString = resultSet.getString("data_ora_plecare");
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    LocalDateTime dataOraPlecare = LocalDateTime.parse(dataOraPlecareString, formatter);
                    Integer nrLocuriDisponibile = resultSet.getInt("nr_locuri_disponibile");
                    Cursa cursa = new Cursa(destinatie, dataOraPlecare, nrLocuriDisponibile);
                    cursa.setId(id);
                    curse.add(cursa);
                }
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return curse;
    }

    @Override
    public List<Cursa> findByDataOraPlecare(String dateTime) {
        List<Cursa> curse = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from cursa where data_ora_plecare = ?")){
            preparedStatement.setString(1, dateTime);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()) {
                    Long id = resultSet.getLong("id_cursa");
                    String destinatie = resultSet.getString("destinatie");
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    LocalDateTime dataOraPlecare = LocalDateTime.parse(dateTime, formatter);
                    Integer nrLocuriDisponibile = resultSet.getInt("nr_locuri_disponibile");
                    Cursa cursa = new Cursa(destinatie, dataOraPlecare, nrLocuriDisponibile);
                    cursa.setId(id);
                    curse.add(cursa);
                }
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return curse;
    }
}
