package com.segat.trujilloinformado.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.segat.trujilloinformado.model.dto.ReporteDto;
import com.segat.trujilloinformado.model.entity.Reporte;
import com.segat.trujilloinformado.model.enums.TipoProblema;
import com.segat.trujilloinformado.model.enums.EstadoReporte;
import com.segat.trujilloinformado.model.payload.MessageResponse;
import com.segat.trujilloinformado.service.IReporteService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private IReporteService reporteService;

    // Crear reporte con archivos
    @PostMapping("reporte")
    public ResponseEntity<?> create(@RequestParam("descripcion") String descripcion,
                                    @RequestParam("tipoProblema") String tipoProblema,
                                    @RequestParam("photos") List<MultipartFile> photos) {
        try {
            // Procesar tipo de problema
            TipoProblema tipoProblemaEnum = TipoProblema.valueOf(tipoProblema);

            // Crear ReporteDto con los archivos
            ReporteDto reporteDto = new ReporteDto(
                    null,  // idReporte se generará automáticamente
                    descripcion,
                    tipoProblemaEnum,
                    EstadoReporte.PENDIENTE,
                    new byte[0], // Se pasa un byte[] vacío para la imagen
                    photos // Aquí pasas las fotos
            );

            // Guardar el reporte usando el servicio
            Reporte reporteSave = reporteService.save(reporteDto);

            return new ResponseEntity<>(MessageResponse.builder()
                    .objecto(reporteSave) // Devolver el reporte guardado
                    .build(), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(MessageResponse.builder()
                    .mensaje("Error al insertar el reporte: " + e.getMessage())
                    .build(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    // Actualizar reporte
    @PutMapping("reporte/{id}")
    public ResponseEntity<?> update(@RequestParam("descripcion") String descripcion,
                                    @RequestParam("tipoProblema") String tipoProblema,
                                    @RequestParam("photos") List<MultipartFile> photos,
                                    @PathVariable Long id) {
        try {
            if (reporteService.existsById(id)) {
                ReporteDto reporteDto = new ReporteDto(id, descripcion, TipoProblema.valueOf(tipoProblema), EstadoReporte.PENDIENTE, new byte[0], photos);
                Reporte reporteUpdate = reporteService.save(reporteDto);
                return new ResponseEntity<>(MessageResponse.builder()
                        .objecto(reporteUpdate)
                        .build(), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(MessageResponse.builder()
                    .mensaje("El reporte con ID " + id + " no existe.")
                    .build(), HttpStatus.NOT_FOUND);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(MessageResponse.builder()
                    .mensaje("Error al actualizar el reporte: " + e.getMessage())
                    .build(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    // Eliminar reporte
    @DeleteMapping("reporte/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Reporte reporte = reporteService.findById(id);
            if (reporte == null) {
                return new ResponseEntity<>(MessageResponse.builder()
                        .mensaje("El reporte con ID " + id + " no existe.")
                        .build(), HttpStatus.NOT_FOUND);
            }
            reporteService.delete(reporte);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            return new ResponseEntity<>(MessageResponse.builder()
                    .mensaje("Error al eliminar el reporte: " + e.getMessage())
                    .build(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    // Mostrar reporte por ID
    @GetMapping("reporte/{id}")
    public ResponseEntity<?> showById(@PathVariable Long id) {
        Reporte reporte = reporteService.findById(id);
        if (reporte == null) {
            return new ResponseEntity<>(MessageResponse.builder()
                    .mensaje("El reporte con ID " + id + " no existe.")
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(MessageResponse.builder()
                .objecto(reporte)
                .build(), HttpStatus.OK);
    }

    // Mostrar todos los reportes
    @GetMapping("reportes")
    public ResponseEntity<?> showAll() {
        List<Reporte> reportes = reporteService.findAll();
        if (reportes.isEmpty()) {
            return new ResponseEntity<>(MessageResponse.builder()
                    .mensaje("No hay reportes registrados.")
                    .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MessageResponse.builder()
                .objecto(reportes)
                .build(), HttpStatus.OK);
    }
}
