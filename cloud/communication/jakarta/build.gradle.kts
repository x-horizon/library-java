dependencies {
    api(project(GradleModule.TOOL_LANG))

    api(GradleDependency.CLOUD_COMMUNICATION_JAKARTA_MAIL.withoutVersion)
}