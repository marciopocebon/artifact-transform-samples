plugins {
    java
}

val artifactType = Attribute.of("artifactType", String::class.java)

val externalClasspath by configurations.creating
val jarsToRelocate by configurations.creating {
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    isCanBeResolved = true
    isCanBeConsumed = false
}

val externalClasspathConfiguration = externalClasspath

afterEvaluate {
    dependencies.add(jarsToRelocate.name, project)
}

dependencies {
    implementation(project(":lib1"))
    implementation(project(":lib2"))
    externalClasspath("com.google.guava:guava:27.0.1-jre")

    registerTransform(ClassRelocator::class) {
        from.attribute(artifactType, "jar")
        to.attribute(artifactType, "relocated")
        parameters {
            externalClasspath.from(externalClasspathConfiguration)
        }
    }
}

tasks.register<Copy>("relocateJars") {
    val artifacts = jarsToRelocate.incoming.artifactView {
        attributes { attribute(artifactType, "relocated") }
    }.artifacts
    from(artifacts.artifactFiles)
    into(project.layout.buildDirectory.dir("relocated"))
}
