dependencies {
    api(project(GradleModule.TOOL_LANG))

    api(GradleDependency.TEST_SPRING_INTEGRATION_TEST.withoutVersion)
}