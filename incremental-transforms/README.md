# Incremental artifact transforms

The sample shows an incremental artifact transform, which each file in a source directory to files containing the number of lines of code in the source file.
The transform can be found [here](buildSrc/src/main/kotlin/CountLoc.kt).

On the first run, all the sources are processed.
```console
$>./gradlew :app:resolve


> Transform artifact sources (project :lib2) with CountLoc
Running transform on sources, incremental: false
Processing file Lib2.java

> Transform artifact sources (project :lib1) with CountLoc
Running transform on sources, incremental: false
Processing file Lib1.java

BUILD SUCCESSFUL in 6s
3 actionable tasks: 3 executed
```

If we add a source file, e.g. in `lib1/src/main/java/mylib1/Lib1Helper.java`, then only this file is processed.

```console
$>./gradlew :app:resolve

> Transform artifact sources (project :lib1) with CountLoc
Running transform on sources, incremental: true
Processing file Lib1Helper.java

BUILD SUCCESSFUL in 1s
3 actionable tasks: 2 executed, 1 up-to-date
```

If we remove the file again, then the removal is communicated to the transform:

```console
$>./gradlew :app:resolve

> Transform artifact sources (project :lib1) with CountLoc
Running transform on sources, incremental: true
Removing leftover output file Lib1Helper.java.loc

BUILD SUCCESSFUL in 1s
3 actionable tasks: 2 executed, 1 up-to-date
```