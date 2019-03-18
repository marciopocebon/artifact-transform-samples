subprojects {

    val artifactType = Attribute.of("artifactType", String::class.java)
    allprojects {
        val implementation by configurations.creating {
            attributes.attribute(artifactType, "java")
        }
        val producer by tasks.registering(Sync::class) {
            from(layout.projectDirectory.dir("src/main/java"))
            into(layout.buildDirectory.dir("sources"))
        }
        artifacts {
            add(implementation.name, producer.map { it.destinationDir }) {
                builtBy(producer)
            }
        }

        tasks.create<Sync>("resolve") {
            val view = implementation.incoming.artifactView {
                attributes.attribute(artifactType, "loc")
            }.files
            from(view)
            into(layout.buildDirectory.dir("transformed"))
        }
    }
}

allprojects {
    apply(plugin = "base")
}
