subprojects {
    apply(plugin = "java")
    the<JavaPluginExtension>().sourceCompatibility = JavaVersion.VERSION_1_8

    repositories {
        jcenter()
    }
}