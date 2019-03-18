val artifactType = Attribute.of("artifactType", String::class.java)

dependencies {
    implementation(project(":lib1"))
    implementation(project(":lib2"))

    registerTransform(CountLoc::class) {
        from.attribute(artifactType, "java")
        to.attribute(artifactType, "loc")
    }
}
