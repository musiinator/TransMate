package com.example.objectprotocol;

import com.example.domain.Rezervare;

public class RezervareRequest implements Request {
    private final Rezervare rezervare;

    public RezervareRequest(Rezervare rezervare) {
        this.rezervare = rezervare;
    }
    public Rezervare getRezervare() {
        return rezervare;
    }
}
