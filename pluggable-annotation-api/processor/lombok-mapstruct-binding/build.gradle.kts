dependencies {
    api(project(GradleModule.TOOL_LANG))

    api(GradleDependency.PLUGGABLE_ANNOTATION_PROCESSING_API_PROCESSOR_LOMBOK_MAPSTRUCT_BINDING.withoutVersion)
}