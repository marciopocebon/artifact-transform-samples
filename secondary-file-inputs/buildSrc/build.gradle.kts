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

dependencies {
    implementation("me.lucko:jar-relocator:1.3")
}
