dependencies {
    api(project(GradleModule.TOOL_LANG))

    api(GradleDependency.LOG_SPRING_BOOT.withoutVersion)
}