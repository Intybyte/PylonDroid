package io.github.vaan.droid.instructions.base

import io.github.pylonmc.rebar.registry.RebarRegistry
import io.github.vaan.droid.DroidKeys
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import org.bukkit.Keyed
import org.bukkit.NamespacedKey

interface Instruction : Keyed {

    val name: String
    val description: String

    fun getOwningInstructionSet() = DroidKeys.Ins.BASE

    override fun getKey(): NamespacedKey = PylonDroid.key(name.lowercase())

    fun execute(data: DynamicDroidData, args: Array<String>)

    fun init() {
        REGISTRY.register(this)
    }

    fun failInstruction(data: DynamicDroidData, str: String) {
        val pc = DataRegistry.PC.get(data).value as Int
        data.addLog("CRITICAL", "Error in executing $name: $str at line $pc")
        data.error = true
    }

    companion object {

        val REGISTRY_KEY = PylonDroid.key("instructions")

        val REGISTRY = RebarRegistry<Instruction>(REGISTRY_KEY).also {
            RebarRegistry.addRegistry(it)
        }
    }
}