// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

dependencies {
    api(project(GradleModule.toReferenceName(GradleModule.DATA_POSTGRESQL)))
    api(project(GradleModule.toReferenceName(GradleModule.ORM_CONTRACT_MYBATIS_BASE)))

    annotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_LOMBOK_MAPSTRUCT_BINDING)))
    annotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_MAPSTRUCT_PLUS)))
    testAnnotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_LOMBOK_MAPSTRUCT_BINDING)))
    testAnnotationProcessor(project(GradleModule.toReferenceName(GradleModule.PLUGGABLE_ANNOTATION_API_PROCESSOR_MAPSTRUCT_PLUS)))
}