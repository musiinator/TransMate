package com.example.domain;

import java.util.Objects;

public class RezervareDTO {
    private Long idRezervareDTO;
    private String numeClientDTO;
    private Integer nrLocDTO;
    private Long idCursaDTO;

    public RezervareDTO(Long idRezervareDTO, String numeClientDTO, Integer nrLocDTO, Long idCursaDTO) {
        this.idRezervareDTO = idRezervareDTO;
        this.numeClientDTO = numeClientDTO;
        this.nrLocDTO = nrLocDTO;
        this.idCursaDTO = idCursaDTO;
    }

    public Long getIdRezervareDTO() {
        return idRezervareDTO;
    }

    public void setIdRezervareDTO(Long idRezervareDTO) {
        this.idRezervareDTO = idRezervareDTO;
    }

    public String getNumeClientDTO() {
        return numeClientDTO;
    }

    public void setNumeClientDTO(String numeClientDTO) {
        this.numeClientDTO = numeClientDTO;
    }

    public Integer getNrLocDTO() {
        return nrLocDTO;
    }

    public void setNrLocDTO(Integer nrLocDTO) {
        this.nrLocDTO = nrLocDTO;
    }

    public Long getIdCursaDTO() {
        return idCursaDTO;
    }

    public void setIdCursaDTO(Long idCursaDTO) {
        this.idCursaDTO = idCursaDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RezervareDTO that = (RezervareDTO) o;
        return Objects.equals(idRezervareDTO, that.idRezervareDTO) && Objects.equals(numeClientDTO, that.numeClientDTO) && Objects.equals(nrLocDTO, that.nrLocDTO) && Objects.equals(idCursaDTO, that.idCursaDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRezervareDTO, numeClientDTO, nrLocDTO, idCursaDTO);
    }

    @Override
    public String toString() {
        return "RezervareDTO{" +
                "idRezervareDTO=" + idRezervareDTO +
                ", numeClientDTO='" + numeClientDTO + '\'' +
                ", nrLocDTO=" + nrLocDTO +
                ", idCursaDTO=" + idCursaDTO +
                '}';
    }
}
