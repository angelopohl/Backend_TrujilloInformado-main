package com.segat.trujilloinformado.model.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.segat.trujilloinformado.model.enums.EstadoReporte;
import com.segat.trujilloinformado.model.enums.TipoProblema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReporteDto implements Serializable {
    private Long idReporte;
    private String descripcion;
    private TipoProblema tipoProblema;
    private EstadoReporte estado;
    private byte[] imagen;  // O puede ser Optional<byte[]>
    private List<MultipartFile> fotos;

    // Constructor con 6 parámetros
    public ReporteDto(Long idReporte, String descripcion, TipoProblema tipoProblema, EstadoReporte estado, byte[] imagen, List<MultipartFile> fotos) {
    this.idReporte = idReporte;
    this.descripcion = descripcion;
    this.tipoProblema = tipoProblema;
    this.estado = estado;
    this.imagen = imagen;
    this.fotos = fotos;
}


}



