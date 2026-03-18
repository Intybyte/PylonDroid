package io.github.vaan.droid.data

import org.bukkit.NamespacedKey
import java.util.UUID

interface Valued<T> {
    val value: T
    val type: Class<T>

    interface CanBeString {
        fun string(): String
    }

    interface IsNumber {
        val number: Number
    }

    data class IntVal(override val value: Int) : Valued<Int>, IsNumber {
        override val type = Int::class.java

        override val number: Number = value
    }

    data class DoubleVal(override val value: Double) : Valued<Double>, IsNumber {
        override val type = Double::class.java

        override val number: Number = value
    }

    data class LongVal(override val value: Long) : Valued<Long>, IsNumber {
        override val type = Long::class.java

        override val number: Number = value
    }

    data class StringVal(override val value: String) : Valued<String>, CanBeString {
        override val type = String::class.java

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

    companion object {
        operator fun invoke(value: Any?): Valued<*>? = when (value) {
            is Valued<*> -> value // already wrapped

            is Int -> IntVal(value)
            is Double -> DoubleVal(value)
            is Float -> DoubleVal(value.toDouble())
            is Long -> LongVal(value)
            is Short -> IntVal(value.toInt())
            is Byte -> IntVal(value.toInt())

            is String -> StringVal(value)

            is UUID -> UUIDVal(value)

            is NamespacedKey -> KeyVal(value)

            null -> null

            else -> error("Didn't find valid Valued for ${value::class.java.simpleName}")
        }
    }
}