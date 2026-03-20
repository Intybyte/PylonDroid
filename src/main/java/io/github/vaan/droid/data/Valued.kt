package io.github.vaan.droid.data

import io.github.vaan.droid.instructions.jump.Jumpable
import org.bukkit.NamespacedKey
import java.util.UUID

interface Valued<T> : Cloneable {
    val value: T
    val type: Class<T>

    public override fun clone(): Valued<*> {
        return Valued(this)
    }

    interface CanBeString {
        fun string(): String

        fun stringValued() = StringVal(string())
    }

    interface IsNumber {
        val number: Number

        fun numberString() = number.toString()
    }

    data class IntVal(override val value: Int) : Valued<Int>, IsNumber, Jumpable {
        override val type = Int::class.java

        override val number: Number = value
        override fun evaluate(data: DynamicDroidData): Int = value
    }

    data class DoubleVal(override val value: Double) : Valued<Double>, IsNumber {
        override val type = Double::class.java

        override val number: Number = value
    }

    data class LongVal(override val value: Long) : Valued<Long>, IsNumber {
        override val type = Long::class.java

        override val number: Number = value
    }

    data class StringVal(override val value: String) : Valued<String>, CanBeString, Jumpable {
        override val type = String::class.java

        override fun evaluate(data: DynamicDroidData): Int? = data.labels[value]
        override fun string(): String = value
    }

    data class UUIDVal(override val value: UUID) : Valued<UUID>, CanBeString {
        override val type = UUID::class.java

        override fun string(): String = value.toString()
    }

    data class KeyVal(override val value: NamespacedKey) : Valued<NamespacedKey>, CanBeString {
        override val type = NamespacedKey::class.java

        override fun string(): String = value.toString()
    }

    object EmptyVal : Valued<Any?> {
        override val value: Any? = null
        override val type: Class<Any?> = Any::class.java as Class<Any?>
    }

    companion object {
        operator fun invoke(value: Any?): Valued<*> = when (value) {
            is Valued<*> -> value // already wrapped

            is Int -> IntVal(value)
            is Double -> DoubleVal(value)
            is Float -> DoubleVal(value.toDouble())
            is Long -> LongVal(value)
            is Short -> IntVal(value.toInt())
            is Byte -> IntVal(value.toInt())

            is String -> parseString(value)

            is UUID -> UUIDVal(value)

            is NamespacedKey -> KeyVal(value)

            null -> EmptyVal

            else -> error("Didn't find valid Valued for ${value::class.java.simpleName}")
        }

        fun parseString(str: String): Valued<out Comparable<*>> {
            val trimmed = str.trim()

            trimmed.toIntOrNull()?.let { return IntVal(it) }

            trimmed.toDoubleOrNull()?.let { return DoubleVal(it) }

            try {
                val uuid = UUID.fromString(trimmed)
                return UUIDVal(uuid)
            } catch (_: IllegalArgumentException) { /* not a UUID */ }

            if (":" in trimmed) {
                val parts = trimmed.split(":", limit = 2)
                if (parts.size == 2) {
                    val key = NamespacedKey(parts[0], parts[1])
                    return KeyVal(key)
                }
            }

            // TODO: material support
            // TODO: maybe string support, needs to refine argument parsing maybe

            return StringVal(str)
        }
    }
}