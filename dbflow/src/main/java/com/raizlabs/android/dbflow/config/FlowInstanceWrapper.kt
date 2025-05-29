/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.raizlabs.android.dbflow.config

import android.content.Context
import com.raizlabs.android.dbflow.DbFlowDependencyHelper.checkIdWithWarning
import com.raizlabs.android.dbflow.runtime.ModelNotifier
import com.raizlabs.android.dbflow.structure.InstanceAdapter
import com.raizlabs.android.dbflow.structure.ModelAdapter
import com.raizlabs.android.dbflow.structure.QueryModelAdapter
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper

object FlowInstanceWrapper {
    @JvmStatic
    fun getConfig(id: String?, source: String): FlowConfig {
        checkIdWithWarning(id, "$source _ getConfig")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.config
        }

        return FlowManager.config
    }

    @JvmStatic
    fun getContext(id: String?, source: String): Context {
        checkIdWithWarning(id, "$source _ getContext")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.context
        }

        return FlowManager.getContext()
    }

    @JvmStatic
    fun getWritableDatabaseForTable(id: String?, table: Class<*>, source: String): DatabaseWrapper {
        checkIdWithWarning(id, "$source _ getWritableDatabaseForTable")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getWritableDatabaseForTable(table)
        }

        return FlowManager.getWritableDatabaseForTable(table)
    }

    @JvmStatic
    fun getDatabaseForTable(id: String?, table: Class<*>, source: String): DatabaseDefinition {
        checkIdWithWarning(id, "$source _ getDatabaseForTable")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getDatabaseForTable(table)
        }

        return FlowManager.getDatabaseForTable(table)
    }

    @JvmStatic
    fun<TModel> getModelAdapter(id: String?, table: Class<TModel>, source: String): ModelAdapter<TModel> {
        checkIdWithWarning(id, "$source _ getModelAdapter")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getModelAdapter(table)
        }

        return FlowManager.getModelAdapter(table)
    }

    @JvmStatic
    fun getModelNotifierForTable(id: String?, table: Class<*>, source: String): ModelNotifier {
        checkIdWithWarning(id, "$source _ getModelNotifierForTable")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getModelNotifierForTable(table)
        }

        return FlowManager.getModelNotifierForTable(table)
    }

    @JvmStatic
    fun<T> getInstanceAdapter(id: String?, table: Class<T>, source: String): InstanceAdapter<T> {
        checkIdWithWarning(id, "$source _ getInstanceAdapter")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getInstanceAdapter(table)
        }

        return FlowManager.getInstanceAdapter(table)
    }

    @JvmStatic
    fun<T> getQueryModelAdapter(id: String?, table: Class<T>, source: String): QueryModelAdapter<T> {
        checkIdWithWarning(id, "$source _ getQueryModelAdapter")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getQueryModelAdapter(table)
        }

        return FlowManager.getQueryModelAdapter(table)
    }

    @JvmStatic
    fun getTableName(id: String?, table: Class<*>, source: String): String {
        checkIdWithWarning(id, "$source _ getTableName")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getTableName(table)
        }

        return FlowManager.getTableName(table)
    }

    @JvmStatic
    fun getTableClassForName(id: String?, table: Class<*>, tableName: String, source: String): Class<*> {
        checkIdWithWarning(id, "$source _ getTableClassForName")
        val flowInstance = id?.let { FlowInstanceManager.get(it) }

        if (flowInstance != null) {
            return flowInstance.getTableClassForName(table, tableName)
        }

        return FlowManager.getTableClassForName(table, tableName)
    }
}