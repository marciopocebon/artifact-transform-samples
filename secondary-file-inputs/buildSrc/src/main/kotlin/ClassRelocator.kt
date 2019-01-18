
import me.lucko.jarrelocator.JarRelocator
import me.lucko.jarrelocator.Relocation
import org.gradle.api.artifacts.transform.ArtifactTransformDependencies
import org.gradle.api.artifacts.transform.PrimaryInput
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.Workspace
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.CompileClasspath
import org.gradle.api.tasks.Internal
import java.io.File
import java.util.concurrent.Callable
import java.util.function.Predicate
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.stream.Collectors
import javax.inject.Inject


@TransformAction(ClassRelocatorAction::class)
interface ClassRelocator {
    @get:CompileClasspath
    var externalClasspath: FileCollection
}

abstract class ClassRelocatorAction : Callable<List<File>> {
    @get:Inject
    abstract val parameters: ClassRelocator

    @get:Classpath
    @get:PrimaryInput
    abstract val primaryInput: File

    @get:Workspace
    abstract val workspace: File

    @get:Internal
    @get:Inject
    abstract val dependencies: ArtifactTransformDependencies

    @get:CompileClasspath
    val dependencyFiles
        get() = dependencies.files

    override fun call(): List<File> {
        val baseName = primaryInput.name.substring(0, primaryInput.name.length - 4)
        val output = workspace.resolve("${baseName}-relocated.jar")
        val relocatedPackages = (dependencies.files.flatMap { it.readPackages() } + primaryInput.readPackages()).toSet()
        val nonRelocatedPackages = parameters.externalClasspath.flatMap { it.readPackages() }
        val relocations = (relocatedPackages - nonRelocatedPackages).map { packageName ->
            val toPackage = "relocated.$packageName"
            println("$packageName -> $toPackage")
            Relocation(packageName, toPackage)
        }
        JarRelocator(primaryInput, output, relocations).run()
        return listOf(output)
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