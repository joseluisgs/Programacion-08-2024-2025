package dev.joseluisgs.database

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.koin.core.annotation.Property
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.io.File

/**
 * Clase que gestiona la conexión con la base de datos
 * y la creación de tablas y carga de datos
 */

@Singleton
class JdbiManager(
    private val databaseUrl: String,
    private val databaseInitData: Boolean,
    private val databaseInitTables: Boolean
) {
    private val logger = logging()
    
    
    // La creo de forma lazy para que se cree cuando se necesite
    val jdbi by lazy { Jdbi.create(databaseUrl) }
    
    
    init {
        logger.debug { "Inicializando JdbiManager" }
        // instalamos los plugins
        jdbi.installPlugin(KotlinPlugin()) // Necesario para trabajar con Kotlin
        jdbi.installPlugin(SqlObjectPlugin()) // Necesario para trabajar con SQLObject, DAO
        
        
        if (databaseInitTables) {
            logger.debug { "Creando tablas" }
            // Leemos el fichero de resources
            executeSqlScriptFromResources("tables.sql")
        }
        if (databaseInitData) {
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

@Singleton
fun provideDatabaseManager(
    @Property("database.url") databaseUrl: String = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    @Property("database.init.data") databaseInitData: String = "false",
    @Property("database.init.tables") databaseInitTables: String = "false"
): Jdbi {
    val logger = logging()
    logger.debug { "Proporcionando instancia de JdbiManager" }
    return JdbiManager(
        databaseUrl,
        databaseInitData.toBoolean(),
        databaseInitTables.toBoolean()
    ).jdbi
}
