// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

dependencies {
    api(project(GradleModule.toReferenceName(GradleModule.TOOL_LANG)))

    api(GradleDependency.GATEWAY_SPRING_CLOUD.withoutVersion)
    api(GradleDependency.LOADBALANCER_SPRING_CLOUD.withoutVersion)
}