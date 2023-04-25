package com.example.domain;

import java.util.Objects;

public class CursaDTO {
    private Long idCursaDTO;
    private String destinatieDTO;
    private String dataOraPlecareDTO;
    private Integer nrLocuriDisponibileDTO;

    public CursaDTO(Long idCursaDTO, String destinatieDTO, String dataOraPlecareDTO, Integer nrLocuriDisponibileDTO) {
        this.idCursaDTO = idCursaDTO;
        this.destinatieDTO = destinatieDTO;
        this.dataOraPlecareDTO = dataOraPlecareDTO;
        this.nrLocuriDisponibileDTO = nrLocuriDisponibileDTO;
    }

    public Long getIdCursaDTO() {
        return idCursaDTO;
    }

    public void setIdCursaDTO(Long idCursaDTO) {
        this.idCursaDTO = idCursaDTO;
    }

    public String getDestinatieDTO() {
        return destinatieDTO;
    }

    public void setDestinatieDTO(String destinatieDTO) {
        this.destinatieDTO = destinatieDTO;
    }

    public String getDataOraPlecareDTO() {
        return dataOraPlecareDTO;
    }

    public void setDataOraPlecareDTO(String dataOraPlecareDTO) {
        this.dataOraPlecareDTO = dataOraPlecareDTO;
    }

    public Integer getNrLocuriDisponibileDTO() {
        return nrLocuriDisponibileDTO;
    }

    public void setNrLocuriDisponibileDTO(Integer nrLocuriDisponibileDTO) {
        this.nrLocuriDisponibileDTO = nrLocuriDisponibileDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CursaDTO cursaDTO = (CursaDTO) o;
        return Objects.equals(idCursaDTO, cursaDTO.idCursaDTO) && Objects.equals(destinatieDTO, cursaDTO.destinatieDTO) && Objects.equals(dataOraPlecareDTO, cursaDTO.dataOraPlecareDTO) && Objects.equals(nrLocuriDisponibileDTO, cursaDTO.nrLocuriDisponibileDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCursaDTO, destinatieDTO, dataOraPlecareDTO, nrLocuriDisponibileDTO);
    }

    @Override
    public String toString() {
        return "CursaDTO{" +
                "idCursaDTO=" + idCursaDTO +
                ", destinatieDTO='" + destinatieDTO + '\'' +
                ", dataOraPlecareDTO=" + dataOraPlecareDTO +
                ", nrLocuriDisponibileDTO=" + nrLocuriDisponibileDTO +
                '}';
    }
}
