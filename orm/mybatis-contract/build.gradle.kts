// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

dependencies {
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_CONVERT_MAPSTRUCT)))
    api(project(GradleModule.toReferenceName(GradleModule.POOL_DATABASE_HIKARICP)))
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_ENUMS)))
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_LANG)))
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_SPRING_CONTRACT)))

    api(GradleDependency.ORM_MYBATIS.withoutVersion)
    api(GradleDependency.TOOL_VALIDATION_HIBERNATE.withoutVersion)
    implementation(GradleDependency.DOC_XIAOYMIN_KNIFE4J_OPENAPI3_JAKARTA_SPRING_BOOT.withoutVersion)

    annotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_LOMBOK_MAPSTRUCT_BINDING)))
    annotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_MAPSTRUCT)))
    testAnnotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_LOMBOK_MAPSTRUCT_BINDING)))
    testAnnotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_MAPSTRUCT)))
}