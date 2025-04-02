package personas.services

import org.lighthousegames.logging.logging
import personas.models.Persona
import java.util.*

private val logger = logging()

class PersonasStorageFile : PersonasStorage {
    private val id = UUID.randomUUID()

    override fun save(item: Persona): Persona {
        logger.debug { "PersonasStorageFile.save() --> $item" }
        println("PersonasStorageFile.save() --> $item")
        return item
    }

    override fun toString() = "PersonaDataBaseStorage(id='$id')"
}
