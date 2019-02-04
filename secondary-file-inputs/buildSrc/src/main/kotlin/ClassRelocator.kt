import me.lucko.jarrelocator.JarRelocator
import me.lucko.jarrelocator.Relocation
import org.gradle.api.artifacts.transform.ArtifactTransformAction
import org.gradle.api.artifacts.transform.ArtifactTransformOutputs
import org.gradle.api.artifacts.transform.PrimaryInput
import org.gradle.api.artifacts.transform.PrimaryInputDependencies
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.CompileClasspath
import java.io.File
import java.util.function.Predicate
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.stream.Collectors


@TransformAction(ClassRelocatorAction::class)
interface ClassRelocator {
    @get:CompileClasspath
    val externalClasspath: ConfigurableFileCollection
}

abstract class ClassRelocatorAction : ArtifactTransformAction {
    @get:TransformParameters
    abstract val parameters: ClassRelocator

    @get:Classpath
    @get:PrimaryInput
    abstract val primaryInput: File

    @get:CompileClasspath
    @get:PrimaryInputDependencies
    abstract val dependencies: FileCollection

    override fun transform(outputs: ArtifactTransformOutputs) {
        if (parameters.externalClasspath.contains(primaryInput)) {
            outputs.registerOutputFile(primaryInput)
        } else {
            val baseName = primaryInput.name.substring(0, primaryInput.name.length - 4)
            relocateJar(outputs.registerOutput("$baseName-relocated.jar"))
        }
    }

    private fun relocateJar(output: File) {
        val relocatedPackages = (dependencies.flatMap { it.readPackages() } + primaryInput.readPackages()).toSet()
        val nonRelocatedPackages = parameters.externalClasspath.flatMap { it.readPackages() }
        val relocations = (relocatedPackages - nonRelocatedPackages).map { packageName ->
            val toPackage = "relocated.$packageName"
            println("$packageName -> $toPackage")
            Relocation(packageName, toPackage)
        }
        JarRelocator(primaryInput, output, relocations).run()
    }

    private fun File.readPackages(): Set<String> {
        return JarFile(this).use { jarFile ->
            return jarFile.stream()
                    .filter(Predicate.not(JarEntry::isDirectory))
                    .filter { it.name.endsWith(".class") }
                    .map { entry ->
                        entry.name.substringBeforeLast('/').replace('/', '.')
                    }
                    .collect(Collectors.toSet())
        }
    }
}
