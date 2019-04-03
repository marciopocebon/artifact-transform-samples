# Cacheable Artifact Transforms

This sample shows how to declare cacheable artifact transforms.
Short version: Add `@CacheableTransform` to the transform action.

The implementation of the transform is in [buildSrc/src/main/kotlin/ClassRelocator.kt](buildSrc/src/main/kotlin/ClassRelocator.kt) and it is registered in [app/app.gradle.kts](app/app.gradle.kts).

Build cache logging can be enabled via `-Dorg.gradle.caching.debug=true`.

What to do:
1. Run `./gradlew :app:relocateJars --build-cache`
    ```console
    > Task :buildSrc:jar
    :jar: No valid plugin descriptors were found in META-INF/gradle-plugins
    
    > Transform artifact lib2.jar (project :lib2) with ClassRelocator
    mylib1 -> relocated.mylib1
    mylib2 -> relocated.mylib2
    
    > Transform artifact lib1.jar (project :lib1) with ClassRelocator
    mylib1 -> relocated.mylib1
    
    > Transform artifact app.jar (project :app) with ClassRelocator
    App.class -> relocated.App.class
    mylib2 -> relocated.mylib2
    mylib1 -> relocated.mylib1
    
    BUILD SUCCESSFUL in 8s
    7 actionable tasks: 7 executed
    ```

2. Clean `lib:jar`: `./gradlew :lib1:clean`
3. Run `./gradlew :app:relocateJars --build-cache --info -Dorg.gradle.caching.debug=true`
    ```console
    ...
    
    > Transform artifact lib1.jar (project :lib1) with ClassRelocator
    Transforming artifact lib1.jar (project :lib1) with ClassRelocator
    Appending implementation to build cache key: ClassRelocator@9ac1ebcfaef77eefd1e72958de605234
    Appending input value fingerprint for 'inputPropertiesHash' to build cache key: d72c7c688d1e7da69f0e9f6fb5c1576c
    Appending input file fingerprints for 'inputArtifact' to build cache key: 5a85193a844a8474136a4abe3969788d
    Appending input file fingerprints for 'inputArtifactDependencies' to build cache key: e6e7d347126dbfb9f5e21ca9a6e1659e
    Appending output property name to build cache key: outputDirectory
    Appending output property name to build cache key: resultsFile
    Build cache key for ClassRelocator: /Users/user/gradle/artifact-transform-samples/cacheable-transforms/lib1/build/libs/lib1.jar is 6cdf3afcabb56317af1bb0beea45348e
    ClassRelocator: /Users/user/gradle/artifact-transform-samples/cacheable-transforms/lib1/build/libs/lib1.jar is not up-to-date because:
      Output property 'outputDirectory' file /Users/user/gradle/artifact-transform-samples/cacheable-transforms/lib1/build/transforms/80e27c0142b78242002f8db9057e8a58 has been removed.
      Output property 'outputDirectory' file /Users/user/gradle/artifact-transform-samples/cacheable-transforms/lib1/build/transforms/80e27c0142b78242002f8db9057e8a58/lib1-relocated.jar has been removed.
      Output property 'resultsFile' file /Users/user/gradle/artifact-transform-samples/cacheable-transforms/lib1/build/transforms/80e27c0142b78242002f8db9057e8a58.bin has been removed.
    Origin for org.gradle.api.internal.artifacts.transform.DefaultTransformerInvoker$TransformerExecution@a507c49: {executionTime=81, hostName=My-MacBook-Pro.local, creationTime=1554278683148, identity=transform/80e27c0142b78242002f8db9057e8a58, buildInvocationId=odbfefr2xbaytmhjpi4rcfvt24, rootPath=/Users/user/gradle/artifact-transform-samples/cacheable-transforms, type=org.gradle.api.internal.artifacts.transform.DefaultTransformerInvoker.TransformerExecution, userName=user, operatingSystem=Mac OS X, gradleVersion=5.4-20190403012714+0000}
    Unpacked trees for ClassRelocator: /Users/user/gradle/artifact-transform-samples/cacheable-transforms/lib1/build/libs/lib1.jar from cache.
    ClassRelocator (Thread[Execution worker for ':',5,main]) completed. Took 0.014 secs.
    ...
    ```
