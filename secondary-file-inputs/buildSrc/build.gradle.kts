plugins {
    `kotlin-dsl`
}

plugins.withType<KotlinDslPlugin> {
    kotlinDslPluginOptions {
        experimentalWarning.set(false)
    }
}

repositories {
    jcenter()
}
