package io.github.vaan.droid.instructions.jump

import io.github.vaan.droid.DroidKeys
import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.instructions.base.PisaBase
import org.bukkit.NamespacedKey

object PisaRet : PisaBase("RET", "Return, jumping back to callsite after BL") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.isNotEmpty()) {
            failInstruction(data, "Must have no arguments")
            return
        }

        DataRegistry.PC.set(data, DataRegistry.LR.get(data).clone())
    }

    override fun getOwningInstructionSet(): NamespacedKey = DroidKeys.Ins.JUMP
}