plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

fun getGitHash(): String {
    return providers.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
    }.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
    }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // DB
    runtimeOnly("com.mysql:mysql-connector-j")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("com.redis.testcontainers:testcontainers-redis:1.6.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Swagger UI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // QueryDSL 의존성 추가
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta") // QueryDSL JPA 모듈 (Jakarta API 지원)
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework:spring-aspects")
}

// QueryDSL Q 클래스 생성 디렉토리 설정
tasks.withType<JavaCompile> {
    options.annotationProcessorGeneratedSourcesDirectory = file("$buildDir/generated/querydsl")
}

// QueryDSL Q 클래스 경로를 소스 경로에 포함
sourceSets {
    main {
        java {
            srcDirs("$buildDir/generated/querydsl")
        }
    }
}

// 테스트 환경 설정
tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("user.timezone", "UTC")
}