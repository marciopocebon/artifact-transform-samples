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
