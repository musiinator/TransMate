package com.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cursa extends Entity<Long> {
    private String destinatie;
    private LocalDateTime dataOraPlecare;
    private Integer nrLocuriDisponibile;

    public Cursa(String destinatie, LocalDateTime dataOraPlecare, Integer nrLocuriDisponibile) {
        this.destinatie = destinatie;
        this.dataOraPlecare = dataOraPlecare;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public LocalDateTime getDataOraPlecare() {
        return dataOraPlecare;
    }

    public void setDataOraPlecare(LocalDateTime dataOraPlecare) {
        this.dataOraPlecare = dataOraPlecare;
    }

    public Integer getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }

    public void setNrLocuriDisponibile(Integer nrLocuriDisponibile) {
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cursa cursa = (Cursa) o;
        return Objects.equals(destinatie, cursa.destinatie) && Objects.equals(dataOraPlecare, cursa.dataOraPlecare) && Objects.equals(nrLocuriDisponibile, cursa.nrLocuriDisponibile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinatie, dataOraPlecare, nrLocuriDisponibile);
    }

    @Override
    public String toString() {
        return "Cursa{" +
                "destinatie='" + destinatie + '\'' +
                ", dataOraPlecare=" + dataOraPlecare +
                ", nrLocuriDisponibile=" + nrLocuriDisponibile +
                '}';
    }
}
