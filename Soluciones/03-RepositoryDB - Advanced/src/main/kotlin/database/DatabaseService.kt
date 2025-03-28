package database

import dev.joseluisgs.config.Config
import dev.joseluisgs.database.DatabaseManager
import org.apache.ibatis.jdbc.ScriptRunner
import org.lighthousegames.logging.logging
import java.io.PrintWriter
import java.io.Reader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

/**
 * Clase que gestiona la base de datos
 * @property connection Conexión con la base de datos
 */

class DatabaseService : DatabaseManager {
    private val logger = logging()
    
    // Conexión con la base de datos
    private var connection: Connection? = null
    
    /**
     * Inicializamos la base de datos
     */
    init {
        // Iniciamos la base de datos en base a su conexion
        initConexion()
        if (Config.databaseInitTables) {
            initTablas()
        }
        if (Config.databaseInitData) {
            initData()
        }
        
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
    /*override fun close() {
        logger.debug { "Cerrando conexión con la base de datos" }
        if (!connection!!.isClosed) {
            connection!!.close()
        }
        logger.debug { "Conexión con la base de datos cerrada" }
    }*/
    
    
    /**
     * Función para ejecutar un script SQL en la base de datos
     */
    
    private fun scriptRunner(reader: Reader, logWriter: Boolean = false) {
        logger.debug { "Ejecutando script SQL con log: $logWriter" }
        val sr = ScriptRunner(connection)
        sr.setLogWriter(if (logWriter) PrintWriter(System.out) else null)
        sr.runScript(reader)
    }
    
    
    override fun select(sql: String, vararg params: Any): List<Map<String, Any>> {
        logger.debug { "Ejecutando consulta: $sql" }
        // Creamos una lista mutable para guardar los resultados, como un mapa de columnas
        val result = mutableListOf<Map<String, Any>>()
        // Preparamos la consulta y la ejecutamos
        initConexion()
        connection!!.prepareStatement(sql).use { statement ->
            setParameters(statement, params) // Pasamos los parámetros
            val resultSet = statement.executeQuery() // Ejecutamos la consulta y obtenemos el resultado
            
            // Recorremos el resultado y lo guardamos en la lista
            while (resultSet.next()) {
                val row = mutableMapOf<String, Any>() // Creamos un mapa mutable para guardar la fila
                
                // Recorremos las columnas y las guardamos en el mapa, sabiendo sus metadatos
                for (i in 1..resultSet.metaData.columnCount) {
                    row[resultSet.metaData.getColumnName(i)] = resultSet.getObject(i)
                }
                // Añadimos la fila a la lista de resultados
                result.add(row)
            }
        }
        
        logger.debug { "Consulta finalizada con ${result.size} resultados" }
        return result // Devolvemos la lista de resultados
    }
    
    override fun insert(sql: String, vararg params: Any): Int {
        logger.debug { "Ejecutando inserción: $sql" }
        var result = 0
        initConexion()
        connection!!.prepareStatement(sql).use { statement ->
            setParameters(statement, params)
            result = statement.executeUpdate()
        }
        
        logger.debug { "Inserción finalizada con $result filas afectadas" }
        return result
    }
    
    override fun insertAndGetId(query: String, vararg params: Any): Any {
        logger.debug { "Ejecutando inserción y obteniendo ID: $query" }
        var result: Any = 0 // Puede ser un UUID tambien, pero lo normal es que sea un Long
        // Esta vez usamos RETURN_GENERATED_KEYS para obtener las claves generadas
        initConexion()
        connection!!.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
            setParameters(statement, params)
            statement.executeUpdate()
            val resultSet = statement.generatedKeys // Obtenemos las claves generadas
            if (resultSet.next()) { // Si hay claves generadas
                result = resultSet.getObject(1) // Obtenemos la primera clave
            }
        }
        logger.debug { "Inserción finalizada con ID: $result" }
        return result
    }
    
    override fun update(query: String, vararg params: Any): Int {
        logger.debug { "Ejecutando actualización: $query" }
        var result = 0
        initConexion()
        connection!!.prepareStatement(query).use { statement ->
            setParameters(statement, params)
            result = statement.executeUpdate()
        }
        logger.debug { "Actualización finalizada con $result filas afectadas" }
        return result
    }
    
    override fun delete(query: String, vararg params: Any): Int {
        logger.debug { "Ejecutando borrado: $query" }
        var result = 0
        initConexion()
        connection!!.prepareStatement(query).use { statement ->
            setParameters(statement, params)
            result = statement.executeUpdate()
        }
        logger.debug { "Borrado finalizado con $result filas afectadas" }
        return result
    }
    
    /**
     * Pasamos los parámetros a la consulta
     */
    private fun setParameters(statement: PreparedStatement, params: Array<out Any>) {
        logger.debug { "Pasando parámetros a la consulta: ${params.joinToString()}" }
        for ((index, param) in params.withIndex()) {
            statement.setObject(index + 1, param)
        }
    }
}