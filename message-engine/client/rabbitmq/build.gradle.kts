dependencies {
    api(project(GradleModule.MESSAGE_ENGINE_CLIENT_CONTRACT))

    api(GradleDependency.MESSAGE_RABBITMQ_SPRING_CLOUD_STREAM.withoutVersion)
}