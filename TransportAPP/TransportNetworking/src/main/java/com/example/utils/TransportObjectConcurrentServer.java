package com.example.utils;

import com.example.objectprotocol.TransportClientObjectWorker;
import com.example.service.TransportServiceInterface;

import java.net.Socket;

public class TransportObjectConcurrentServer extends AbsConcurrentServer{

    private TransportServiceInterface transportServer;

    public TransportObjectConcurrentServer(int port, TransportServiceInterface transportServer) {
        super(port);
        this.transportServer = transportServer;
        System.out.println("Transport- TransportObjectConcurrentServer");
    }
    @Override
    protected Thread createWorker(Socket client) {
        TransportClientObjectWorker worker=new TransportClientObjectWorker(transportServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
