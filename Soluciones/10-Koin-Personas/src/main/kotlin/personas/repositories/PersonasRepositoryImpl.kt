package personas.repositories

import org.lighthousegames.logging.logging
import personas.models.Persona
import personas.services.PersonasStorage
import java.util.*

private val logger = logging()

class PersonasRepositoryImpl(
    private val storage: PersonasStorage
) : PersonasRepository {
    private val id = UUID.randomUUID()

    override fun save(entity: Persona): Persona {
        logger.debug { "PersonasRepository.save() -->$entity" }
        println("PersonasRepository.save() -->$entity")
        return storage.save(entity)
    }

    override fun toString() = "PersonasRepository(storage=$storage, id='$id')"

}
