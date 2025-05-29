/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.raizlabs.android.dbflow.config

import java.util.concurrent.ConcurrentHashMap

object FlowInstanceManager {
    val instances = ConcurrentHashMap<String, FlowInstance>()

    @JvmStatic
    @Synchronized
    fun get(id: String): FlowInstance? = instances[id]

    @JvmStatic
    @Synchronized
    fun add(id:String): FlowInstance {
        return instances.getOrPut(id) {
            FlowInstance(id)
        }
    }

    @JvmStatic
    @Synchronized
    fun remove(id: String) {
        instances.remove(id)
    }
}