plugins {
    kotlin("jvm") version "2.1.10"
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
    
    // Result: ROP
    implementation("com.michael-bull.kotlin-result:kotlin-result:2.0.1")
    
    
    // Test
    testImplementation("io.mockk:mockk:1.13.16") // Mocks
    testImplementation(kotlin("test")) // Test JUnit
    testImplementation("org.jdbi:jdbi3-testing:3.48.0") // Testing JDBI
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}