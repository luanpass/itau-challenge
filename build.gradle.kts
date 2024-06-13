import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.aot.hint.TypeReference.listOf

plugins {
	jacoco
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "br.com"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// KOTLIN
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.22")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.22")

	//DOCUMENTATION
	implementation("org.springdoc:springdoc-openapi-starter-common:2.0.2")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

	//SPRING
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-web")

	//TESTS
	testImplementation("io.mockk:mockk:1.13.3")
	testImplementation("org.wiremock:wiremock-standalone:3.0.4")
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.1")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	testLogging {
		showStandardStreams = true
		events(org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
			org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
			org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED)
	}
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
	finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.withType<JacocoReport> {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.required.set(true)
	}
	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(
				"**/config",
				"**/exception",
				"**/extensions",
				"**/handler",
				"/**/entity",
				"/**/response",
				"/**/data",
				"br/com/itau/challenge/ItauChallengeApplication.kt"
			)
		}
	)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				counter = "LINE"
				minimum = BigDecimal(0.80)
			}
		}
		rule {
			limit {
				counter = "BRANCH"
				minimum = BigDecimal(0.80)
			}
		}
	}
	mustRunAfter(tasks.jacocoTestReport)
}