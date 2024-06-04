// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

dependencies {
    api(project(GradleModule.toReferenceName(GradleModule.CONCURRENT_REDIS)))

    api(project(GradleModule.toReferenceName(GradleModule.CACHE_CONTRACT)))
    api(project(GradleModule.toReferenceName(GradleModule.CACHE_MAP)))
    api(project(GradleModule.toReferenceName(GradleModule.CACHE_CAFFEINE)))
    api(project(GradleModule.toReferenceName(GradleModule.CACHE_REDIS)))
}