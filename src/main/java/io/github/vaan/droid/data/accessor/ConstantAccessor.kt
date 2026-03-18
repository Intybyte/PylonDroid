package io.github.vaan.droid.data.accessor

import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import org.bukkit.NamespacedKey
import java.util.UUID

class ConstantAccessor(val value: Valued<*>?) : DataAccessor {
    override fun get(data: DynamicDroidData): Valued<*>? = value

    override fun set(data: DynamicDroidData, value: Valued<*>?) {
        data.addLog("CRITICAL", "Cannot write result to constant")
        data.error = true
    }

    companion object {
        fun of(str: String): ConstantAccessor? {
            val trimmed = str.trim()

            trimmed.toIntOrNull()?.let { return ConstantAccessor(Valued.IntVal(it)) }

            trimmed.toDoubleOrNull()?.let { return ConstantAccessor(Valued.DoubleVal(it)) }

            try {
                val uuid = UUID.fromString(trimmed)
                return ConstantAccessor(Valued.UUIDVal(uuid))
            } catch (_: IllegalArgumentException) { /* not a UUID */ }

            if (":" in trimmed) {
                val parts = trimmed.split(":", limit = 2)
                if (parts.size == 2) {
                    val key = NamespacedKey(parts[0], parts[1])
                    return ConstantAccessor(Valued.KeyVal(key))
                }
            }

            // TODO: material support
            // TODO: maybe string support, needs to refine argument parsing maybe

            return null
        }
    }
}