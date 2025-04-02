package dev.joseluisgs.config

import org.lighthousegames.logging.logging
import java.util.*


object Config {
    private val logger = logging()
    
    // Valores por defecto
    var databaseUrl: String = "jdbc:h2:mem:estudiantes"
        private set
    var databaseInitTables: Boolean = false
        private set
    var databaseInitData: Boolean = false
        private set
    var storageData: String = "data"
        private set
    
    var locale: String = Locale.getDefault().language
    
    var cacheSize = 10L
    var cacheExpiration = 120L // 2 minutos en segundos
    
    init {
        try {
            logger.debug { "Cargando configuración" }
            val properties = Properties()
            properties.load(ClassLoader.getSystemResourceAsStream("config.properties"))
            databaseUrl = properties.getProperty("database.url", this.databaseUrl)
            databaseInitTables =
                properties.getProperty("database.init.tables", this.databaseInitTables.toString()).toBoolean()
            databaseInitData =
                properties.getProperty("database.init.data", this.databaseInitData.toString()).toBoolean()
            storageData = properties.getProperty("storage.data", this.storageData)
            locale = properties.getProperty("locale", this.locale)
            cacheSize = properties.getProperty("cache.size", this.cacheSize.toString()).toLong()
            cacheExpiration = properties.getProperty("cache.expiration", this.cacheExpiration.toString()).toLong()
            logger.debug { "Configuración cargada correctamente" }
        } catch (e: Exception) {
            logger.error { "Error cargando configuración: ${e.message}" }
            logger.error { "Usando valores por defecto" }
        }
        
    }
}