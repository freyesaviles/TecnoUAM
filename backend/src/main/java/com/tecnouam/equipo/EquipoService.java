
package com.tecnouam.equipo;

import com.tecnouam.dto.EquipoRequestDTO;
import com.tecnouam.dto.EquipoResponseDTO;
import com.tecnouam.prestamo.EstadoPrestamo;
import com.tecnouam.prestamo.PrestamoRepository;
import com.tecnouam.shared.api.ConflictoNegocioException;
import com.tecnouam.shared.api.RecursoNoEncontradoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final PrestamoRepository prestamoRepository;

    // Inyección de dependencias
    public EquipoService(
            EquipoRepository equipoRepository,
            PrestamoRepository prestamoRepository) {

        this.equipoRepository = equipoRepository;
        this.prestamoRepository = prestamoRepository;
    }

    // Convertir una entidad en DTO de respuesta
    private EquipoResponseDTO convertirDTO(Equipo equipo) {
        return new EquipoResponseDTO(
                equipo.getId(),
                equipo.getCodigoPatrimonial(),
                equipo.getNombre(),
                equipo.getTipo(),
                equipo.getEstado()
        );
    }

    // 1. Listar todos los equipos
    public List<EquipoResponseDTO> listarEquipos() {

        return equipoRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // 2. Buscar equipo por ID
    public EquipoResponseDTO buscarPorId(Long id) {

        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe un equipo con ID: " + id
                        )
                );

        return convertirDTO(equipo);
    }

    // 3. Registrar nuevo equipo
    @Transactional
    public EquipoResponseDTO crearEquipo(EquipoRequestDTO datos) {

        String codigo = datos.codigoPatrimonial().trim();
        String nombre = datos.nombre().trim();
        String tipo = datos.tipo().trim();

        // Validar código duplicado
        if (equipoRepository.existsByCodigoPatrimonial(codigo)) {
            throw new ConflictoNegocioException(
                    "Ya existe un equipo con el código: " + codigo
            );
        }

        // El equipo nuevo inicia DISPONIBLE
        Equipo equipo = new Equipo(codigo, nombre, tipo);

        Equipo guardado = equipoRepository.save(equipo);

        return convertirDTO(guardado);
    }

    // 4. Actualizar equipo
    @Transactional
    public EquipoResponseDTO actualizarEquipo(
            Long id, EquipoRequestDTO datos) {

        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe un equipo con ID: " + id
                        )
                );

        String codigo = datos.codigoPatrimonial().trim();

        // Verificar que el código no pertenezca a otro equipo
        if (equipoRepository
                .existsByCodigoPatrimonialAndIdNot(codigo, id)) {

            throw new ConflictoNegocioException(
                    "El código patrimonial ya pertenece a otro equipo"
            );
        }

        equipo.setCodigoPatrimonial(codigo);
        equipo.setNombre(datos.nombre().trim());
        equipo.setTipo(datos.tipo().trim());

        Equipo actualizado = equipoRepository.save(equipo);

        return convertirDTO(actualizado);
    }

    // 5. Eliminar equipo
    @Transactional
    public void eliminarEquipo(Long id) {

        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe un equipo con ID: " + id
                        )
                );

        // No se puede eliminar si tiene préstamos activos
        boolean tienePrestamosActivos =
                prestamoRepository.existsByEquipo_IdAndEstado(
                        id, EstadoPrestamo.ACTIVO
                );

        if (tienePrestamosActivos) {
            throw new ConflictoNegocioException(
                    "No se puede eliminar el equipo porque tiene préstamos activos"
            );
        }

        equipoRepository.delete(equipo);
    }
}
