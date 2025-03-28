package personas.controllers

import org.lighthousegames.logging.logging
import personas.models.Persona
import personas.repositories.PersonasRepository
import java.util.*

private val logger = logging()

// Controlador de Personas, que se encarga de gestionar las personas
class PersonasController(
    private val personaRepository: PersonasRepository,
    private val serverUrl: String
) {
    private val id = UUID.randomUUID()

    fun save(persona: Persona): Persona {
        logger.debug { "PersonasController.save() --> $persona" }
        println("PersonasController.save() --> $persona")
        return personaRepository.save(persona)
    }

    override fun toString() =
        "PersonaController(personaRepository=$personaRepository, id='$id', serverUrl='$serverUrl')"
}
