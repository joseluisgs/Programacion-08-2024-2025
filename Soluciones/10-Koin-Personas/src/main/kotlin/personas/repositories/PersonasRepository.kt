package personas.repositories

import personas.models.Persona
import java.util.*

interface PersonasRepository : CrudRepository<Persona, UUID>