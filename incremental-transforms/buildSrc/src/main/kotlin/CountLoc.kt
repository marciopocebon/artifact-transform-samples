
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.work.ChangeType
import org.gradle.work.InputChanges
import java.io.File
import javax.inject.Inject

abstract class CountLoc : TransformAction<TransformParameters.None> {

    @get:Inject
    abstract val inputChanges: InputChanges

    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputArtifact
    abstract val input: File

    override fun transform(outputs: TransformOutputs) {
        val outputDir = outputs.dir("${input.name}.loc")
        println("Running transform on ${input.name}, incremental: ${inputChanges.isIncremental}")
        inputChanges.getFileChanges(input).forEach { change ->
            val changedFile = change.file
            if (changedFile.exists() && !changedFile.isFile) {
                return@forEach
            }
            val outputLocation = outputDir.resolve("${changedFile.relativeTo(input)}.loc")
            when (change.changeType) {
                ChangeType.ADDED, ChangeType.MODIFIED -> {
                    println("Processing file ${changedFile.name}")
                    outputLocation.parentFile.mkdirs()
                    outputLocation.writeText(changedFile.readLines().size.toString())
                }
                ChangeType.REMOVED -> {
                    println("Removing leftover output file ${outputLocation.name}")
                    outputLocation.delete()
                }
            }
        }
    }
}