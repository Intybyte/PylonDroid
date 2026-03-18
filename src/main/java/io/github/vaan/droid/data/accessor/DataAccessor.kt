package io.github.vaan.droid.data.accessor

import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.instructions.DataRegistry

interface DataAccessor {
    fun get(data: DynamicDroidData): Valued<*>?

    /**
     * Assembly code changes stuff
     */
    fun set(data: DynamicDroidData, value: Valued<*>?)

    fun set(data: DynamicDroidData, value: Any?) {
        set(data, Valued(value))
    }

    /**
     * We change stuff internally
     */
    fun rawSet(data: DynamicDroidData, value: Valued<*>?) {
        set(data, value)
    }

    fun rawSet(data: DynamicDroidData, value: Any?) {
        rawSet(data, value)
    }

    companion object {
        fun of(data: DynamicDroidData, key: String) : DataAccessor? {
            val stack = StackAccessor.of(data, key)
            if (stack != null) {
                return stack
            }

            val registry = DataRegistry.of(key)
            if (registry != null) {
                return registry
            }

            val constant = ConstantAccessor.of(key)
            if (constant != null) {
                return constant
            }

            return null
        }
    }
}