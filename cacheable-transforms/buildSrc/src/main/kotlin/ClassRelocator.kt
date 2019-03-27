import me.lucko.jarrelocator.JarRelocator
import me.lucko.jarrelocator.Relocation
import org.gradle.api.artifacts.transform.CacheableTransform
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.InputArtifactDependencies
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.CompileClasspath
import java.io.File
import java.util.function.Predicate
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.stream.Collectors


@CacheableTransform
abstract class ClassRelocator : TransformAction<ClassRelocator.Parameters> {
    interface Parameters : TransformParameters {
        @get:CompileClasspath
        val externalClasspath: ConfigurableFileCollection
    }

    @get:Classpath
    @get:InputArtifact
    abstract val primaryInput: Provider<FileSystemLocation>

    @get:CompileClasspath
    @get:InputArtifactDependencies
    abstract val dependencies: FileCollection

    override fun transform(outputs: TransformOutputs) {
        val primaryInputFile = primaryInput.get().asFile
        if (parameters.externalClasspath.contains(primaryInput)) {
            outputs.file(primaryInput)
        } else {
            val baseName = primaryInputFile.name.substring(0, primaryInputFile.name.length - 4)
            relocateJar(outputs.file("$baseName-relocated.jar"))
        }
    }

    private fun relocateJar(output: File) {
        val relocatedPackages = (dependencies.flatMap { it.readPackages() } + primaryInput.get().asFile.readPackages()).toSet()
        val nonRelocatedPackages = parameters.externalClasspath.flatMap { it.readPackages() }
        val relocations = (relocatedPackages - nonRelocatedPackages).map { packageName ->
            val toPackage = "relocated.$packageName"
            println("$packageName -> $toPackage")
            Relocation(packageName, toPackage)
        }
        JarRelocator(primaryInput.get().asFile, output, relocations).run()
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
