plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    id("com.google.devtools.ksp") version "2.1.10-1.0.31"
    jacoco
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
    
    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    
    // Cache Caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    
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
    
    // Jacoco
    implementation("org.jacoco:org.jacoco.core:0.8.12") // Jacoco
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
kotlin {
    jvmToolchain(21)
}


tasks.jacocoTestReport {
    dependsOn(tasks.test) // Asegúrate de que los tests se ejecuten antes del reporte
    
    reports {
        xml.required.set(true) // Genera el reporte XML
        csv.required.set(false) // No generes el reporte CSV
        html.required.set(true) // Genera el reporte HTML
        // html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml")) // Ubicación del reporte HTML
    }
    
    
    // Exclusiones de cobertura
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    // Añade aquí los patrones de exclusión, en base a lo que necesites excluir
                    "**/model/**",
                    "**/exceptions/**",
                    "**/*Main*.*"
                )
            }
        })
    )
}

// Tarea adicional para verificación de cobertura mínima
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal() // 80% de cobertura mínima
            }
        }
    }
}

// Tarea personalizada para ejecutar tests y verificación
tasks.register("testCoverage") {
    group = "verification"
    description = "Runs the unit tests with coverage"
    
    dependsOn(
        ":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification"
    )
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "dev.joseluisgs.MainKt", // Clase principal
        )
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE // Evitamos duplicados
    
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}
