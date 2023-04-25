package com.example.rpcprotocol;

import com.example.domain.Cursa;
import com.example.domain.Rezervare;
import com.example.domain.User;
import com.example.rpcprotocol.RequestType;
import com.example.service.TransportObserverInterface;
import com.example.service.TransportServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransportServiceRpcProxy implements TransportServiceInterface {

    private String host;
    private int port;
    private TransportObserverInterface client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public TransportServiceRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public User findOneUser(Long id) {
        return null;
    }

    @Override
    public void login(User user, TransportObserverInterface client) throws Exception {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public void logout(User user, TransportObserverInterface client) throws Exception {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        closeConnection();
    }

    @Override
    public Iterable<User> findAllUsers() throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_USERS).build();
        sendRequest(req);

        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return (List<User>) response.data();
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User deleteUser(Long id) {
        return null;
    }

    @Override
    public User findUserByUsername(String username) throws Exception{
        Request req = new Request.Builder().type(RequestType.GET_USER_BY_USERNAME).data(username).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return (User) response.data();
    }

    @Override
    public Cursa findOneCursa(Long id) {
        return null;
    }

    @Override
    public Iterable<Cursa> findAllCursa() throws Exception{
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.GET_CURSE).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return (List<Cursa>) response.data();
    }

    @Override
    public Cursa saveCursa(Cursa cursa) {
        return null;
    }

    @Override
    public Cursa updateCursa(Cursa cursa) throws Exception {
        Request req = new Request.Builder().type(RequestType.UPDATE_CURSA).data(cursa).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return (Cursa) response.data();
    }

    @Override
    public Cursa deleteCursa(Long id) {
        return null;
    }

    @Override
    public List<Cursa> findCursaByDestinatie(String destinatie) {
        return null;
    }

    @Override
    public List<Cursa> findCursaByDataOraPlecare(String dateTime) {
        return null;
    }

    @Override
    public Rezervare findOneRezervare(Long id) {
        return null;
    }

    @Override
    public Iterable<Rezervare> findAllRezervare() throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_REZERVARI).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return (List<Rezervare>) response.data();
    }

    @Override
    public Rezervare saveRezervare(Rezervare rezervare) throws Exception {
        Request req = new Request.Builder().type(RequestType.SAVE_REZERVARE).data(rezervare).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return (Rezervare) response.data();
    }

    @Override
    public Rezervare updateRezervare(Rezervare rezervare) {
        return null;
    }

    @Override
    public Rezervare deleteRezervare(Long id) {
        return null;
    }

    @Override
    public List<Rezervare> findRezervareByIdCursa(Long idCursa) throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_REZERVARI_BY_ID_CURSA).data(idCursa).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return (List<Rezervare>) response.data();
    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }

    }

    private void handleUpdate(Response response){
        if(response.type() == ResponseType.NEW_REZERVARE){
            Rezervare rezervare = (Rezervare) response.data();
            try{
                client.rezervareReceived(rezervare);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.NEW_REZERVARE;
    }

    private Response readResponse(){
        Response response=null;
        try{
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void initializeConnection() {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received " + response);
                    if(isUpdate((Response) response)){
                        handleUpdate((Response) response);
                    }
                    else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
