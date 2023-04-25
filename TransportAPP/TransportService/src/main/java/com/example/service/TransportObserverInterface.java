package com.example.service;

import com.example.domain.Rezervare;

public interface TransportObserverInterface {
    void rezervareReceived(Rezervare rezervare) throws Exception;
}
