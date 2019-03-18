# Detailed task input changes

This sample shows how to query per-property changes for task inputs.
The example uses a [Checksum](buildSrc/src/main/kotlin/Checksum.kt) task, which incrementally checksums the files on the runtime classpath and everything under the `inputs` directory.

On a first run, everything needs to be checksummed.

```console
$> ./gradlew checksum

> Task :checksum
Non-incremental changes - cleaning output directory
Hashing junit-jupiter-5.4.1.jar
Hashing junit-jupiter-engine-5.4.1.jar
Hashing junit-platform-engine-1.4.1.jar
Hashing junit-jupiter-params-5.4.1.jar
Hashing junit-jupiter-api-5.4.1.jar
Hashing junit-platform-commons-1.4.1.jar
Hashing apiguardian-api-1.0.0.jar
Hashing opentest4j-1.1.1.jar
Hashing something-to-checksum.txt

BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
``` 

If we change `inputs/something-to-checksum.txt` and add a new file - `inputs/new-file.txt`, then only the two new files are processed.

```console
$> ./gradlew checksum

> Task :checksum
Hashing something-to-checksum.txt
Hashing new-file.txt

BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
```

If we now remove the new file again, then the file is removed.

```console
$> ./gradlew checksum

> Task :checksum
Removing old output new-file.txt.sha256

BUILD SUCCESSFUL in 0s
1 actionable task: 1 execute
```