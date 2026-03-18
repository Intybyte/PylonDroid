package io.github.vaan.droid.data

import io.github.pylonmc.rebar.registry.RebarRegistry
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.accessor.DataAccessor
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import java.lang.Exception

enum class DataRegistry : Keyed, DataAccessor {
    A, B, C, D, // generic registries
    SS(Valued.IntVal::class.java), // stack size, read only
    PC(Valued.IntVal::class.java), // program counter
    LR(Valued.IntVal::class.java), // link register
    CMP(Valued.IntVal::class.java), // comparing registries

    // registries for block position, read only
    X(Valued.IntVal::class.java),
    Y(Valued.IntVal::class.java),
    Z(Valued.IntVal::class.java),

    // location registries obtained in other ways
    TX(Valued.IntVal::class.java),
    TY(Valued.IntVal::class.java),
    TZ(Valued.IntVal::class.java),

    // used for entities, always UUID
    ID(Valued.UUIDVal::class.java);

    private val key: NamespacedKey = PylonDroid.Companion.key("registry_${this.name.lowercase()}")

    private val strictClassType: Class<*>?

    constructor(strictClassType: Class<*>? = null) {
        this.strictClassType = strictClassType
    }

    override fun getKey(): NamespacedKey = key

    override fun get(data: DynamicDroidData): Valued<*>? = data.registryValues[this]

    override fun rawSet(data: DynamicDroidData, value: Valued<*>?) = data.registryValues.set(this, value)

    override fun set(data: DynamicDroidData, value: Valued<*>?) {
        if (this in READ_ONLY_REGISTRIES) {
            data.error = true
            data.addLog("CRITICAL", "Error: ${this.name} registry is read only")
            return
        }

        if (value != null && strictClassType != null && strictClassType != value::class.java) {
            data.error = true
            data.addLog("CRITICAL", "Error: ${this.name} registry only supports values of type ${strictClassType::class.java.simpleName}")
        }

        rawSet(data, value)
    }

    companion object {
        val READ_ONLY_REGISTRIES = listOf(X, Y, Z, SS)
        val REGISTRY_KEY = PylonDroid.Companion.key("registries")

        val REGISTRY = RebarRegistry<DataRegistry>(REGISTRY_KEY).also {
            RebarRegistry.Companion.addRegistry(it)
        }

        fun of(key: String) : DataRegistry? {
            try {
                valueOf(key.uppercase()).also {
                    return@of it
                }
            } catch (_: Exception) {}

            return null
        }
    }
}