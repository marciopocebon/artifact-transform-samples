
import com.google.common.hash.Hashing
import com.google.common.io.Files
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.work.ChangeType
import org.gradle.work.InputChanges
import java.io.File

abstract class Checksum : DefaultTask() {

    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputFiles
    abstract val sources: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun checksum(changes: InputChanges) {
        val outputDir = outputDirectory.get().asFile
        if (!changes.isIncremental) {
            println("Non-incremental changes - cleaning output directory")
            outputDir.deleteRecursively()
            outputDir.mkdirs()
        }
        changes.getFileChanges(sources).forEach { change ->
            val changedFile = change.file
            if (changedFile.exists() && !changedFile.isFile) {
                return
            }
            when (change.changeType) {
                ChangeType.ADDED, ChangeType.MODIFIED -> outputFileForInput(changedFile).writeText(hashFileContents(changedFile))
                ChangeType.REMOVED -> deleteStaleOutput(outputFileForInput(changedFile))
            }
        }
    }

    private fun deleteStaleOutput(file: File) {
        println("Removing old output ${file.name}")
        file.delete()
    }

    private fun outputFileForInput(inputFile: File) = outputDirectory.get().asFile.resolve("${inputFile.name}.sha256")

    private fun hashFileContents(file: File): String {
        println("Hashing ${file.name}")
        return Files.asByteSource(file).hash(Hashing.sha256()).toString()
    }
}
