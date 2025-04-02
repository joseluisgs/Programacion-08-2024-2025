package dev.joseluisgs.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.joseluisgs.models.Estudiante
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Property
import org.lighthousegames.logging.logging
import java.util.concurrent.TimeUnit

@Factory
fun provideEstudiantesCache(
    @Property("cache.capacity") _capacity: String = "5",
    @Property("cache.duration") _duration: String = "60"
): Cache<Int, Estudiante> {
    val logger = logging()
    logger.debug { "Inicializando Cache con capacidad $_capacity y duración $_duration s" }
    val capacity = _capacity.toLong()
    val duration = _duration.toLong()
    // Configura la caché con un tamaño máximo de 5 y expiración de 1 minuto
    return Caffeine.newBuilder()
        .maximumSize(capacity) // LRU con máximo de 5 elementos
        .expireAfterWrite(duration, TimeUnit.SECONDS) // Expira 1 minuto después de la escritura, 60 segundos
        .build()
}