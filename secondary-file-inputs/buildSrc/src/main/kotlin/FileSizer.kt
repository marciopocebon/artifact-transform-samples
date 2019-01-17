import org.gradle.api.Project
import org.gradle.api.artifacts.transform.ArtifactTransformDependencies
import org.gradle.api.artifacts.transform.PrimaryInput
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.Workspace
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.internal.artifacts.transform.TransformationDependency
import org.gradle.api.tasks.CompileClasspath
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.naming.spi.ObjectFactory

@TransformAction(FileSizerAction::class)
interface FileSizer {
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputFiles
    var additionalInputs: FileCollection
}

abstract class FileSizerAction : Callable<List<File>> {
    @get:Inject
    abstract val parameters: FileSizer

    @get:PathSensitive(PathSensitivity.NONE)
    @get:InputFiles
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
        val output = workspace.resolve("${primaryInput.name}.txt")
        output.writeText(primaryInput.length().toString(), StandardCharsets.UTF_8)
        output.appendText("\ndependencies:")
        dependencyFiles.forEach {
            output.appendText("\n${it.name}: ${it.length()}")
        }
        output.appendText("\nparameters:")
        parameters.additionalInputs.forEach {
            output.appendText("\n${it.name}: ${it.length()}")
        }
        return listOf(output)
    }
}