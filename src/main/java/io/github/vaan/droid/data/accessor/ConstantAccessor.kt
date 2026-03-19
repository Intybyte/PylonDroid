package io.github.vaan.droid.data.accessor

import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import org.bukkit.NamespacedKey
import java.util.UUID

class ConstantAccessor(val value: Valued<*>) : DataAccessor {
    override fun get(data: DynamicDroidData): Valued<*> = value

    override fun set(data: DynamicDroidData, value: Valued<*>) {
        data.addLog("CRITICAL", "Cannot write result to constant")
        data.error = true
    }

    companion object {
        fun of(str: String): ConstantAccessor? {
            return ConstantAccessor(Valued.parseString(str))
        }
    }
}