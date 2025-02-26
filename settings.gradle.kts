pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/spring/")
        maven("https://repo.spring.io/milestone/")
        maven("https://repo.spring.io/snapshot/")
        gradlePluginPortal()
    }
}

rootProject.name = "library-java"
rootDir.walkTopDown()
    .filter {
        it.isDirectory && it.name != "buildSrc" && (it.resolve("build.gradle.kts").exists() || it.resolve("build.gradle").exists())
    }
    .forEach { moduleAbsolutePath ->
        val moduleRelativePath = moduleAbsolutePath.relativeTo(rootDir).path
        val moduleName = moduleRelativePath.replace(File.separator, "-")
        include(":$moduleName")
        project(":$moduleName").projectDir = moduleAbsolutePath
    }