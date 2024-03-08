import io.micronaut.testresources.buildtools.KnownModules.HIBERNATE_REACTIVE_MYSQL

plugins {
    id("io.micronaut.library")
    id("io.micronaut.test-resources")
    id("io.micronaut.graalvm") // Required to configure Graal for nativeTest
    id("io.micronaut.application")
}

repositories {
    mavenCentral()
}

tasks.withType(Test::class).configureEach {
    useJUnitPlatform()
}

dependencies {
    annotationProcessor(mnData.micronaut.data.processor)

    testAnnotationProcessor(mnData.micronaut.data.processor)
    testAnnotationProcessor(mnSerde.micronaut.serde.processor)

    testImplementation(projects.micronautTestJunit5)
    testImplementation(mnData.micronaut.data.hibernate.reactive)
    testImplementation(mnSerde.micronaut.serde.jackson)

    testRuntimeOnly(mnLogging.logback.classic)
    testRuntimeOnly("io.vertx:vertx-mysql-client:4.5.3")

    testResourcesService(mnSql.mysql.connector.java)
}

micronaut {
    importMicronautPlatform.set(false)
    testResources {
        version.set(libs.versions.micronaut.test.resources)
        additionalModules.add(HIBERNATE_REACTIVE_MYSQL)
    }
}
