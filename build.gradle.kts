plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "de.api.dsb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    jcenter()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.seleniumhq.selenium:selenium-java:4.9.0")
    implementation("dev.kord:kord-core:0.9.0")
    implementation("io.github.bonigarcia:webdrivermanager:5.0.3")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}