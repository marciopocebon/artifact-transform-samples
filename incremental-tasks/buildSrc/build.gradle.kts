plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

plugins.withType<KotlinDslPlugin> {
    kotlinDslPluginOptions {
        experimentalWarning.set(false)
    }
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:26.0-android")
}