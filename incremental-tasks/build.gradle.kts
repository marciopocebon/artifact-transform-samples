plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.platform:junit-platform-engine:1.4.1")
    implementation("org.junit.jupiter:junit-jupiter:5.4.1")
}

tasks.register<Checksum>("checksum") {
    sources.from(configurations.runtimeClasspath)
    sources.from({
        project.fileTree("inputs").files
    })
    outputDirectory.set(layout.buildDirectory.dir("checksummed"))
}