package com.segat.trujilloinformado.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.segat.trujilloinformado.model.dao.ReporteDao;
import com.segat.trujilloinformado.model.dto.ReporteDto;  // Importa MultipartFile
import com.segat.trujilloinformado.model.entity.Reporte;  // Importa File
import com.segat.trujilloinformado.model.enums.EstadoReporte;  // Importa IOException
import com.segat.trujilloinformado.service.IReporteService;

@Service
public class ReporteServiceImpl implements IReporteService {

    @Autowired
    private ReporteDao reporteDao;

   @Transactional
@Override
public Reporte save(ReporteDto reporteDto) {
    // Crear un objeto Reporte
    Reporte reporte = Reporte.builder()
            .descripcion(reporteDto.getDescripcion())
            .tipoProblema(reporteDto.getTipoProblema())
            .estado(EstadoReporte.PENDIENTE)  // Inicializar con estado PENDIENTE
            .build();

    // Guardar fotos si es necesario
    if (reporteDto.getFotos() != null && !reporteDto.getFotos().isEmpty()) {
        for (MultipartFile photo : reporteDto.getFotos()) {  
            try {
                // Convertir la foto a un byte[] y guardarlo en el reporte
                byte[] imageBytes = photo.getBytes();  // Convertir la imagen en bytes
                reporte.setImagen(imageBytes);  // Establecer la imagen como bytes en el reporte
            } catch (IOException e) {
                e.printStackTrace();  // Manejo de error
            }
        }
    }

    // Guardar el reporte en la base de datos
    return reporteDao.save(reporte);
}



    @Transactional(readOnly = true)
    @Override
    public Reporte findById(Long id) {
        return reporteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Reporte reporte) {
        reporteDao.delete(reporte);
    }

    @Override
    public boolean existsById(Long id) {
        return reporteDao.existsById(id);
    }

    @Override
    public List<Reporte> findAll() {
        return (List<Reporte>) reporteDao.findAll();
    }
}
