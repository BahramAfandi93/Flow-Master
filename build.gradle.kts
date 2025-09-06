import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("groovy")
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version ("1.9.10")
    id("org.springframework.boot") version ("2.7.14")
    id("org.jetbrains.kotlin.kapt") version ("1.9.10")
    id("io.spring.dependency-management") version ("1.0.11.RELEASE")
    id("org.jetbrains.kotlin.plugin.jpa") version ("1.9.10")
    id("org.jetbrains.kotlin.plugin.spring") version ("1.9.10")
}

group = "com.calcpro"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring
    implementation("org.springframework:spring-core:5.3.27")
    implementation("org.springframework:spring-beans:5.3.27")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.14")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // logging
    implementation("net.logstash.logback:logstash-logback-encoder:7.0.1")

    // thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // test
    testImplementation("io.github.benas:random-beans:3.9.0")
    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
    testImplementation("org.spockframework:spock-spring:2.3-groovy-4.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // feign
    //    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.7")
    //    implementation("io.github.openfeign:feign-httpclient:12.3")
    //    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    //
    //    //mail
    //    implementation ("org.springframework.boot:spring-boot-starter-mail")

    // data
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
//    implementation("org.hibernate:hibernate-core:5.6.8.Final")
//    runtimeOnly("org.postgresql:postgresql")
//    implementation("org.liquibase:liquibase-core")

    // mapper
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // security
//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
//    testImplementation("org.springframework.security:spring-security-test")

    // thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
