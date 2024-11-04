plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
//    kotlin("kapt")
}

group = "kgp.liivm"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")

    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.5.0")
    implementation("com.google.guava:guava:33.3.0-jre")

    implementation("org.springframework:spring-jdbc")

    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    implementation("com.querydsl:querydsl-apt:5.1.0:jakarta")

    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.56.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.56.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

noArg {
    annotation("jakarta.persistence.Entity")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
