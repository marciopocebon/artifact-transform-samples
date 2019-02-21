# Cacheable Artifact Transforms

This sample shows how to declare cacheable artifact transforms.
Short version: Add `@CacheableTransformAction` to the transform action.

The implementation of the transform is in [buildSrc/src/main/kotlin/ClassRelocator.kt](buildSrc/src/main/kotlin/ClassRelocator.kt) and it is registered in [app/app.gradle.kts](app/app.gradle.kts).

What to do:
1. Run `./gradlew :app:relocateJars --build-cache`
    ```console
    > Task :buildSrc:jar
    :jar: No valid plugin descriptors were found in META-INF/gradle-plugins
    
    > Transform artifact lib2.jar (project :lib2) with ClassRelocatorAction
    mylib1 -> relocated.mylib1
    mylib2 -> relocated.mylib2
    
    > Transform artifact lib1.jar (project :lib1) with ClassRelocatorAction
    mylib1 -> relocated.mylib1
    
    > Transform artifact app.jar (project :app) with ClassRelocatorAction
    App.class -> relocated.App.class
    mylib2 -> relocated.mylib2
    mylib1 -> relocated.mylib1
    
    BUILD SUCCESSFUL in 8s
    7 actionable tasks: 7 executed
    ```

2. Clean `lib:jar`: `./gradlew :lib1:clean`
3. Run `./gradlew :app:relocateJars --build-cache --info`
    ```console
    ...
    > Task :lib1:jar
    Build cache key for task ':lib1:jar' is fdb9d6141c2f55291cb7a66baa4438ba
    Caching disabled for task ':lib1:jar': Caching has not been enabled for the task
    Task ':lib1:jar' is not up-to-date because:
      Output property 'archiveFile' file /Users/my-user/artifact-transform-samples/cacheable-transforms/lib1/build/libs/lib1.jar has been removed.
    :lib1:jar (Thread[Execution worker for ':',5,main]) completed. Took 0.005 secs.
    ClassRelocatorAction (Thread[Execution worker for ':',5,main]) started.
    :lib2:compileJava (Thread[Execution worker for ':' Thread 6,5,main]) started.
    
    > Transform artifact lib1.jar (project :lib1) with ClassRelocatorAction
    Transforming artifact lib1.jar (project :lib1) with ClassRelocatorAction
    ClassRelocatorAction: /Users/my-user/artifact-transform-samples/cacheable-transforms/lib1/build/libs/lib1.jar is not up-to-date because:
      No history is available.
    Origin for org.gradle.api.internal.artifacts.transform.DefaultTransformerInvoker$TransformerExecution@3e8ecc6f: {executionTime=14, hostName=Stefans-MacBook-Pro-3.local, creationTime=1550743135666, identity=transform/504db00c9d942134757474a5a7a56243, buildInvocationId=f3lj7uthxzg37h3kh3ae7ycc2e, rootPath=/Users/my-user/artifact-transform-samples/cacheable-transforms, type=org.gradle.api.internal.artifacts.transform.DefaultTransformerInvoker.TransformerExecution, userName=wolfs, operatingSystem=Mac OS X, gradleVersion=5.3-20190220101700+0000}
    Unpacked trees for ClassRelocatorAction: /Users/my-user/artifact-transform-samples/cacheable-transforms/lib1/build/libs/lib1.jar from cache.
    ClassRelocatorAction (Thread[Execution worker for ':',5,main]) completed. Took 0.006 secs.
    ...
    ```