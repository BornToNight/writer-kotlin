val kotlinVer: String by project // 2.1.0
val jacksonKotlinVer: String by project // 2.18.2
val springBootStarterVer: String by project // 3.4.2
val springAdminVer: String by project // 3.4.1
val postgreSQLVer: String by project // 42.7.3
val jacksonVer: String by project // 2.17.0
val micrometerPrometheusVer: String by project // 1.12.4
val kafkaVer: String by project // 3.2.0
val junitVer: String by project // 1.11.0-M2
val logstashEncoderVer: String by project // 8.0

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    id("org.springframework.boot") version "3.4.2-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.pachan"
version = "1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
}

extra["springBootAdminVersion"] = "3.4.1"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVer")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVer")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootStarterVer")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootStarterVer")
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springBootStarterVer")
    implementation("de.codecentric:spring-boot-admin-starter-client:$springAdminVer")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerPrometheusVer")
    implementation("org.springframework.kafka:spring-kafka:$kafkaVer")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVer")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVer")
    runtimeOnly("org.postgresql:postgresql:$postgreSQLVer")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVer")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootStarterVer")
    testImplementation("org.springframework.kafka:spring-kafka-test:$kafkaVer")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitVer")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
