// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

dependencies {
    api(project(GradleModule.toReferenceName(GradleModule.ORM_MYBATIS_CONTRACT)))
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_CONVERT_ALL)))

    api(GradleDependency.DATA_POSTGRESQL.withoutVersion)
}