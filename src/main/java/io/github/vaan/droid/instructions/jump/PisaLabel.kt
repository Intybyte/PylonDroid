package io.github.vaan.droid.instructions.jump

import io.github.vaan.droid.DroidKeys
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.instructions.base.PisaBase
import org.bukkit.NamespacedKey

object PisaLabel : PisaBase("LABEL", "Label, useful for jumping") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.size != 1) {
            failInstruction(data, "Must have one argument name")
        }
    }

    override fun getOwningInstructionSet(): NamespacedKey = DroidKeys.Ins.JUMP
}