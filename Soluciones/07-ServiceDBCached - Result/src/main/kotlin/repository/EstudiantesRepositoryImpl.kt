package dev.joseluisgs.repositories

import dev.joseluisgs.dao.EstudiantesDao
import dev.joseluisgs.mappers.toEntity
import dev.joseluisgs.mappers.toModel
import dev.joseluisgs.models.Estudiante
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

typealias EstudianteEntity = Map<String, Any>


class EstudiantesRepositoryImpl(
    private val dao: EstudiantesDao
) : EstudiantesRepository {
    private val logger = logging()
    
    override fun findAll(): List<Estudiante> {
        logger.debug { "Buscando todos los estudiantes" }
        
        return dao.findAll().map { it.toModel() }
    }
    
    
    override fun findById(id: Int): Estudiante? {
        logger.debug { "Buscando estudiante por id: $id" }
        
        return dao.findById(id)?.toModel()
        
    }
    
    override fun save(item: Estudiante): Estudiante {
        logger.debug { "Guardando estudiante: $item" }
        
        val timeStamp = LocalDateTime.now()
        
        val toSave = item.copy(createdAt = timeStamp, updatedAt = timeStamp)
        
        val id = dao.save(toSave.toEntity())
        
        return item.copy(id = id)
    }
    
    override fun update(id: Int, item: Estudiante): Estudiante? {
        logger.debug { "Actualizando estudiante por id: $id" }
        var estudiante: Estudiante? = this.findById(id)
        val timeStamp = LocalDateTime.now()
        
        if (estudiante != null) {
            val toUpdate = item.copy(id = id, updatedAt = timeStamp)
            val result = dao.update(toUpdate.toEntity())
            if (result > 0) {
                estudiante = toUpdate
            }
        }
        return estudiante
    }
    
    override fun delete(id: Int): Estudiante? {
        logger.debug { "Borrando estudiante por id: $id" }
        val estudiante: Estudiante? = findById(id)
        if (estudiante != null) {
            val result = dao.delete(id)
            if (result == 0) {
                logger.error { "No se ha podido borrar el estudiante con id: $id" }
            }
        }
        return estudiante
    }
    
    override fun findAllPaginated(page: Int, size: Int): List<Estudiante> {
        logger.debug { "Buscando todos los estudiantes paginados, página: $page, tamaño: $size" }
        val myPage = if (page < 1) 0 else page - 1 // Cuidado con la paginación y los índices para test
        val mySize = if (size < 1) 10 else size
        
        return dao.findAll(mySize, myPage * mySize).map { it.toModel() }
    }
    
    override fun findByNombre(nombre: String): List<Estudiante> {
        logger.debug { "Buscando estudiantes por nombre: $nombre" }
        
        return dao.findByName("%$nombre%").map { it.toModel() }
        
    }
    
    override fun findByCalificacion(calificacion: Double): List<Estudiante> {
        logger.debug { "Buscando estudiantes por calificación: $calificacion" }
        
        return dao.findByCalificacion(calificacion).map { it.toModel() }
    }
    
    override fun findByRepetidor(repetidor: Boolean): List<Estudiante> {
        logger.debug { "Buscando estudiantes por repetidor: $repetidor" }
        
        return dao.findByRepetidor(repetidor).map { it.toModel() }
    }
    
    override fun mediaCalificaciones(): Double {
        logger.debug { "Calculando media de calificaciones" }
        
        return dao.avgCalificacion()
    }
    
    override fun maximaCalificacion(): Double {
        logger.debug { "Calculando máxima calificación" }
        
        return dao.maxCalificacion()
    }
    
    override fun minimaCalificacion(): Double {
        logger.debug { "Calculando mínima calificación" }
        
        return dao.minCalificacion()
    }
    
    override fun numAprobados(): Int {
        logger.debug { "Calculando número de aprobados" }
        
        return dao.countAprobados()
    }
    
    override fun numSuspensos(): Int {
        logger.debug { "Calculando número de suspensos" }
        
        return dao.countSuspensos()
    }
}