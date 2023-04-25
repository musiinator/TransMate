package com.example.service;

import java.io.FileReader;
import java.io.IOException;

public class Properties1 {

    public java.util.Properties getProperties(java.util.Properties props){
        try{
            props.load(new FileReader("D:\\Anul 2\\semestrul2\\MPP\\mpp-proiect-java-musiinator\\lab3-javafx\\src\\main\\bd.config"));
        }catch (IOException e){
            System.out.println("Cannot find bd.config " + e);
        }
        return props;
    }
}
