dependencies {
    api(project(GradleModule.TOOL_LANG))
    api(project(GradleModule.TOOL_SPRING_CONTRACT))

    api(GradleDependency.TOOL_JDK_BURNING_WAVE_CORE.withoutVersion)
}