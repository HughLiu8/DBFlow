/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.raizlabs.android.dbflow

object DbFlowDependencyHelper {
    private var dependency: IDbFlowDependency? = null

    @JvmStatic
    fun setDependency(dbFlowDependency: IDbFlowDependency) {
        dependency = dbFlowDependency
    }

    @JvmStatic
    fun getDependency(): IDbFlowDependency? = dependency

    @JvmStatic
    fun checkId(id: String?, source: String) {
        if (id == null && dependency?.isMultipleDatabaseEnabled() == true) {
            throw IllegalStateException("id can't be null in $source")
        }
    }

    @JvmStatic
    fun checkIdWithWarning(id: String?, source: String) {
        if (id == null && dependency?.isMultipleDatabaseEnabled() == true) {
            dependency?.log(LogPriority.ERROR, "DbFlowDependencyHelper", "id is null from $source")
        }
    }
}