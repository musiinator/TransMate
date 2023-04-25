package com.example.objectprotocol;

import com.example.domain.Rezervare;

public class NewRezervareResponse implements Response{
    private Rezervare rezervare;

    public NewRezervareResponse(Rezervare rezervare) {
        this.rezervare = rezervare;
    }

    public Rezervare getRezervare() {
        return rezervare;
    }
}
