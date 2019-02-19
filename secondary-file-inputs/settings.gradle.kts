include("lib1")
include("lib2")
include("app")

for (project in rootProject.children) {
    val projectDirName = project.name
    project.buildFileName = "$projectDirName.gradle.kts"
}
