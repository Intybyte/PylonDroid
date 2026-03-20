package io.github.vaan.droid.instructions.stack

import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.instructions.base.PisaBase

object PisaPop : PisaBase("POP", "Removes values to the stack and puts them in their relative registries") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        val accessors = args.map { data.accessorOf(it) }
        if (accessors.any { it == null}) {
            failInstruction(data, "Some arguments are invalid")
            return
        }

        val stackSize = DataRegistry.SS.get(data).value as Int
        val newStackSize = stackSize - accessors.size
        if (newStackSize < 0) {
            failInstruction(data, "Stack doesn't have enough elements for this operation")
            return
        }

        for (i in 0..<accessors.size) {
            val stackPosition = stackSize - 1 - i
            accessors[i]!!.set(data, data.stack[stackPosition]!!)
        }

        DataRegistry.SS.rawSet(data, Valued(newStackSize))
    }
}