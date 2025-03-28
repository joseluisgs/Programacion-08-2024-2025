package koin.personas

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import personas.controllers.PersonasController
import personas.repositories.PersonasRepository
import personas.repositories.PersonasRepositoryImpl
import personas.services.PersonasStorage
import personas.services.PersonasStorageDataBase
import personas.services.PersonasStorageFile

// Modulo que indica cómo son las dependencias.
val PersonasModule = module {

    // Hay dos formas de repositorio de personas. y le decimos que al principio (para probar)
    // Como trabajamos con interfaces, debemos indicar cuál es la implementación que queremos usar
    // para eso usamos el named
    // llama a PersonasStorageDataBase() o PersonasStorageFile() según se necesite inyectar
    single<PersonasStorage>(named("DataBaseStorage")) { PersonasStorageDataBase() }
    single<PersonasStorage>(named("FileStorage")) { PersonasStorageFile() }

    // O por defecto, si no se especifica, se usará el DataBaseStorage
    single<PersonasStorage> { PersonasStorageDataBase() }

    // Creamos los posibles repositorios de personas, de acuerdo a la implementación de storage
    // Para eso usamos el named
    // llamaremos a PersonasRepositoryImpl() según se necesite inyectar
    single<PersonasRepository>(named("DataBaseRepository")) { PersonasRepositoryImpl(get(named("DataBaseStorage"))) }
    single<PersonasRepository>(named("FileRepository")) { PersonasRepositoryImpl(get(named("FileStorage"))) }


    // O por defecto, si no se especifica, se usará el DataBaseRepository
    factory<PersonasRepository> { PersonasRepositoryImpl(get()) }

    // Creamos el controlador, a demas podemos inyectar cuando queramos cualquier propiedad
    // para saber con que estamos trabajando, por ejemplo, la url del servidor
    // de nuevo usamos el named para indicar cuál es el repositorio que queremos usar
    // llamaremos a PersonasController() según se necesite inyectar
    single(named("PersonasDataBaseController")) {
        PersonasController(
            get(named("DataBaseRepository")),
            getProperty("server_url")
        )
    }
    single(named("PersonasFileController")) {
        PersonasController(
            get(named("FileRepository")),
            getProperty("server_url")
        )
    }

    // O por defecto
    single { PersonasController(get(), getProperty("server_url", "no_encontrada")) }

}

// Otra manera

val PersonasModule2 = module {
    singleOf(::PersonasStorageDataBase) {
        // definition options
        named("DataBaseStorage")
        bind<PersonasStorage>()
        //createdAtStart()
    }
    singleOf(::PersonasStorageFile) {
        // definition options
        named("FileStorage")
        bind<PersonasStorage>()
        //createdAtStart()
    }

    singleOf(::PersonasStorageDataBase) {
        // definition options
        bind<PersonasStorage>()
    }

    singleOf(::PersonasRepositoryImpl) {
        // definition options
        named("DataBaseRepository")
        bind<PersonasRepository>()
        binds(listOf(PersonasStorageDataBase::class))
        //createdAtStart()
    }

    singleOf(::PersonasRepositoryImpl) {
        // definition options
        named("FileRepository")
        bind<PersonasRepository>()
        binds(listOf(PersonasStorageFile::class))
        //createdAtStart()
    }

    singleOf(::PersonasRepositoryImpl) {
        // definition options
        bind<PersonasRepository>()
        binds(listOf(PersonasStorageDataBase::class))
        //createdAtStart()
    }

    singleOf(::PersonasController) {
        // definition options
        named("PersonasDataBaseController")
        binds(listOf(PersonasRepositoryImpl::class))

        //createdAtStart()
    }
}