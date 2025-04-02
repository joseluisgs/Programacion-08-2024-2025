package dev.joseluisgs.database

import dev.joseluisgs.config.Config
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.lighthousegames.logging.logging
import java.io.File


class JdbiManager {
    private val logger = logging()
    
    // La creo de forma lazy para que se cree cuando se necesite
    val jdbi: Jdbi by lazy {
        logger.debug { "Creando JDBI con URL: ${Config.databaseUrl}" }
        Jdbi.create(Config.databaseUrl)
    }
    
    init {
        // instalamos los plugins
        jdbi.installPlugin(KotlinPlugin()) // Necesario para trabajar con Kotlin
        jdbi.installPlugin(SqlObjectPlugin()) // Necesario para trabajar con SQLObject
        
        
        if (Config.databaseInitTables) {
            logger.debug { "Creando tablas" }
            // Leemos el fichero de resources
            executeSqlScriptFromResources("tables.sql")
        }
        if (Config.databaseInitData) {
            logger.debug { "Cargando datos" }
            executeSqlScriptFromResources("data.sql")
        }
    }
    
    
    /**
     * Ejecuta un script SQL
     * @param scriptFilePath Ruta del fichero
     */
    fun executeSqlScript(scriptFilePath: String) {
        logger.debug { "Ejecutando script SQL: $scriptFilePath" }
        val script = File(scriptFilePath).readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }
    
    /**
     * Ejectua un script SQL desde un recurso
     * @param resourcePath Ruta del recursos
     */
    fun executeSqlScriptFromResources(resourcePath: String) {
        logger.debug { "Ejecutando script SQL desde recursos: $resourcePath" }
        val inputStream = ClassLoader.getSystemResourceAsStream(resourcePath)?.bufferedReader()!!
        val script = inputStream.readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }
}
