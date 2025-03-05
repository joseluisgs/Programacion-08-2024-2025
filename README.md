# Programación - 08 Programación con Bases de Datos

Tema 08 Programación con Bases de Datos. 1DAW. Curso 2023/2024.

![imagen](https://raw.githubusercontent.com/joseluisgs/Programacion-00-2022-2023/master/images/programacion.png)

- [Programación - 08 Programación con Bases de Datos](#programación---08-programación-con-bases-de-datos)
  - [Contenidos](#contenidos)
  - [Bases de datos Relacionales](#bases-de-datos-relacionales)
  - [El Desfase Objeto-Relacional](#el-desfase-objeto-relacional)
  - [Gestión de integridad por código o delegación en la base de datos](#gestión-de-integridad-por-código-o-delegación-en-la-base-de-datos)
  - [Autonuméricos vs UUID](#autonuméricos-vs-uuid)
  - [Programando con Base de Datos](#programando-con-base-de-datos)
    - [Conexión a la base de datos](#conexión-a-la-base-de-datos)
    - [Desconexión de la base de datos](#desconexión-de-la-base-de-datos)
    - [Consultas a la base de datos](#consultas-a-la-base-de-datos)
    - [Actualizaciones a la base de datos](#actualizaciones-a-la-base-de-datos)
    - [Transacciones](#transacciones)
  - [CRUD](#crud)
    - [Create](#create)
    - [Read](#read)
    - [Update](#update)
    - [Delete](#delete)
  - [SqlDeLight](#sqldelight)
  - [Introducción a Railway Oriented Programming](#introducción-a-railway-oriented-programming)
  - [Autor](#autor)
    - [Contacto](#contacto)
  - [Licencia de uso](#licencia-de-uso)


## Contenidos
1. Bases de datos Relacionales
2. El Desfase Objeto-Relacional
3. Gestión de integridad por código o delegación en la base de datos
4. Autonuméricos vs UUID
5. Programando con Base de Datos
6. CRUD
7. SqlDeLight
8. Introducción a Railway Oriented Programming


## Bases de datos Relacionales
Las bases de datos relacionales son un tipo de base de datos que se basa en el modelo relacional. Este modelo se basa en la idea de que los datos se almacenan en tablas, que se relacionan entre sí mediante claves primarias y claves foráneas. 
- Clave primaria: Es un campo que identifica de forma única a cada registro de una tabla.
- Clave foránea: Es un campo que identifica a un registro de otra tabla.

Cada tabla tiene una clave primaria, que es un campo que identifica de forma única a cada registro de la tabla. Además, cada tabla puede tener una o más claves foráneas, que son campos que identifican a un registro de otra tabla.

Los datos de cada columna de una tabla se almacenan en un formato determinado, que puede ser numérico, de texto, de fecha, etc. Además, cada columna tiene un nombre que la identifica. Dependiendo del tipo de dato que almacena, una columna puede tener restricciones, como por ejemplo que no puede ser nula, que tiene que ser única, etc. Además los tipos de datos existentes dependen del Sistema Gestor de Bases de Datos (SGBD) que se esté usando.

Podemos decir que una tabla es una colección de registros, donde cada registro es una colección de campos.

Para trabajar con ellas usamos un lenguaje de consulta estructurado (SQL) que nos permite crear, modificar y consultar las tablas de una base de datos.

```sql
CREATE TABLE tablaX (
    campo1 tipo1,
    campo2 tipo2,
    campo3 tipo3,
    ...
    campoN tipoN REFERENCES tablaY(campoY),
    PRIMARY KEY (campo1)
);
```

## El Desfase Objeto-Relacional
A la hora de programar con bases de datos, nos encontramos con un problema: el modelo relacional no es compatible con el modelo orientado a objetos. Esto se debe a que el modelo relacional está basado en tablas, mientras que el modelo orientado a objetos está basado en objetos. Por tanto, no podemos usar directamente las tablas de una base de datos relacional en un programa orientado a objetos, a parte podemos tener problemas con los tipos de datos y que estos no sean compatibles. Por lo tanto, se puede decir, que el modelo relacional y el modelo orientado a objetos no son compatibles entre sí de manera directa y que hay que hacer un esfuerzo para que estos dos modelos se puedan usar juntos.

Para ello deberemos trabajar relaciones y tips y realizar el mapa de objetos a tablas/entidades y viceversa cada vez que queramos pasar de un modelo a otro: de un objeto a un registro de una tabla y de un registro de una tabla a un objeto.

Esto se puede solucionar con mapeadores automáticos o ORM (Object Relational Mapping) que nos permiten trabajar con objetos en lugar de tablas y registros. Estos mapeadores se encargan de traducir las consultas que se hacen a la base de datos a sentencias SQL y viceversa.

```sql
CREATE TABLE Persona (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(50),
    apellidos VARCHAR(100),
    edad INTEGER
);
```

```kotlin
data class Persona(
    var id: Long,
    var nombre: String,
    var apellidos: String,
    var edad: Int
)
```

## Gestión de integridad por código o delegación en la base de datos
Debido al desfase entre el modelo relacional y el modelo orientado a objetos, podemos tener problemas con la gestión de la integridad de los datos. Esto deberá estudiarse dependiendo de si la base de datos es compartida y las reglas de negocio de una aplicación que haga uso de ella no tiene que coincidir con las del resto de aplicaciones que hagan uso de esta base de datos.

Por ejemplo, si tenemos una tabla de personas y otra de direcciones (y esta base de datos se comparte), podemos tener un problema de integridad referencial si eliminamos una persona y no eliminamos sus direcciones (esto puede ser válido e unos problemas pero en otros no). Para solucionar este problema podemos hacerlo de dos formas:
- Delegando la gestión de la integridad en la base de datos: En este caso, la base de datos se encargará de eliminar las direcciones de la persona que se está eliminando. Para ello, la tabla de direcciones tendrá una clave foránea que apunte a la tabla de personas y usaremos los mecanismos de propagación en cascada. De esta forma, si se elimina una persona, se eliminarán también sus direcciones directamente en la base de datos.
- Gestión de la integridad por código: En este caso, la aplicación se encargará de eliminar las direcciones de la persona que se está eliminando. Para ello, la aplicación tendrá que consultar las direcciones de la persona que se está eliminando y eliminarlas una a una. De esta manera podemos personalizar los casos en los que se elimina una persona y no sus direcciones, cosa que en el caso anterior no se puede hacer.

Esto es un problema que se debe estudiar caso por caso y dependiendo de la aplicación que se esté desarrollando y el ciclo de vida de la misma y de la base de datos y del conjunto de aplicaciones que hagan uso de ella y de las reglas de negocio de cada una de ellas.

## Autonuméricos vs UUID
Otro de los problemas que nos encontramos a la hora de programar con bases de datos es la gestión de los identificadores de los registros. En el modelo relacional, los identificadores de los registros se almacenan en una columna de la tabla y se llaman claves primarias. Estos identificadores son únicos y no pueden repetirse.

De nuevo tenemos dos enfoques, de si queremos que la base de datos se encargue de generar los identificadores o si queremos que la aplicación se encargue de generarlos. Ambas soluciones tiene sus pros y contras.

Si la base de datos se encarga de generar los identificadores, tendremos que usar un tipo de dato que permita valores únicos y que no se repitan. Para ello se usa valores autoincrementados, que es un tipo de dato entero sin signo que se autoincrementa cada vez que se inserta un nuevo registro. Esto provoca, que si tenemos que insertar un dato, no sabremos el identificador que se le va a asignar hasta que no se haya insertado el registro en la base de datos. Esto puede ser un problema si tenemos que insertar un registro y luego insertar otros registros que tengan como clave foránea el identificador del registro que acabamos de insertar, pues deberemos consultarlo para poder usarlo o simplemente completar el objeto que lo desconocía.

Si la aplicación se encarga de generar los identificadores, tendremos que usar un tipo de dato que permita valores únicos y que no se repitan. Una de las opciones es hacer uso de UUID (Universally Unique Identifier) que es un identificador único que se genera de manera aleatoria. Esto nos permite generar un identificador único para cada registro sin tener que esperar a que se inserte el registro en la base de datos y este nos lo devuelva lo consultamos. Esto es una ventaja, pero también es una desventaja, ya que si tenemos que insertar un registro y luego insertar otros registros que tengan como clave foránea el identificador del registro que acabamos de insertar, tendremos que guardar el identificador del registro que acabamos de insertar para poder usarlo como clave foránea en los registros que vamos a insertar después.

## Programando con Base de Datos
Para programar con bases de datos en el universo de la JVM usaremos JDBC (Java Database Connectivity) que es una API que nos permite acceder a bases de datos relacionales desde Java. Esta API está formada por interfaces y clases que nos permiten realizar operaciones con bases de datos relacionales. Estas operaciones se realizan mediante sentencias SQL.

Para ello usaremos un driver que es un componente que permite que una aplicación pueda conectarse a una base de datos. Los drivers son librerías que se encargan de traducir las llamadas a la API JDBC a las llamadas que necesita el SGBD para realizar las operaciones. Cada SGBD tiene su propio driver: MySQL, PostgreSQL, SQLite, etc.

```gradle
dependencies {
    // SQLite
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")

    // H2
    implementation("com.h2database:h2:2.1.214")

    // MariaDB
    implementation("org.mariadb.jdbc:mariadb-java-client:3.1.3")

}
```

### Conexión a la base de datos
Para conectarnos a una base de datos, primero debemos crear un objeto de tipo Connection que nos permitirá realizar las operaciones con la base de datos. Primero deberemos seleccionar el driver adecuado según el SGDB. Para ello usaremos el método getConnection de la clase DriverManager, que es una clase que nos permite obtener conexiones a bases de datos. Este método recibe como parámetros la URL de la base de datos, el usuario y la contraseña. La URL de la base de datos es un String que contiene la información necesaria para conectarse a la base de datos. Esta URL tiene un formato específico para cada SGBD. Por ejemplo, para SQLite la URL es jdbc:sqlite:nombreFichero.db, para MySQL es jdbc:mysql://servidor:puerto/nombreBD, etc.

```kotlin
val url = "jdbc:sqlite:ejemplo.db"
val conexion = DriverManager.getConnection(url)
```

Recuerda que si quieres usar bases de datos en memoria debes informarte de cómo se configura la cadena de conexión, a veces consiste en añadir un parámetro al final de la cadena de conexión:

```properties
database.url=jdbc:sqlite:file:test?mode=memory&cache=shared
database.urljdbc:h2:mem:test;DB_CLOSE_DELAY=-1
```


### Desconexión de la base de datos
Para desconectarnos de la base de datos, simplemente debemos cerrar la conexión. Para ello usaremos el método close de la clase Connection. Lo haremos siempre al terminar de trabajar con la base de datos.

```kotlin
conexion.close()
```
### Consultas a la base de datos
Para realizar consultas a la base de datos, primero debemos crear un objeto de tipo Statement que nos permitirá realizar las consultas. Para ello usaremos el método createStatement de la clase Connection. Este método nos devuelve un objeto de tipo Statement que nos permitirá realizar las consultas.

En nuestro caso usaremos siempre PreparedStatement que es una subclase de Statement que nos permite realizar consultas parametrizadas. Esto nos permite reutilizar la misma consulta con diferentes parámetros. Además, nos ayuda a que nuestras consultas sean más seguras contra ataques. Para ello usaremos el método prepareStatement de la clase Connection. Este método recibe como parámetro una cadena con la consulta SQL. Este método nos devuelve un objeto de tipo PreparedStatement que es un objeto que nos permite realizar consultas parametrizadas.

```kotlin
val sentencia = conexion.prepareStatement("SELECT * FROM Persona WHERE id = ?")
sentencia.setLong(1, 1) // Establecemos el primer parámetro de la consulta el valor 1
```

El objeto Statement tiene varios métodos para realizar consultas a la base de datos. El método executeQuery nos permite realizar consultas que devuelven resultados. Este método recibe como parámetro una cadena con la consulta SQL. Este método nos devuelve un objeto de tipo ResultSet que es un objeto que contiene los resultados de la consulta.

```kotlin
val resultado = sentencia.executeQuery()
```

El objeto ResultSet tiene varios métodos para recorrer los resultados de la consulta. El método next de la clase ResultSet nos devuelve true si hay más resultados y false si no hay más resultados que recorrer y avanza al siguiente registro de la consulta, de esta manera nos permite recorrer todos los resultados de la consulta. El método getXXX nos permite recuperar el valor de una columna del registro actual. Este método recibe como parámetro el nombre de la columna o el índice de la columna. Este método nos devuelve el valor de la columna del registro actual.

```kotlin
while (resultado.next()) {
    // Recuperamos los valores por nombre de columna
    val id = resultado.getLong("id")
    val nombre = resultado.getString("nombre")
    val apellidos = resultado.getString("apellidos")
    val edad = resultado.getInt("edad")
    // Mostramos los valores por pantalla
    println("ID: $id, Nombre: $nombre, Apellidos: $apellidos, Edad: $edad")
}
```

Aquí es donde entra en juego el momento donde mapeamos tipos, columnas a objetos y viceversa. Para ello usaremos un mapeador de objetos a tablas y viceversa.

```kotlin
val sentencia = conexion.prepareStatement("SELECT * FROM Persona WHERE id = ?")
sentencia.setLong(1, 1)
val resultado = sentencia.executeQuery()
while (resultado.next()) {
    // Recuperamos los valores por nombre de columna
    val id = resultado.getLong("id")
    val nombre = resultado.getString("nombre")
    val apellidos = resultado.getString("apellidos")
    val edad = resultado.getInt("edad")
    // Creamos el objeto persona
    val persona = Persona(id, nombre, apellidos, edad)
    // Mostramos los valores por pantalla
    println(persona)
}
```

### Actualizaciones a la base de datos
Para actualizar procederemos de la misma manera anterior, pero usaremos el método executeUpdate en lugar de executeQuery. Este método recibe como parámetro una cadena con la consulta SQL. Este método nos devuelve un entero con el número de filas afectadas por la consulta.

```kotlin
val sentencia = conexion.prepareStatement("UPDATE Persona SET nombre = ?, apellidos = ? WHERE id = ?")
sentencia.setString(1, "José Luis")
sentencia.setString(2, "González Sánchez")
sentencia.setLong(3, 1)
val filas = sentencia.executeUpdate()
println("Filas actualizadas: $filas")

val sentencia = conexion.prepareStatement("DELETE FROM Persona WHERE id = ?")
sentencia.setLong(1, 1)
val filas = sentencia.executeUpdate()
println("Filas eliminadas: $filas")
```

### Transacciones
Las transacciones nos permiten realizar varias operaciones en una base de datos de forma segura. De esta manera se pueden ejecutar varias operaciones de forma segura y como si fueran una sola. Si alguna de las operaciones falla, todas las operaciones se deshacen. Para ello usaremos el método setAutoCommit de la clase Connection. Este método recibe como parámetro un booleano que indica si queremos que las operaciones se realicen de forma automática o no. Si el parámetro es true, las operaciones se realizarán de forma automática. Si el parámetro es false, las operaciones se realizarán de forma manual. Por defecto, las operaciones se realizan de forma automática.

Para validar una transacción usaremos el método commit de la clase Connection. Este método no recibe ningún parámetro. Este método nos permite validar una transacción.

```kotlin
conexion.autoCommit = false

var sentencia = conexion.prepareStatement("UPDATE Persona SET nombre = ?, apellidos = ? WHERE id = ?")
sentencia.setString(1, "José Luis")
sentencia.setString(2, "González Sánchez")
sentencia.setLong(3, 1)
val filas = sentencia.executeUpdate()

sentencia = conexion.prepareStatement("DELETE FROM Persona WHERE id = ?")
sentencia.setLong(1, 1)
val filas = sentencia.executeUpdate()

println("Filas actualizadas: $filas")

conexion.commit()
```

Para deshacer una transacción usaremos el método rollback de la clase Connection. Este método no recibe ningún parámetro. Este método nos permite deshacer una transacción.

```kotlin
conexion.autoCommit = false

var sentencia = conexion.prepareStatement("UPDATE Persona SET nombre = ?, apellidos = ? WHERE id = ?")
sentencia.setString(1, "José Luis")
sentencia.setString(2, "González Sánchez")
sentencia.setLong(3, 1)
val filas = sentencia.executeUpdate()

sentencia = conexion.prepareStatement("DELETE FROM Persona WHERE id = ?")
sentencia.setLong(1, 1)
val filas = sentencia.executeUpdate()

println("Filas actualizadas: $filas")

conexion.rollback()
```

## CRUD
El CRUD es un acrónimo que significa Create, Read, Update y Delete. Estas son las cuatro operaciones básicas que podemos realizar sobre una base de datos. En este apartado vamos a ver como realizar estas operaciones con JDBC.

### Create
Para realizar una operación de creación de datos usaremos el método executeUpdate de la clase Statement. Este método recibe como parámetro una cadena con la consulta insert SQL. Este método nos devuelve un entero con el número de filas afectadas por la consulta.

```kotlin
val sentencia = conexion.prepareStatement("INSERT INTO Persona (nombre, apellidos, edad) VALUES (?, ?, ?)")
sentencia.setString(1, "Pedro")
sentencia.setString(2, "González")
sentencia.setInt(3, 25)
val filas = sentencia.executeUpdate()
println("Filas insertadas: $filas")
```

Algunas veces trabajamos con bases de datos que generan automáticamente el valor de la clave primaria. En este caso, no podemos indicar el valor de la clave primaria. En este caso, usaremos el método getGeneratedKeys de la clase Statement. Este método no recibe ningún parámetro. Este método nos devuelve un objeto ResultSet con las claves generadas.

```kotlin
val sentencia = conexion.prepareStatement("INSERT INTO Persona (nombre, apellidos, edad) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
sentencia.setString(1, "Pedro")
sentencia.setString(2, "González")
sentencia.setInt(3, 25)
val filas = sentencia.executeUpdate()
println("Filas insertadas: $filas")
val claves = sentencia.generatedKeys
if (claves.next()) {
    val id = claves.getLong(1)
    println("Clave generada: $id")
}
```

### Read
Para realizar una operación de lectura de datos usaremos el método executeQuery de la clase Statement. Este método recibe como parámetro una cadena con la consulta select SQL. Este método nos devuelve un objeto ResultSet con los resultados de la consulta. En este caso podemos buscar todos o uno en concreto. Recordemos que el método next de la clase ResultSet nos permite avanzar al siguiente registro.

```kotlin
val sentencia = conexion.prepareStatement("SELECT * FROM Persona")
val resultado = sentencia.executeQuery()
while (resultado.next()) {
    val id = resultado.getLong("id")
    val nombre = resultado.getString("nombre")
    val apellidos = resultado.getString("apellidos")
    val edad = resultado.getInt("edad")
    val persona = Persona(id, nombre, apellidos, edad)
    println(persona)
}
```
```kotlin
val sentencia = conexion.prepareStatement("SELECT * FROM Persona WHERE id = ?")
sentencia.setLong(1, 1)
val resultado = sentencia.executeQuery()
if (resultado.next()) {
    val id = resultado.getLong("id")
    val nombre = resultado.getString("nombre")
    val apellidos = resultado.getString("apellidos")
    val edad = resultado.getInt("edad")
    val persona = Persona(id, nombre, apellidos, edad)
    println(persona)
}
```

### Update
Para realizar una operación de actualización de datos usaremos el método executeUpdate de la clase Statement. Este método recibe como parámetro una cadena con la consulta update SQL. Este método nos devuelve un entero con el número de filas afectadas por la consulta.

```kotlin
val sentencia = conexion.prepareStatement("UPDATE Persona SET nombre = ?, apellidos = ? WHERE id = ?")
sentencia.setString(1, "José Luis")
sentencia.setString(2, "González Sánchez")
sentencia.setLong(3, 1)
val filas = sentencia.executeUpdate()
println("Filas actualizadas: $filas")
```

### Delete
Para realizar una operación de eliminación de datos usaremos el método executeUpdate de la clase Statement. Este método recibe como parámetro una cadena con la consulta delete SQL. Este método nos devuelve un entero con el número de filas afectadas por la consulta.

```kotlin
val sentencia = conexion.prepareStatement("DELETE FROM Persona WHERE id = ?")
sentencia.setLong(1, 1)
val filas = sentencia.executeUpdate()
println("Filas eliminadas: $filas")
```

## SqlDeLight
[SqlDeLight](https://cashapp.github.io/sqldelight/2.0.0-alpha05/) es una librería que nos permite generar código Kotlin para realizar [operaciones CRUD sobre una base de datos](https://cashapp.github.io/sqldelight/2.0.0-alpha05/jvm_sqlite/). Para usar esta librería debemos añadir el plugin de SqlDelight al fichero build.gradle del módulo, cargar la dependencia de SqlDelight y añadir la configuración de SqlDelight.

Recuerda que si quieres usar bases de datos en memoria debes informarte de cómo se configura la cadena de conexión, a veces consiste en añadir un parámetro al final de la cadena de conexión:

```properties
database.url=jdbc:sqlite:file:test?mode=memory&cache=shared
database.urljdbc:h2:mem:test;DB_CLOSE_DELAY=-1
```

```gradle 
plugins {
    kotlin("jvm") version "1.8.0"
    application
    // Plugin de SqlDeLight
    id("app.cash.sqldelight") version "2.0.0-alpha05"
}

// ....
dependencies {
   // SqlDeLight para SQLite
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0-alpha05")
}
// Configuración del plugin de SqlDeLight
sqldelight {
    databases {
        // Nombre de la base de datos que le vamos a dar, puede ser cualquiera
        create("AppDatabase") {
            // Paquete donde se generan las clases, puede ser cualquiera
            packageName.set("dev.joseluisgs.database")
        }
    }
}
```

Una vez configurado el plugin de SqlDeLight, podemos crear el fichero de definición de la base de datos. Este fichero debe tener la extensión .sq y debe estar en una carpeta dentro de la ruta src/main/sqldelight. En este fichero definiremos las tablas de la base de datos y las consultas que queramos realizar sobre ellas.

```sql
CREATE TABLE Persona (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    edad INTEGER NOT NULL
);

selectAll:
SELECT * FROM Persona;

selectById:
SELECT * FROM Persona WHERE id = ?;

insert:
INSERT INTO Persona (nombre, apellidos, edad) VALUES (?, ?, ?);

update:
UPDATE Persona SET nombre = ?, apellidos = ? WHERE id = ?;

delete:
DELETE FROM Persona WHERE id = ?;
```

Una vez creado el fichero de definición de la base de datos, podemos generar el código Kotlin para realizar las operaciones CRUD. Para generar el código debemos ejecutar la tarea de gradle generateAppDatabaseInterface. Este comando genera el código Kotlin en la carpeta build/generated/source/sqldelight.

```bash
./gradlew generateAppDatabaseInterface
```

Una vez generado el código Kotlin podemos usarlo en nuestro proyecto. Para ello debemos crear una instancia de la clase AppDatabase y usar sus métodos para realizar las operaciones CRUD.

```kotlin
val driver = JdbcSqliteDriver(connectionUrl)
// Creamos la base de datos
AppDatabase.Schema.create(driver)
// Creamos una instancia de la clase AppDatabase
val database = AppDatabase(driver)
// Creamos una instancia de la clase PersonaQueries
val personaQueries = database.personaQueries
```

Para realizar por ejemplo una operación de inserción de datos usaremos el método insert de la clase PersonaQueries. Este método recibe como parámetro los valores de los campos de la tabla. Este método nos devuelve un entero con el número de filas afectadas por la consulta.

```kotlin
var filas= personaQueries.insert("José Luis", "González Sánchez", 35)
println("Filas insertadas: $filas")

filas = personaQueries.update("José Luis", "González Sánchez", 1)
println("Filas actualizadas: $filas")

filas = personaQueries.delete(1)
println("Filas eliminadas: $filas")

```

Para realizar una operación de lectura de datos usaremos el método selectAll de la clase PersonaQueries. Este método nos devuelve un objeto Query de la librería SqlDeLight. Este objeto nos permite iterar sobre los resultados de la consulta. Podemos usar el método executeAsList para obtener una lista con los resultados de la consulta y el método executeAsOne para obtener un único resultado de la consulta. Además, podemos mapear los resultados de la consulta a un objeto de nuestra aplicación.

```kotlin
val resultado = personaQueries.selectAll().executeAsList().map {
    Persona(it.id, it.nombre, it.apellidos, it.edad)
}
println(resultado)
```

```kotlin
val resultado = personaQueries.selectById(1).executeAsOne().let {
    Persona(it.id, it.nombre, it.apellidos, it.edad)
}
println(resultado)
```

## Introducción a Railway Oriented Programming
[Railway Oriented Programming](https://fsharpforfunandprofit.com/rop/) es un patrón de diseño que nos permite escribir código más limpio y mantenible. Este patrón se basa en el concepto de [programación funcional](https://es.wikipedia.org/wiki/Programaci%C3%B3n_funcional) y en el uso de [monadas](https://es.wikipedia.org/wiki/Monada_(programaci%C3%B3n_funcional)). 

La base de este patrón es ir encadenando funciones que devuelven un valor. En lugar de usar excepciones, se usan valores de retorno para indicar si una operación ha tenido éxito o no. De esta manera el resultado de una función pasa al siguiente. Si todo se resuelve correctamente, tendremos el resultado exitoso (camino feliz). En caso de que alguna función devuelva un error, el resto de funciones no se ejecutan y se devuelve el error (camino infeliz).

La filosofía se puede ver en esta imagen: 

![imagen](https://user-images.githubusercontent.com/24237865/229043283-3584b713-42a4-4491-a26c-a06b68b57f0d.jpg).

Este patrón será muy util para encadenar las operaciones de repositorios, controladores y bases de datos. De esta manera, si una operación falla, el resto de operaciones no se ejecutan y se devuelve el error. Este error nos puede servir para devolver una respuesta al cliente o para realizar alguna otra operación, como se puede ver en esta imagen 

![esta imagen](https://www.netmentor.es/Imagen/65a1e2de-b4ed-4ca6-9ac7-6ae6fca01723.jpg).

```kotlin
// Creamos los Errores que vamos a usar para Producto
sealed class ProductoError(val message: String) {
    class NoEncontrado(message: String) : ProductoError("ERROR: Producto no encontrado: $message")
    class NoGuardado(message: String) : ProductoError("ERROR: Producto no guardado: $message")
    class NoValido(message: String) : ProductoError("ERROR: Producto no válido: $message")
}
```
```kotlin
// Podemos tener una función que nos devuelva un producto o un error al validarlo
fun Producto.validar(): Result<Producto, ProductoError> {
    if (nombre.isBlank()) return Err(ProductoError.NoValido("Nombre no puede estar vacío"))
    if (precio < 0) return Err(ProductoError.NoValido("Precio debe ser mayor o igual a 0"))
    if (cantidad < 0) return Err(ProductoError.NoValido("Cantidad debe ser mayor o igual a 0"))
    return Ok(this)
}
```

```kotlin
// Podemos tener en nuestro controlador una función que nos devuelva un producto o un error al buscarlo
fun findById(id: Long): Result<Producto, ProductoError> {
    return productosRepository.findById(id)?.let {
        Ok(it)
    } ?: Err(ProductoError.NoEncontrado("Producto con $id no existe en almacenamiento"))
}

// Podemos tener en nuestro controlador una función que nos devuelva un producto o un error al actualizarlo
// Como podemos ver se puede encadenar las funciones de forma muy limpia
// si saltara el error, no se ejecutaría el resto de funciones y se devolvería el error
fun update(producto: Producto): Result<Producto, ProductoError> {
    // Lo buscamos
    return findById(producto.id)
        // lo validamos
        .andThen { it.validar() }
        // lo actualizamos
        .andThen { Ok(productosRepository.update(it)) }
}
```

```kotlin
// Finalmente en nuestra vista o lugar donde se use el controlador
println()
println("Actualizar producto con id 1")
productosController.update(
    Producto(
        id = 1,
        nombre = "Producto 1 Updated",
        precio = 100.0,
        cantidad = 10,
        disponible = false
    )
)
// Si se hace con éxito podremos sacar el valor
.onSuccess { println(it.toLocalString()) }
// Si se produce un error podremos sacar el error
.onFailure { println(it.message) }

// Tambien podemos actoar el tipo de error por si queremos hacer algo diferente
// Por ejemplo desahacer la operación o mostrar un mensaje diferente
productosController.update(
    Producto(
        id = 1,
        nombre = "Producto 1 Updated",
        precio = 100.0,
        cantidad = 10,
        disponible = false
    )
).onFailure { error ->
    when (error) {
        is ProductoError.NoEncontrado -> println("No se ha encontrado el producto")
        is ProductoError.NoValido -> println("El producto no es válido")
        is ProductoError.NoGuardado -> println("El producto no se ha podido guardar")
    }
}
```
## Autor

Codificado con :sparkling_heart: por [José Luis González Sánchez](https://twitter.com/JoseLuisGS_)

[![Twitter](https://img.shields.io/twitter/follow/JoseLuisGS_?style=social)](https://twitter.com/JoseLuisGS_)
[![GitHub](https://img.shields.io/github/followers/joseluisgs?style=social)](https://github.com/joseluisgs)
[![GitHub](https://img.shields.io/github/stars/joseluisgs?style=social)](https://github.com/joseluisgs)

### Contacto

<p>
  Cualquier cosa que necesites házmelo saber por si puedo ayudarte 💬.
</p>
<p>
 <a href="https://joseluisgs.dev" target="_blank">
        <img src="https://joseluisgs.github.io/img/favicon.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://github.com/joseluisgs" target="_blank">
        <img src="https://distreau.com/github.svg" 
    height="30">
    </a> &nbsp;&nbsp;
        <a href="https://twitter.com/JoseLuisGS_" target="_blank">
        <img src="https://i.imgur.com/U4Uiaef.png" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://www.linkedin.com/in/joseluisgonsan" target="_blank">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/LinkedIn_logo_initials.png/768px-LinkedIn_logo_initials.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://g.dev/joseluisgs" target="_blank">
        <img loading="lazy" src="https://googlediscovery.com/wp-content/uploads/google-developers.png" 
    height="30">
    </a>  &nbsp;&nbsp;
<a href="https://www.youtube.com/@joseluisgs" target="_blank">
        <img loading="lazy" src="https://upload.wikimedia.org/wikipedia/commons/e/ef/Youtube_logo.png" 
    height="30">
    </a>  
</p>

## Licencia de uso

Este repositorio y todo su contenido está licenciado bajo licencia **Creative Commons**, si desea saber más, vea
la [LICENSE](https://joseluisgs.dev/docs/license/). Por favor si compartes, usas o modificas este proyecto cita a su
autor, y usa las mismas condiciones para su uso docente, formativo o educativo y no comercial.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Licencia de Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">
JoseLuisGS</span>
by <a xmlns:cc="http://creativecommons.org/ns#" href="https://joseluisgs.dev/" property="cc:attributionName" rel="cc:attributionURL">
José Luis González Sánchez</a> is licensed under
a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons
Reconocimiento-NoComercial-CompartirIgual 4.0 Internacional License</a>.<br />Creado a partir de la obra
en <a xmlns:dct="http://purl.org/dc/terms/" href="https://github.com/joseluisgs" rel="dct:source">https://github.com/joseluisgs</a>.
