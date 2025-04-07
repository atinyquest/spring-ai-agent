plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.tinyquest"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
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

extra["springModulithVersion"] = "1.3.4"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.modulith:spring-modulith-starter-core")
	implementation("org.springframework.modulith:spring-modulith-runtime")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")


	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	runtimeOnly("org.springframework.modulith:spring-modulith-docs")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.json:json:20240303")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6") // JWT API (인터페이스)

	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")  // JWT 구현체
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")  // JSON 파싱 (Jackson 기반)
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()

	doFirst {
		val agentJar = configurations.testRuntimeClasspath.get().files
			.firstOrNull { it.name.contains("byte-buddy-agent") }

		if (agentJar != null) {
			jvmArgs("-javaagent:${agentJar.absolutePath}")
			println("✅ Mockito agent attached: ${agentJar.name}")
		} else {
			println("⚠️ Byte Buddy Agent not found in classpath.")
		}
	}
}

val installGitHooks by tasks.registering(Copy::class) {
	val gitHookDir = file(".git/hooks")
	val customHookDir = file("hooks")

	// 언제나 복사 (기존 파일이 있어도 최신 내용으로 덮어쓰기)
	from(customHookDir)
	into(gitHookDir)

	// 기존 파일과 동일하면 overwrite 안 함, 달라지면 덮어씀 (Gradle Copy 기본 기능)
	duplicatesStrategy = DuplicatesStrategy.INCLUDE

	doLast {
		println("✅ Git hooks copied from 'hooks/' to '.git/hooks'")
		fileTree(gitHookDir).forEach {
			it.setExecutable(true)
		}
	}
}

// 주요 task 실행 시 항상 최신 hook 유지
listOf("build", "test", "bootRun").forEach { taskName ->
	tasks.named(taskName).configure {
		dependsOn(installGitHooks)
	}
}
