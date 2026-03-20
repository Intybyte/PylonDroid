package io.github.vaan.droid.instructions.stack

import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.instructions.base.PisaBase

object PisaPush : PisaBase("PUSH", "Push values to the stack") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        val accessors = args.map { data.accessorOf(it) }
        if (accessors.any { it == null}) {
            failInstruction(data, "Some arguments are invalid")
            return
        }

        val values = accessors.map {
            it!!.get(data)
        }

        val stackSize = DataRegistry.SS.get(data).value as Int
        val newStackSize = stackSize + values.size
        if (newStackSize >= data.static.stackSize) {
            failInstruction(data, "StackOverflow, stack isn't big enough for this operation")
            return
        }

        for (i in 0..<values.size) {
            val stackPosition = stackSize + i
            data.stack[stackPosition] = values[i].clone()
        }

        DataRegistry.SS.rawSet(data, Valued(newStackSize))
    }
}