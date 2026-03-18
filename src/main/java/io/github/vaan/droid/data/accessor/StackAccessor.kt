package io.github.vaan.droid.data.accessor

import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued

class StackAccessor(val index: Int) : DataAccessor {
    override fun get(data: DynamicDroidData): Valued<*>? = data.stack[index]

    override fun set(data: DynamicDroidData, value: Valued<*>?) {
        data.stack[index] = value
    }

    companion object {
        fun of(data: DynamicDroidData, str: String): StackAccessor? {
            if (str.startsWith("[") && str.endsWith("]")) {
                val position = str.substring(1, str.length - 1)
                val index = position.toIntOrNull() ?: return null
                if (index >= data.static.stackSize) return null

                return StackAccessor(index)
            }

            return null
        }
    }
}