package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued

object PisaDie : PisaBase("DIE", "Stops execution") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.isNotEmpty()) {
            failInstruction(data, "Must have no arguments")
            return
        }

        DataRegistry.PC.set(data, Valued.IntVal(Int.MAX_VALUE - 1))
    }
}