plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Data JPA: 관계형 데이터베이스와 객체를 매핑하여 데이터를 쉽게 관리할 수 있도록 돕는 라이브러리
    // 데이터베이스 CRUD(생성, 읽기, 업데이트, 삭제) 작업을 편리하게 수행할 수 있습니다.
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Security OAuth2 Authorization Server:
    // 이 애플리케이션이 OAuth 2.0 권한 서버(Authorization Server) 역할을 하도록 설정합니다.
    // 즉, 다른 클라이언트 애플리케이션에 Access Token을 발급해주는 역할을 수행합니다.
    // 자체적인 OAuth2 인증 시스템을 구축할 때 사용합니다.
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")

    // Spring Security OAuth2 Resource Server:
    // 이 애플리케이션이 OAuth 2.0 리소스 서버(Resource Server) 역할을 하도록 설정합니다.
    // 즉, 보호된 API나 자원을 제공하며, 클라이언트가 보낸 Access Token의 유효성을 검증하고,
    // 유효한 경우에만 자원 접근을 허용합니다. (토큰 발행이 아닌, 토큰 검증 역할)
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Spring Security: 강력한 인증(Authentication) 및 인가(Authorization) 기능을 제공하는 핵심 보안 프레임워크입니다.
    // 웹 애플리케이션의 보안을 쉽게 설정하고 관리할 수 있도록 도와줍니다.
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Spring Web: 웹 애플리케이션 개발에 필요한 의존성(Spring MVC, Tomcat 등)을 제공합니다.
    // RESTful API를 만들거나 웹 페이지를 렌더링하는 등의 기능을 구현할 때 사용합니다.
    implementation("org.springframework.boot:spring-boot-starter-web")

    // SpringDoc OpenAPI UI: OpenAPI 3(Swagger) 기반의 API 문서를 자동으로 생성하고 UI를 제공합니다.
    // REST API의 명세를 자동으로 만들고 웹 브라우저에서 테스트할 수 있는 페이지를 제공하여 API 개발 및 공유를 돕습니다.
    // (Spring Boot 3.x 이상에서 권장되는 Swagger 구현체)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0") // 최신 버전을 확인하세요!

    // Lombok: 자바 개발 시 반복되는 코드(getter, setter, 생성자 등) 작성을 줄여주는 유틸리티 라이브러리입니다.
    // 컴파일 시점에 자동으로 코드를 생성하여 생산성을 높여줍니다.
    // 'compileOnly'는 컴파일 시에만 필요하고 런타임에는 포함되지 않음을 의미합니다.
    compileOnly("org.projectlombok:lombok")

    // Spring Boot DevTools: 개발 단계에서 편리한 기능을 제공합니다.
    // 코드 변경 시 애플리케이션 자동 재시작, 라이브 리로드(LiveReload) 등을 지원하여 개발 효율을 높여줍니다.
    // 'developmentOnly'는 개발 환경에서만 적용되는 의존성임을 나타냅니다.
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Spring Boot Docker Compose: 개발 환경에서 Docker Compose를 사용하여
    // 애플리케이션과 연관된 서비스(DB, Redis 등)를 쉽게 실행하고 관리할 수 있도록 돕습니다.
    // 'developmentOnly'는 개발 환경에서만 적용되는 의존성임을 나타냅니다.
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    // MySQL Connector/J: MySQL 데이터베이스에 연결하기 위한 JDBC 드라이버입니다.
    // 'runtimeOnly'는 애플리케이션 실행 시에만 필요한 의존성임을 의미합니다.
    runtimeOnly("com.mysql:mysql-connector-j")

    // Spring Boot Configuration Processor:
    // '@ConfigurationProperties' 어노테이션이 붙은 클래스의 설정 속성들에 대한 메타데이터를 생성합니다.
    // 이를 통해 IDE(IntelliJ 등)에서 'application.properties' 또는 'application.yml' 설정 시
    // 자동 완성 및 문서화 기능을 제공하여 설정 파일 작성을 편리하게 해줍니다.
    // 'annotationProcessor'는 컴파일러 플러그인처럼 작동하여 코드 생성 등에 사용됩니다.
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Lombok: 앞서 'compileOnly'로 추가된 Lombok이 실제로 코드를 생성하도록 하는 어노테이션 프로세서입니다.
    // 이 선언이 있어야 Lombok 어노테이션이 제대로 동작합니다.
    annotationProcessor("org.projectlombok:lombok")

    // Spring Boot Starter Test: 테스트를 위한 핵심 의존성(JUnit, Mockito, Spring Test 등)을 제공합니다.
    // 단위 테스트, 통합 테스트 등을 작성할 때 사용합니다.
    // 'testImplementation'은 테스트 시에만 필요한 의존성임을 의미합니다.
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Spring Security Test: Spring Security가 적용된 애플리케이션의 테스트를 돕는 유틸리티를 제공합니다.
    // 예를 들어, MockMvc를 사용하여 보안 컨텍스트를 시뮬레이션하거나 특정 사용자 권한으로 테스트할 수 있습니다.
    testImplementation("org.springframework.security:spring-security-test")

    // JUnit Platform Launcher: JUnit 5 테스트를 실행하기 위한 런타임 의존성입니다.
    // 'testRuntimeOnly'는 테스트 실행 시에만 필요한 의존성임을 의미합니다.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
