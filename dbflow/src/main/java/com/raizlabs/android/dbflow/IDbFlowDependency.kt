/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.raizlabs.android.dbflow

import androidx.annotation.IntDef

@IntDef(value = [LogPriority.ALL, LogPriority.VERBOSE, LogPriority.DEBUG, LogPriority.ADAL_INFO, LogPriority.INFO, LogPriority.WARNING, LogPriority.ERROR, LogPriority.ASSERT])
@Retention(
    AnnotationRetention.SOURCE
)
annotation class LogPriority {
    companion object {
        const val ALL: Int = -1
        const val VERBOSE: Int = 2
        const val DEBUG: Int = 3
        const val ADAL_INFO: Int = 4
        const val INFO: Int = 5
        const val WARNING: Int = 6
        const val ERROR: Int = 7
        const val ASSERT: Int = 8
    }
}


interface IDbFlowDependency {
    fun isMultipleDatabaseEnabled(): Boolean

    fun log(@LogPriority priority: Int, tag: String, format: String?, vararg args: Any?)
}