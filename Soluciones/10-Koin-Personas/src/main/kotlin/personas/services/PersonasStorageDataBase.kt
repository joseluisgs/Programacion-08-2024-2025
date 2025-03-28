package personas.services

import org.lighthousegames.logging.logging
import personas.models.Persona
import java.util.*

private val logger = logging()

class PersonasStorageDataBase : PersonasStorage {
    private val id = UUID.randomUUID()

    override fun save(item: Persona): Persona {
        logger.debug { "PersonasStorageDataBase.save() --> $item" }
        println("PersonasStorageDataBase.save() --> $item")
        return item
    }

    override fun toString() = "PersonaDataBaseStorage(id='$id')"
}
