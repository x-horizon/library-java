dependencies {
    api(project(GradleModule.TOOL_LANG))

    api(GradleDependency.JDBC_MYSQL.withoutVersion)
}