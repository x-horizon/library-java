dependencies {
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_LANG)))

    api(GradleDependency.TOOL_TEMPLATE_ENGINE_APACHE_VELOCITY.withoutVersion)
}