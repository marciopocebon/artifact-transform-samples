plugins {
    id("java-library")
}

repositories {
    maven {
        url = uri("https://repo.gradle.org/gradle/libs")
    }
}

dependencies {
	testImplementation(gradleTestKit())
    testImplementation("org.gradle:sample-check:0.4.2")
}
