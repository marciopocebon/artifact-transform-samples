# Secondary File Inputs

This sample shows how to pass non-primary file inputs to artifact transformations.

The implementation of the transform is in [buildSrc/src/main/kotlin/ClassRelocator.kt](buildSrc/src/main/kotlin/ClassRelocator.kt) and it is registered in [app/app.gradle](app/app.gradle)

The following things still will/might change:
- instead of implementing `Callable<List<String>>`, the [artifact transform](buildSrc/src/main/kotlin/ClassRelocator.kt) may need to implement another, Gradle specific, interface.
- `ArtifactDependencies` will be injected by an annotation `@ArtifactDependencies` and be of type `Iterable<File>` instead of by type.
- `@PrimaryInput` will imply `@InputFiles`.
- The names of the different API methods still may change.
- The way how the transform action is inferred from the parameters object still may change. Currently, an annotation `@TransformAction` is used.