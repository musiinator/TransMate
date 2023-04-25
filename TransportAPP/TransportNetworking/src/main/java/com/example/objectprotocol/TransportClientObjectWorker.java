package com.example.objectprotocol;

import com.example.domain.Rezervare;
import com.example.service.TransportObserverInterface;
import com.example.service.TransportServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TransportClientObjectWorker implements Runnable, TransportObserverInterface {
    private TransportServiceInterface server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public TransportClientObjectWorker (TransportServiceInterface server, Socket connection){
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request = input.readObject();
                Object response = handleRequest((Request) request);
                if (response != null){
                    sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request){
        Response response = null;
        if (request instanceof RezervareRequest){
            System.out.println("Rezervare request ...");
            RezervareRequest rezervareRequest = (RezervareRequest) request;
            try{
                server.saveRezervare(rezervareRequest.getRezervare());
            } catch (Exception e){
                connected = false;
                e.printStackTrace();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void rezervareReceived(Rezervare rezervare) throws Exception {
        try{
            sendResponse(new NewRezervareResponse(rezervare));
        } catch (Exception e){
            throw new Exception("Sending error: " + e);
        }
    }


}
