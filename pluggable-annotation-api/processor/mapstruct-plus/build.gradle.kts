dependencies {
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_LANG)))

    api(GradleDependency.PLUGGABLE_ANNOTATION_PROCESSING_API_PROCESSOR_MAPSTRUCT_PLUS.withoutVersion)
}