package io.github.vaan.droid.instructions.base.jump

import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.instructions.base.PisaBase

object PisaLabel : PisaBase("LABEL", "Label, useful for jumping") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.size != 1) {
            failInstruction(data, "Must have one argument name")
        }
    }
}