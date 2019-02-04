# Secondary File Inputs

This sample shows how to pass non-primary file inputs to artifact transformations.

The implementation of the transform is in [buildSrc/src/main/kotlin/ClassRelocator.kt](buildSrc/src/main/kotlin/ClassRelocator.kt) and it is registered in [app/app.gradle](app/app.gradle)

The following things still will/might change:
- `@PrimaryInput` will imply `@InputFiles`, as will `@PrimaryInputDependencies`.
- The names of the different API methods still may change.
