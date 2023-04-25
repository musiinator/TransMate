package com.example.utils;

import com.example.rpcprotocol.TransportClientRpcReflectionWorker;
import com.example.service.TransportServiceInterface;

import java.net.Socket;

public class TransportRpcConcurrentServer extends AbsConcurrentServer{

    private TransportServiceInterface transportServer;
    public TransportRpcConcurrentServer(int port, TransportServiceInterface transportServer) {
        super(port);
        this.transportServer = transportServer;
        System.out.println("Transport- TransportRpcConcurrentServer");
    }
    @Override
    protected Thread createWorker(Socket client) {
        TransportClientRpcReflectionWorker worker=new TransportClientRpcReflectionWorker(transportServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop() {
        System.out.println("Stopping services ...");
    }
}
