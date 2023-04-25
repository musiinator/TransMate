package com.example.rpcprotocol;

import com.example.domain.Cursa;
import com.example.domain.Rezervare;
import com.example.domain.User;
import com.example.objectprotocol.NewRezervareResponse;
import com.example.service.TransportObserverInterface;
import com.example.service.TransportServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class TransportClientRpcReflectionWorker implements Runnable, TransportObserverInterface {

    private TransportServiceInterface server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public TransportClientRpcReflectionWorker(TransportServiceInterface server, Socket connection){
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
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
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;/*
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }*/
        try {
            if(request.type() == RequestType.LOGIN){
                System.out.println("Login request ...");
                server.login((User) request.data(), this);
                return okResponse;
            }
            if (request.type() == RequestType.GET_USER_BY_USERNAME) {
                System.out.println("Get user by username request ...");
                server.findUserByUsername((String) request.data());
                response = new Response.Builder().type(ResponseType.TAKE_USER_BY_USERNAME).data(server.findUserByUsername((String) request.data())).build();
            }
            if (request.type() == RequestType.GET_CURSE) {
                System.out.println("Get curse request ...");
                response = new Response.Builder().type(ResponseType.TAKE_CURSE).data(server.findAllCursa()).build();
            }
            if (request.type() == RequestType.GET_REZERVARI) {
                System.out.println("Get rezervari request ...");
                response = new Response.Builder().type(ResponseType.TAKE_REZERVARI).data(server.findAllRezervare()).build();
            }
            if (request.type() == RequestType.GET_REZERVARI_BY_ID_CURSA) {
                server.findRezervareByIdCursa((Long) request.data());
                System.out.println("Get rezervari by id cursa request ...");
                response = new Response.Builder().type(ResponseType.TAKE_REZERVARI_BY_ID_CURSA).data(server.findRezervareByIdCursa((Long) request.data())).build();
            }
            if (request.type() == RequestType.SAVE_REZERVARE) {
                System.out.println("Save rezervare request ...");
                server.saveRezervare((Rezervare) request.data());
                response = new Response.Builder().type(ResponseType.OK).build();
            }
            if (request.type() == RequestType.UPDATE_CURSA) {
                System.out.println("Update cursa request ...");
                server.updateCursa((Cursa) request.data());
                response = new Response.Builder().type(ResponseType.OK).build();
            }
            if (request.type() == RequestType.LOGOUT) {
                System.out.println("Logout request...");
                server.logout((User) request.data(), this);
                response = new Response.Builder().type(ResponseType.OK).build();
                connected = false;
            }

        }catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void rezervareReceived(Rezervare rezervare) throws Exception {
        Response resp = new Response.Builder().type(ResponseType.NEW_REZERVARE).data(rezervare).build();
        System.out.println("Rezervare received " + resp);
        try{
            sendResponse(resp);
        } catch (IOException e){
            throw new Exception("Error sending object "+e);
        }
    }

}
