plugins {
    kotlin("jvm") version "2.1.10"
    id("com.google.devtools.ksp") version "2.1.10-1.0.31" // KSP instalar la versión que coincida con la de kotlin
}

group = "dev.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Base de datos a usar, driver
    implementation("com.h2database:h2:2.3.232") // H2
    
    // JDBI
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Necesario para JDBI la reflexión
    implementation("org.jdbi:jdbi3-core:3.48.0") // Core
    implementation("org.jdbi:jdbi3-sqlobject:3.48.0") // SQL Object para DAO
    implementation("org.jdbi:jdbi3-kotlin:3.48.0") // Kotlin extension
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.48.0") // Kotlin extension para SQL Object
    
    
    // Logger
    implementation("org.lighthousegames:logging:1.5.0")
    implementation("ch.qos.logback:logback-classic:1.5.12")
    
    // Cache Caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    
    // serialización
    // Librería para serialización en JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    
    // Resuly: ROP
    implementation("com.michael-bull.kotlin-result:kotlin-result:2.0.1")
    
    // Koin DI
    implementation("io.insert-koin:koin-core:4.0.2") // Core de Koin
    implementation("io.insert-koin:koin-annotations:2.0.0") // Anotaciones de Koin
    ksp("io.insert-koin:koin-ksp-compiler:2.0.0") // KSP Compiler de Koin
    
    
    // Test
    testImplementation("io.mockk:mockk:1.13.16") // Mocks
    testImplementation(kotlin("test")) // Test JUnit
    //testImplementation("org.jdbi:jdbi3-testing:3.48.0") // Testing JDBI, no lo usaremos
    testImplementation("io.insert-koin:koin-test-junit5:4.0.2") // Para test con JUnit5
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

