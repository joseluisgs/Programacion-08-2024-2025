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
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    implementation("com.h2database:h2:2.3.232")
    // Logger
    implementation("org.lighthousegames:logging:1.5.0")
    implementation("ch.qos.logback:logback-classic:1.5.12")
    implementation("org.mybatis:mybatis:3.5.19")
    
    // Test
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}