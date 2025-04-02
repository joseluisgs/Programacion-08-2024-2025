package database

import dev.joseluisgs.config.Config
import org.apache.ibatis.jdbc.ScriptRunner
import org.lighthousegames.logging.logging
import java.io.PrintWriter
import java.io.Reader
import java.sql.Connection
import java.sql.DriverManager


object DataBaseManager : AutoCloseable {
    private val logger = logging()
    
    // Conexión con la base de datos
    var connection: Connection? = null
        private set
    
    /**
     * Inicializamos la base de datos
     */
    init {
        initDatabase()
    }
    
    fun initDatabase() {
        // Iniciamos la base de datos
        initConexion()
        if (Config.databaseInitTables) {
            initTablas()
        }
        if (Config.databaseInitData) {
            initData()
        }
        close()
    }
    
    /**
     * Inicializamos los datos de la base de datos en caso de que se haya configurado
     */
    private fun initData() {
        logger.debug { "Iniciando carga de datos" }
        try {
            val data = ClassLoader.getSystemResourceAsStream("data.sql")?.bufferedReader()!!
            scriptRunner(data, true)
            logger.debug { "Datos cargados" }
        } catch (e: Exception) {
            logger.error { "Error al cargar los datos: ${e.message}" }
        }
    }
    
    /**
     * Inicializamos las tablas de la base de datos en caso de que se haya configurado
     */
    
    private fun initTablas() {
        logger.debug { "Creando tablas" }
        try {
            val tablas = ClassLoader.getSystemResourceAsStream("tables.sql")?.bufferedReader()!!
            scriptRunner(tablas, true)
            logger.debug { "Tabla estudiantes creada" }
        } catch (e: Exception) {
            logger.error { "Error al crear las tablas: ${e.message}" }
        }
    }
    
    /**
     * Inicializamos la conexión con la base de datos
     */
    
    private fun initConexion() {
        logger.debug { "Iniciando conexión con la base de datos en ${Config.databaseUrl}" }
        // Inicializamos la base de datos
        logger.debug { "Iniciando conexión con la base de datos" }
        if (connection == null || connection!!.isClosed) {
            connection = DriverManager.getConnection(Config.databaseUrl)
        }
        logger.debug { "Conexión con la base de datos inicializada" }
    }
    
    
    /**
     * Cerramos la conexión con la base de datos
     */
    override fun close() {
        logger.debug { "Cerrando conexión con la base de datos" }
        if (!connection!!.isClosed) {
            connection!!.close()
        }
        logger.debug { "Conexión con la base de datos cerrada" }
    }
    
    /**
     * Función para usar la base de datos y cerrarla al finalizar la operación
     */
    
    fun <T> use(block: (DataBaseManager) -> T) {
        initConexion()
        this.connection.use { block(this) }
        close()
    }
    
    /**
     * Función para ejecutar un script SQL en la base de datos
     */
    
    private fun scriptRunner(reader: Reader, logWriter: Boolean = false) {
        logger.debug { "Ejecutando script SQL con log: $logWriter" }
        val sr = ScriptRunner(connection)
        sr.setLogWriter(if (logWriter) PrintWriter(System.out) else null)
        sr.runScript(reader)
    }
}